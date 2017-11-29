package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;

import nl.finalist.liferay.lam.statemgnt.api.MigrationExecutor;
import nl.finalist.liferay.lam.statemgnt.api.ScriptMigration;
import nl.finalist.liferay.lam.statemgnt.api.StateManager;

/**
 * Base class for concrete project specific components.
 */
public abstract class ProjectConfig {

    private static final Log LOG = LogFactoryUtil.getLog(ProjectConfig.class);

    protected Executor executor;

    protected StateManager stateManager;

    /**
     * Implement this method and annotate with @Activate. In it, call
     * super.doActivate(context);
     *
     * @param context the BundleContext
     */
    public abstract void activate(BundleContext context);

    /**
     * Implement this setter and annotate with @Reference
     *
     * @param executor the Executor
     */
    protected abstract void setExecutor(Executor executor);

    /**
     * Implement this setter and annotate with @Reference
     * @param stateManager the StateManager
     */
    protected abstract void setStateManager(StateManager stateManager);

    protected void doActivate(BundleContext context) {
        LOG.debug("Running project-specific configuration with @Activate");
        Enumeration<URL> entries = context.getBundle().findEntries("/", "*.groovy", true);

        List<ScriptMigration> scripts = Collections
            .list(entries)
            .stream()
            .map(s -> new ScriptMigration() {

                @Override
                public String getName() {
                    return s.getFile();
                }

                @Override
                public InputStream getStream() throws IOException {
                    return s.openStream();
                }

                @Override
                public MigrationExecutor getMigrationExecutor() {
                    return () -> {
                        try {
                            executor.runScripts(context.getBundle(), new InputStreamReader(s.openStream()));
                        } catch (IOException e) {
                            LOG.error("While executing script " + getName(), e);
                        }
                    };
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        stateManager.migrate(scripts);
    }
}