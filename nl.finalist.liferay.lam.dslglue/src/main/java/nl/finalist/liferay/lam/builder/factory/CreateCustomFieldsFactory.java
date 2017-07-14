package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.model.CustomFieldModel;

public class CreateCustomFieldsFactory extends AbstractFactory {

    private static final Log LOG = LogFactoryUtil.getLog(CreateCustomFieldsFactory.class);

    CustomFields customFieldsService;

    public CreateCustomFieldsFactory(CustomFields customFields) {
        this.customFieldsService = customFields;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        CustomFieldModel customFieldObject = null;
        if (attributes != null) {
            ArrayList<String> list = (ArrayList<String>) attributes.get("roles");
            String[] rolesArray = list.toArray(new String[list.size()]);
            customFieldObject = new CustomFieldModel();
            customFieldObject.setName((String) attributes.get("name"));
            customFieldObject.setDefaultValue(attributes.get("defaultValue"));
            customFieldObject.setRoles(rolesArray);
            customFieldObject.setType((String) attributes.get("type"));
            customFieldObject.setEntityName((String) attributes.get("entityName"));
        }

        return customFieldObject;
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        CustomFieldModel cf = (CustomFieldModel) node;
        LOG.info("Complete : " + cf + " : service : " + customFieldsService);
        if (cf.getType().equalsIgnoreCase("Int")) {
            customFieldsService.addCustomIntegerField(cf.getEntityName(), cf.getName(), (int) cf.getDefaultValue(), cf.getRoles());
        }
        if (cf.getType().equalsIgnoreCase("String")) {
            customFieldsService.addCustomTextField(cf.getEntityName(), cf.getName(), (String) cf.getDefaultValue(), cf.getRoles());
        }

    }

    //    private void dumpAttributes(Map attributes) {
    //        LOG.info("attributes count : " + attributes.size());
    //        for (Object obj : attributes.entrySet()) {
    //            Map.Entry<String, Object> entry = (Entry<String, Object>) obj;
    //            LOG.info("Item : " + entry.getKey() + " Value : " + entry.getValue() + " Type : "
    //                            + entry.getValue().getClass().getName());
    //        }
    //    }

}
