package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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
	public void runScripts() {
		LOG.debug("DSL Executor running the available scripts");

		Binding sharedData = new Binding();
		
		// Add all available API classes to the context of the scripts 
		sharedData.setVariable("customFields", customFields);
		GroovyShell shell = new GroovyShell(sharedData);


		// TODO: get the path to the groovy scripts from a parameter
		// TODO: evaluate the scripts

		// Evaluate an actual Groovy script (PoC, to be removed)
		shell.evaluate("int foo=123; println 'from within groovy script: foo has value: ' + foo");

		// Access CustomFields class instance from within script:  (PoC, to be removed)
		shell.evaluate("println 'customFields is: ' + customFields");
	}
}