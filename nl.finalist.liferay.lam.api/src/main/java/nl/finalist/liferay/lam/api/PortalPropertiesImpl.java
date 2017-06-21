package nl.finalist.liferay.lam.api;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsUtil;


/**
 * This class Accepts a Map of PropertyValues and validates it against 
 * the portal-ext property values and returns a true or false.  
 *  
 */


@Component(immediate = true, service=PortalPropertiesInterface.class)
public class PortalPropertiesImpl implements PortalPropertiesInterface {
	private static final Log LOG = LogFactoryUtil.getLog(PortalPropertiesImpl.class);
	
	/**
     * Validating the User PropertyValues
     * 
     * @param propertyValues map that the validation takes with portal-ext.property values
     * @return boolean that satisfying the validation between user map and portal-ext.property values.
     * 
     */
	public boolean checkingPortalProperties(Map<String, String> propertyValues) {
		LOG.info(String.format("Start the comparing the portal property values"));
		
		
		int count = 0;
		String[] keys = propertyValues.keySet().stream().toArray(String[]::new);
		LOG.info(String.format("Number of Properties in the User Config file is %d", keys.length));
		
		
		for (String key : keys) {
			LOG.info(String.format("Property in the User config is %s", key));
			if ( PropsUtil.get(key) != null  && propertyValues.get(key) != null
					&& PropsUtil.get(key).equals(propertyValues.get(key))) {
				LOG.info("Property in the User config Map and the Portal-ext.properties are the same");
			} else {
				if (PropsUtil.get(key) == null) {
					LOG.info("The property of the User config file doesn't exist in the portal-ext.properties");
					count++;
				} else if (propertyValues.get(key) == null) {
					LOG.info("The property of portal-ext.properties doesn't exist in the User config file");
					count++;
				} else {
					LOG.info(String.format("%s is this value for %s but should be %s", propertyValues.get(key), key,
							PropsUtil.get(key)));
					count++;
				}
			}
		}
		if(count >0) {
			LOG.info(String.format("Count of mismatch properties is %d",count));
			LOG.info("Comparing the portal property values between User config file and portal-ext.property has successfully completed");
			return false;
		} else {
			LOG.info("No Mismatch recorded");
			LOG.info("Comparing the portal property values between User config file and portal-ext.property has successfully completed");
			return true;
		}
		
		
		
	}

}
