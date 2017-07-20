package nl.finalist.liferay.lam.builder.factory;

import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.dslglue.CustomFieldType;
import nl.finalist.liferay.lam.dslglue.CustomFieldModel;

class CreateCustomFieldsFactory extends AbstractFactory {

    CustomFields customFieldsService;

    CreateCustomFieldsFactory(CustomFields customFields) {
        this.customFieldsService = customFields;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new CustomFieldModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        CustomFieldModel cf = (CustomFieldModel) node;
        switch (CustomFieldType.valueOf(cf.type)) {
            case(CustomFieldType.INTEGER_32):
                customFieldsService.addCustomIntegerField(cf.getEntityName(), cf.getName(), (int) cf.getDefaultValue(), cf.getRoles());
                break;
            case(CustomFieldType.TEXT):
                customFieldsService.addCustomTextField(cf.getEntityName(), cf.getName(), (String) cf.getDefaultValue(), cf.getRoles());
                break;
            case(CustomFieldType.TEXT_GROUP):
                customFieldsService.addCustomTextArrayField(cf.getEntityName(), cf.getName(), (String) cf.getDefaultValue(), cf.getRoles(), cf.getDisplayType());
                break;
        }
    }
}
