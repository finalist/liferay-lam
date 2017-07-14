package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.model.CustomFieldModel;

public class DeleteCustomFieldsFactory extends AbstractFactory{
    private static final Log LOG = LogFactoryUtil.getLog(DeleteCustomFieldsFactory.class);
    CustomFields customFieldsService;

    public DeleteCustomFieldsFactory(CustomFields customFieldsService){
        this.customFieldsService = customFieldsService;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        CustomFieldModel customField = null;
        if (attributes != null) {
            customField = new CustomFieldModel();
            customField.setName((String) attributes.get("name"));
            customField.setEntityName((String) attributes.get("entityName"));
        }
        return customField;
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        CustomFieldModel cf = (CustomFieldModel) node;
        customFieldsService.deleteCustomField(cf.getEntityName(), cf.getName());
        LOG.info("DeleteCustomField node completed");
    }

}
