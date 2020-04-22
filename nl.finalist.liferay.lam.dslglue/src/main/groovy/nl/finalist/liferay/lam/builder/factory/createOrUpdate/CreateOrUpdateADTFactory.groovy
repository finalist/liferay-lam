package nl.finalist.liferay.lam.builder.factory.createOrUpdate

import org.osgi.framework.Bundle;

import nl.finalist.liferay.lam.api.ADT;
import nl.finalist.liferay.lam.dslglue.model.ADTModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;


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

        // Method call updated to use webIds available in groovy model
        adtService.createOrUpdateADT(model.webIds, model.adtKey, model.file, bundle ,model.type, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap));
    }
}