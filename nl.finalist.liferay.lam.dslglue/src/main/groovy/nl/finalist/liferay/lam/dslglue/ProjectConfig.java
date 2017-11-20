package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * Base class for concrete project specific components.
 */
public abstract class ProjectConfig {

    protected Executor executor;

    private static final Log LOG = LogFactoryUtil.getLog(ProjectConfig.class);

    /**
     * Implement this method and annotate with @Activate. In it, call
     * super.doActivate(context);
     *
     * @param context
     *            the BundleContext
     */
    public abstract void activate(BundleContext context);

    /**
     * Implement this setter and annotate with @Reference
     *
     * @param executor
     *            the Executor
     */
    protected abstract void setExecutor(Executor executor);

    protected void doActivate(BundleContext context) {
        LOG.debug("Running project-specific configuration with @Activate");
        Enumeration<URL> entries = context.getBundle().findEntries("/", "*.groovy", true);
        List<Script> scripts = Collections.list(entries).stream().map(scriptUrl -> {

            LOG.debug("Entry : " + scriptUrl.getFile());

            InputStream inputChecksum;
            InputStream input;
            try {
                inputChecksum = scriptUrl.openStream();
                input = scriptUrl.openStream();
                return new Script(scriptUrl.getFile(), new InputStreamReader(input), createChecksum(inputChecksum));

            } catch (IOException | NoSuchAlgorithmException e) {
                LOG.error(e);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        flyway(scripts, context.getBundle());
    }

    protected void flyway(List<Script> scripts, Bundle bundle) {
        LOG.info("Running flyway migrations with scripts: " + scripts);
        Flyway flyway = new Flyway();

        flyway.setSkipDefaultResolvers(true);

        flyway.setDataSource(InfrastructureUtil.getDataSource());

        flyway.setResolvers((MigrationResolver) () -> scripts.stream()
                        .map(s -> new FlywayLAMMigration(bundle, executor, s)).collect(Collectors.toList()));

        flyway.setTable("LAM_Changelog");
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersion(MigrationVersion.fromVersion("0"));
        flyway.setBaselineVersionAsString("0");
        flyway.setBaselineDescription("Baseline");
        flyway.migrate();
    }

    protected Integer createChecksum(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("SHA-1");

        int numRead;
        do {
            numRead = inputStream.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        inputStream.close();
        return new BigInteger(complete.digest()).intValue();
    }

}
