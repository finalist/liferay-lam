package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;


/**
 * Validate portal properties
 */
@Component(immediate = true, service=PortalProperties.class)
public class PortalPropertiesImpl implements PortalProperties {
    private static final Log LOG = LogFactoryUtil.getLog(PortalPropertiesImpl.class);

    /**
     * Validate properties in configuration against portal properties
     *
     * @param propertyValues map with expected portal-ext.properties values
     * @return boolean indicates whether all configured properties were as expected
     *
     */
    @Override
    public boolean validatePortalProperties(Map<String, String> propertyValues) {
        LOG.debug(String.format("Start comparing the portal property values"));

        int count = 0;
        Set<String> keys = propertyValues.keySet();

        for (String key : keys) {
            if (PropsUtil.get(key) != null && propertyValues.get(key) != null
                            && PropsUtil.get(key).equals(propertyValues.get(key))) {
                LOG.debug(String.format("Property %s has expected value", key));
            } else {
                if (PropsUtil.get(key) == null) {
                    LOG.info(String.format("Property %s doesn't exist in portal-ext.properties", key));
                    count++;
                } else if (propertyValues.get(key) == null) {
                    LOG.info(String.format("Property %s was expected not to have a value, check your configuration", key));
                    count++;
                } else {
                    LOG.info(String.format("Property %s should have value %s but value is %s instead", key, propertyValues.get(key),
                                    PropsUtil.get(key)));
                    count++;
                }
            }
        }

        if(count > 0) {
            return false;
        } else {
            LOG.info("Validating the portal properties has completed successfuly, there were no mismatches");
            return true;
        }
    }
}
