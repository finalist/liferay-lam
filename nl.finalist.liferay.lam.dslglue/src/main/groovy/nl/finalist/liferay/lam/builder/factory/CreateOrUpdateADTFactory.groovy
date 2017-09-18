package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.ADT;
import nl.finalist.liferay.lam.dslglue.LocaleMapConverter;
import nl.finalist.liferay.lam.dslglue.model.ADTModel;
import org.osgi.framework.Bundle;


class CreateOrUpdateADTFactory extends AbstractFactory {

    ADT adtService;
    Bundle bundle;

    CreateOrUpdateADTFactory(ADT adtService, Bundle bundle) {
        this.adtService = adtService;
        this.bundle = bundle;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new ADTModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        ADTModel model = (ADTModel) node;
        
      
        adtService.createOrUpdateADT(model.file, bundle ,model.type, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap));
    }
}