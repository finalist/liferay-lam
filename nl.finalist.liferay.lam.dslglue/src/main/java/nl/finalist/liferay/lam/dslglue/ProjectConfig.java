package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        context.getBundle().
        Enumeration<URL> structureUrls = context.getBundle().findEntries("/", "*.json", true);
        Map<String,String> structures = new HashMap<>();
        Collections.list(structureUrls).forEach(structureUrl -> {
            LOG.info(context.getBundle().getResource("myStructure"));
            InputStream input;

            try {
                input = structureUrl.openStream();
                String structure = "";
                Charset charset = Charset.forName("UTF-8");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(input, charset))) {
                    structure = br.lines().collect(Collectors.joining(System.lineSeparator()));
                    structures.put(structureUrl.getFile().replaceAll(".json", "").replaceAll("/",""), structure);
                }
                finally {
                    input.close();
                }
            }

            catch (IOException e) {
                LOG.error("IOException while reading input"+e);
            }


        });
        Collections.list(entries).forEach(scriptUrl -> {

            LOG.debug("Entry : " + scriptUrl.getFile());

            InputStream input;
            try {
                input = scriptUrl.openStream();
                try {
                    executor.runScripts(structures, new InputStreamReader(input));
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }

        });

    }

}
