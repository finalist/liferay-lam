package nl.finalist.liferay.lam.builder.factory.createOrUpdate
import org.osgi.framework.Bundle;

import nl.finalist.liferay.lam.api.Structure;
import nl.finalist.liferay.lam.dslglue.model.StructureModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;
class CreateOrUpdateStructureFactory extends AbstractFactory {

    Structure structureService;
    Bundle bundle;

    CreateOrUpdateStructureFactory(Structure structureService, Bundle bundle) {
        this.structureService = structureService;
        this.bundle = bundle;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new StructureModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        StructureModel model = (StructureModel) node;

        // Method call updated to use webIds available in groovy model
        structureService.createOrUpdateStructure(model.webIds, model.structureKey, model.file, bundle, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap), model.siteKey);
    }
}
