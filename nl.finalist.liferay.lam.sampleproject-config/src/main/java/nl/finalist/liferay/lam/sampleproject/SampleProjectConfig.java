package nl.finalist.liferay.lam.sampleproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import nl.finalist.liferay.lam.dslglue.Executor;

@Component(immediate = true)
public class SampleProjectConfig  {

	private static final Log LOG = LogFactoryUtil.getLog(SampleProjectConfig.class);

	@Reference
	private Executor executor;
	
	@Activate
	public void activate(BundleContext context) {
		LOG.info("Running project-specific configuration with @Activate");
		
		URL configUrl = context.getBundle().getEntry("test.groovy");
	    if (configUrl != null) {
	        InputStream input;
			try {
				input = configUrl.openStream();
				try {
		        	executor.runScripts(new InputStreamReader(input));
		        } finally {
		            input.close();
		        }
			} catch (IOException e) {
				LOG.error(e);
			}
	    }
	}
}
