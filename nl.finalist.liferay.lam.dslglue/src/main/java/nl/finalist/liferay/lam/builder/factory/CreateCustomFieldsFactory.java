package nl.finalist.liferay.lam.builder.factory;

import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Entity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.service.component.annotations.Reference;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.builder.CustomField;

public class CreateCustomFieldsFactory extends AbstractFactory {

	private static final Log LOG = LogFactoryUtil.getLog(CreateCustomFieldsFactory.class);
	

	CustomFields service;
	
	public CreateCustomFieldsFactory(CustomFields customFields) {
		this.service = customFields;
	}



	@Override
	public Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
			throws InstantiationException, IllegalAccessException {
		CustomField customField = null;
	
		//dumpAttributes(attributes);
		ArrayList<String> list = (ArrayList<String>) attributes.get("roles");
		String[] rolesArray = list.toArray(new String[list.size()]);
	
		
		customField = new CustomField();
		if ( attributes != null ){
			customField.setName( objectName.toString() );
			customField.setDefaultValue(attributes.get("defaultValue"));
			customField.setRoles(rolesArray );
			customField.setType((int) attributes.get("type") );
			customField.setValue(value);			
		}
		

		
		return customField;
	}

	
	
	@Override
	public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
		super.onNodeCompleted(builder, parent, node);
		CustomField cf = (CustomField)node;	
		LOG.debug("Complete : " + cf + " : service : " + service );
		if( cf.getType() == ExpandoColumnConstants.INTEGER){
			//service.addCustomIntegerField(20116, "Entities.user", cf.getName(), cf.getValue().toString(), cf.getRoles());
		}

	}



	private void dumpAttributes(Map attributes) {
		LOG.debug("attributes count : " + attributes.size());
		for( Object  obj : attributes.entrySet()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) obj;
			LOG.debug("Item : " + entry.getKey() + " Value : " + entry.getValue() + " Type : " + entry.getValue().getClass().getName());
		}
	}

}
