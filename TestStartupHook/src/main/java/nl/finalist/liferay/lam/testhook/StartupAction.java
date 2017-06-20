package nl.finalist.liferay.lam.testhook;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.model.User;

import nl.finalist.liferay.lam.api.CustomFieldsInterface;
import nl.finalist.liferay.lam.api.PortalValuesInterface;

@Component(
		immediate = true, 
		property = { "key=login.events.pre" }, 
		service = LifecycleAction.class)
public class StartupAction implements LifecycleAction {
	@Reference
	private CustomFieldsInterface customFields;
	
	@Reference
	private PortalValuesInterface portalValues;
	
	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		System.out.println("Adding custom field");
		customFields.addCustomTextField(20116L, User.class.getName(), "test2", "default");
		
		System.out.println("Checking portal-ext.properties");
		Map<String, String> expectedValues = new HashMap<>();
		expectedValues.put("theme.css.fast.load", "false");
		try {
			portalValues.checkingPortalProperties(expectedValues);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
