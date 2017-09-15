package nl.finalist.liferay.lam.builder;

import org.osgi.framework.Bundle;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Structure;
import nl.finalist.liferay.lam.builder.factory.CreateOrUpdateStructureFactory;

class CreateOrUpdateFactoryBuilder extends FactoryBuilderSupport {


    CreateOrUpdateFactoryBuilder(Structure structureService, Bundle bundle){
        registerFactory("structure", new CreateOrUpdateStructureFactory(structureService, bundle));
    }
}
