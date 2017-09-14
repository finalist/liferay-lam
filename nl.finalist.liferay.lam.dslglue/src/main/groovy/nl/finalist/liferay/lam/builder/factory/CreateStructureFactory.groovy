package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.model.StructureModel

class CreateStructureFactory extends AbstractFactory {

    Structure structureService;

    CreateStructureFactory(Structure structureService) {
        this.structureService = structureService;
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
       
        structureService.addStructure(model.file);
    }
}
