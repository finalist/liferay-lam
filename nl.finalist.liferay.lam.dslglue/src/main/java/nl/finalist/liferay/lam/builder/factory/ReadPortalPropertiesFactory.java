package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.HashMap;
import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalProperties;

public class ReadPortalPropertiesFactory  extends AbstractFactory {

    private static final Log LOG = LogFactoryUtil.getLog(CreateCustomFieldsFactory.class);


    PortalProperties portalPropertiesService;

    public ReadPortalPropertiesFactory(PortalProperties portalPropertiesService) {
        this.portalPropertiesService = portalPropertiesService;
    }



    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        Map<String, String> extProperties = new HashMap<>(attributes);
        portalPropertiesService.validatePortalProperties(extProperties);

        return null;
    }



    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        LOG.info("PortalProperties node completed");
    }

}
