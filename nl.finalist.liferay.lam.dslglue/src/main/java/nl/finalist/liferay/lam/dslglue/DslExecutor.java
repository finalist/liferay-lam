package nl.finalist.liferay.lam.dslglue;

import java.io.Reader;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

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
	public void runScripts(Reader... scripts) {
		LOG.debug("DSL Executor running the available scripts");

		Binding sharedData = new Binding();


		// Add all available API classes to the context of the scripts 
		sharedData.setVariable("customFields", customFields);
		sharedData.setVariable("LOG", LOG);

        CompilerConfiguration conf = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();

        // Make these imports available to the scripts
        imports.addImport("Roles", "nl.finalist.liferay.lam.dslglue.Roles");
        imports.addImport("Entities", "nl.finalist.liferay.lam.dslglue.Entities");
        imports.addImport("CustomFieldsOperation", "nl.finalist.liferay.lam.dslglue.CustomFieldsOperation");
        imports.addImport("CustomField", "nl.finalist.liferay.lam.dslglue.CustomField");
//        imports.addStaticImport("nl.finalist.liferay.lam.dslglue.Entrypoint", "customField");
//        imports.addStaticImport("nl.finalist.liferay.lam.dslglue.Entrypoint", "with");

        
        conf.addCompilationCustomizers(imports);

        // Use the classloader of this class
        ClassLoader classLoader = this.getClass().getClassLoader();

        GroovyShell shell = new GroovyShell(classLoader, sharedData, conf);

        for (Reader script : scripts) {
            shell.evaluate(script);
        }
	}
}