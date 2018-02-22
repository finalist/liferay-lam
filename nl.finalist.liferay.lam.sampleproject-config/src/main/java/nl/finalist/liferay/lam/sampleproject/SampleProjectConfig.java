package nl.finalist.liferay.lam.sampleproject;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.dslglue.Executor;
import nl.finalist.liferay.lam.dslglue.ProjectConfig;
import nl.finalist.liferay.lam.statemgnt.api.StateManager;

/**
 * This is an example of a Component that you need to include in your project specific config module.
 * By default, all groovy files on the classpath's root directory will be used.
 * TODO: make this overridable.
 *
 */
@Component(immediate = true)
public class SampleProjectConfig extends ProjectConfig {

	@Override
	@Activate
	public void activate(BundleContext context) {
	    super.doActivate(context);
	}

	@Override
	@Reference
	protected void setExecutor(Executor executor) {
		this.executor = executor;
	}

	@Override
    @Reference
	protected void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
}
