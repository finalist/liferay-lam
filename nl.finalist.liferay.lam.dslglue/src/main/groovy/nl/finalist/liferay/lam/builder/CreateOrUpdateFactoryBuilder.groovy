package nl.finalist.liferay.lam.builder;

import org.osgi.framework.Bundle;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Structure;
import nl.finalist.liferay.lam.api.Template;
import nl.finalist.liferay.lam.api.ADT;
import nl.finalist.liferay.lam.builder.factory.CreateOrUpdateStructureFactory;
import nl.finalist.liferay.lam.builder.factory.CreateOrUpdateTemplateFactory;
import nl.finalist.liferay.lam.builder.factory.CreateOrUpdateADTFactory;

class CreateOrUpdateFactoryBuilder extends FactoryBuilderSupport {


    CreateOrUpdateFactoryBuilder(Structure structureService, Template templateService, ADT adtService, Bundle bundle){
        registerFactory("structure", new CreateOrUpdateStructureFactory(structureService, bundle));
        registerFactory("template", new CreateOrUpdateTemplateFactory(templateService, bundle))
        registerFactory("adt", new CreateOrUpdateADTFactory(adtService, bundle))
    }
}
