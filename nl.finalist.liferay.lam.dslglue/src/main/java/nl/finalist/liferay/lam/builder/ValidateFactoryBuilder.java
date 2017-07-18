package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalProperties;
import nl.finalist.liferay.lam.builder.factory.ValidatePortalPropertiesFactory;

public class ValidateFactoryBuilder extends FactoryBuilderSupport {

    public ValidateFactoryBuilder(PortalProperties portalPropertiesService){
        System.out.println("Registering readfactorybuilder");
        registerFactory("portalProperties", new ValidatePortalPropertiesFactory(portalPropertiesService));
    }

}
