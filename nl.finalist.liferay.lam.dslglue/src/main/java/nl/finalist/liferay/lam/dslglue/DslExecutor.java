package nl.finalist.liferay.lam.dslglue;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;

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
		sharedData.setVariable("userClassName", User.class.getName());
		sharedData.setVariable("guestRole", RoleConstants.GUEST);
		


        CompilerConfiguration conf = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();

        // Make these imports available to the scripts
        imports.addStaticImport("nl.finalist.liferay.lam.dslglue.Entrypoint", "with");

        conf.addCompilationCustomizers(imports);

        // Use the classloader of this class
        ClassLoader classLoader = this.getClass().getClassLoader();

        GroovyShell shell = new GroovyShell(classLoader, sharedData, conf);

        for (Reader script : scripts) {
            shell.evaluate(script);
        }
	}
}