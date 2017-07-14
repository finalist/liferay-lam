package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalProperties;
import nl.finalist.liferay.lam.builder.factory.ReadPortalPropertiesFactory;

public class ReadFactoryBuilder extends FactoryBuilderSupport {

    public ReadFactoryBuilder(PortalProperties portalPropertiesService){
        System.out.println("Registering readfactorybuilder");
        registerFactory("portalProperties", new ReadPortalPropertiesFactory(portalPropertiesService));
    }

}
