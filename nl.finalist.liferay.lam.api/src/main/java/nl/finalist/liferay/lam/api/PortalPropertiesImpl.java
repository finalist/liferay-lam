package nl.finalist.liferay.lam.api;

import java.io.IOException;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsUtil;


/**
 * This class Accepts a Map of PropertyValues and validates it against 
 * the portal-ext property values and returns a true or false.  
 *  
 */

@Component(immediate = true, service=PortalPropertiesInterface.class)
public class PortalPropertiesImpl implements PortalPropertiesInterface {
	private static final Log LOG = LogFactoryUtil.getLog(PortalPropertiesImpl.class);

	public boolean checkingPortalProperties(Map<String, String> propertyValues) throws IOException {
		LOG.info(String.format("Start the comparing the portal property values"));
		int count = 0;
		String[] keys = propertyValues.keySet().stream().toArray(String[]::new);
		LOG.info(String.format("Set of key values %d", keys.length));
	
		for (String key : keys) {
			LOG.info(String.format("key is  %s ", key));
		LOG.info(PrefsPropsUtil.getPrefsProps());
			if ( PropsUtil.get(key) != null  && propertyValues.get(key) != null
					&& PropsUtil.get(key).equals(propertyValues.get(key))) {
				LOG.info("Passed the check");

			} else {
				if (PropsUtil.get(key) == null) {
					LOG.info("The property doesn't exist in the portal-ext.properties");
					count++;
				} else if (propertyValues.get(key) == null) {
					LOG.info("The property doesn't exist in the User config file");
					count++;
				} else {
					LOG.info(String.format("%s is this value for %s but should be %s", propertyValues.get(key), key,
							PropsUtil.get(key)));
					count++;
				}
			}
		}
		if(count >0) {
			return false;
		} else {
			return true;
		}

	}

}
