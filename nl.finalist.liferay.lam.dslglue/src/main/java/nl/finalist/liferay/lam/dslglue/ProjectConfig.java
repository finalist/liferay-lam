package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;

import org.osgi.framework.BundleContext;

/**
 * Base class for concrete project specific components.
 */
public abstract class ProjectConfig {

    protected Executor executor;

    private static final Log LOG = LogFactoryUtil.getLog(ProjectConfig.class);

    /**
     * Implement this method and annotate with @Activate. In it, call super.doActivate(context);
     *
     * @param context the BundleContext
     */
    public abstract void activate(BundleContext context);

    /**
     * Implement this setter and annotate with @Reference
     * @param executor the Executor
     */
    protected abstract void setExecutor(Executor executor);

    protected void doActivate(BundleContext context) {

        LOG.debug("Running project-specific configuration with @Activate");

        Enumeration<URL> entries = context.getBundle().findEntries("/", "*.groovy", true);
        Collections.list(entries).forEach(scriptUrl -> {

            LOG.debug("Entry : " + scriptUrl.getFile());

            InputStream input;
            try {
                input = scriptUrl.openStream();
                try {
                    executor.runScripts(new InputStreamReader(input));
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }

        });
    }

}
