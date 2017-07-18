package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalProperties;
import nl.finalist.liferay.lam.builder.factory.ValidatePortalPropertiesFactory;

class ValidateFactoryBuilder extends FactoryBuilderSupport {

    ValidateFactoryBuilder(PortalProperties portalPropertiesService){
        registerFactory("portalProperties", new ValidatePortalPropertiesFactory(portalPropertiesService));
    }
}
