package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.dslglue.model.CustomFieldModel;

class DeleteCustomFieldsFactory extends AbstractFactory{
    CustomFields customFieldsService;

    DeleteCustomFieldsFactory(CustomFields customFieldsService){
        this.customFieldsService = customFieldsService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new CustomFieldModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        CustomFieldModel cf = (CustomFieldModel) node;
        customFieldsService.deleteCustomField(cf.getEntityName(), cf.getName());
    }
}
