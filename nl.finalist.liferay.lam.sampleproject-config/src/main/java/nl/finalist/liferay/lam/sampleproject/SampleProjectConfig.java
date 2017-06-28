package nl.finalist.liferay.lam.sampleproject;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.dslglue.Executor;

@Component(immediate = true)
public class SampleProjectConfig  {

	private static final Log LOG = LogFactoryUtil.getLog(SampleProjectConfig.class);

	@Reference
	private Executor executor;
	
	@Activate
	public void activate() {
		LOG.info("Running project-specific configuration with @Activate");
		executor.runScripts("script1.groovy");
	}
}