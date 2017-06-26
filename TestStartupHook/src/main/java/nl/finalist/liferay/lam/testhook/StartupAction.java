package nl.finalist.liferay.lam.testhook;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;

import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.PortalProperties;

/**
 * At the moment this class is triggered every time the user logs in to test our code.
 * In the future this will either change to kick off running our DSL logic at startup of the server, 
 * or this class will have to be removed. 
 */
@Component(
		immediate = true, 
		property = { "key=login.events.pre" }, 
		service = LifecycleAction.class)
public class StartupAction implements LifecycleAction {
	@Reference
	private CustomFields customFields;
	
	@Reference
	private PortalProperties portalValues;
	
	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		System.out.println("Adding custom field");
		customFields.addCustomTextField(20116L, User.class.getName(), "test2", "default", new String[]{RoleConstants.GUEST});
		
		System.out.println("Checking portal-ext.properties");
		
		try {
			portalValues.validatePortalProperties(newProperties());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, String> newProperties() {
		Map<String, String> propertyValues = new HashMap<String,String>();
		propertyValues.put("admin.email.from.address","test@liferay.com");
		propertyValues.put("admin.email.from.name","Test Test");
		propertyValues.put("company.default.name","shlUpgrade");
		propertyValues.put("default.admin.first.name","Test");
		propertyValues.put("default.admin.last.name","Test");
		propertyValues.put("jdbc.default.driverClassName","com.mysql.jdbc.Driver");
		return propertyValues;
	}
}
