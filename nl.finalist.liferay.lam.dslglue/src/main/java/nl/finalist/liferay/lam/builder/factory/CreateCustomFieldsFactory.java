package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.model.CustomFieldModel;

public class CreateCustomFieldsFactory extends AbstractFactory {

    private static final Log LOG = LogFactoryUtil.getLog(CreateCustomFieldsFactory.class);

    CustomFields service;

    public CreateCustomFieldsFactory(CustomFields customFields) {
        this.service = customFields;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        CustomFieldModel customFieldObject = null;

        ArrayList<String> list = (ArrayList<String>) attributes.get("roles");
        String[] rolesArray = list.toArray(new String[list.size()]);
        dumpAttributes(attributes);
        customFieldObject = new CustomFieldModel();
        if (attributes != null) {
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
        LOG.info("Complete : " + cf + " : service : " + service);
        if (cf.getType().equalsIgnoreCase("Int")) {
            service.addCustomIntegerField(cf.getEntityName(), cf.getName(), (int) cf.getDefaultValue(), cf.getRoles());
        }
        if (cf.getType().equalsIgnoreCase("String")) {
            service.addCustomTextField(cf.getEntityName(), cf.getName(), (String) cf.getDefaultValue(), cf.getRoles());
        }

    }

    private void dumpAttributes(Map attributes) {
        LOG.info("attributes count : " + attributes.size());
        for (Object obj : attributes.entrySet()) {
            Map.Entry<String, Object> entry = (Entry<String, Object>) obj;
            LOG.info("Item : " + entry.getKey() + " Value : " + entry.getValue() + " Type : "
                            + entry.getValue().getClass().getName());
        }
    }

}
