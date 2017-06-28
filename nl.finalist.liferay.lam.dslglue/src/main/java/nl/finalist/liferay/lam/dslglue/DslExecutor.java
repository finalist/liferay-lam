package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import java.io.FileReader;
import java.io.IOException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import nl.finalist.liferay.lam.api.CustomFields;

/**
 * Executor that evaluates configured scripts using a context containing all available APIs.
 */
@Component(immediate = true, service=Executor.class)
public class DslExecutor implements Executor {

	private static final Log LOG = LogFactoryUtil.getLog(DslExecutor.class);

	@Reference
	private CustomFields customFields;

	@Activate
	public void activate() {
		LOG.debug("Bundle Activate DslExecutor");
	}

	@Override
	public void runScripts(String... scripts) {
		LOG.debug("DSL Executor running the available scripts");

		Binding sharedData = new Binding();


		// Add all available API classes to the context of the scripts 
		sharedData.setVariable("customFields", customFields);
		sharedData.setVariable("LOG", LOG);


        CompilerConfiguration conf = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();

        // Make these imports available to the scripts
        // Work in progres...
//        imports.addStaticImport("nl.finalist.liferay.lam.dslglue.Entrypoint", "with");

        conf.addCompilationCustomizers(imports);

        // Use the classloader of this class
        ClassLoader classLoader = this.getClass().getClassLoader();

        GroovyShell shell = new GroovyShell(classLoader, sharedData, conf);

        for (String scriptFilename : scripts) {
            try {
                shell.evaluate(new FileReader(classLoader.getResource(scriptFilename).getFile()));
            } catch (IOException e) {
                LOG.warn("While trying to run script: " + scriptFilename, e);
            }
        }
	}

    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }
}