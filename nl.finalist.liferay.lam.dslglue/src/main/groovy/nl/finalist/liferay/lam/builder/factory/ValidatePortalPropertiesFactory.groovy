package nl.finalist.liferay.lam.builder.factory;

import java.util.HashMap;
import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalProperties;

class ValidatePortalPropertiesFactory extends AbstractFactory {

    PortalProperties portalPropertiesService;

    ValidatePortalPropertiesFactory(PortalProperties portalPropertiesService) {
        this.portalPropertiesService = portalPropertiesService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        Map<String, String> extProperties = new HashMap<>(attributes);
        portalPropertiesService.validatePortalProperties(extProperties);

        return null;
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
    }
}
