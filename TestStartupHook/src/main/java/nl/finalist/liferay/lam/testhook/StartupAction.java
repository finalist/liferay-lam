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
import nl.finalist.liferay.lam.api.PortalValuesInterface;

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
	private PortalValuesInterface portalValues;
	
	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		System.out.println("Adding custom field");
		customFields.addCustomTextField(20116L, User.class.getName(), "test2", "default", new String[]{RoleConstants.GUEST});
		
		System.out.println("Checking portal-ext.properties");
		
		try {
			portalValues.checkingPortalProperties(newProperties());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static Map<String , String> newProperties() {
		Map<String, String> propertyValues = new HashMap<String,String>();
		propertyValues.put("include-and-override", "portal-ext.properties");
		propertyValues.put(	"admin.email.from.address","test@liferay.com");
		propertyValues.put(	"admin.email.from.name","Ashwini Sidhanti");
		//propertyValues.put(	"company.default.name","shlUpgrade");
		propertyValues.put(	"default.admin.first.name","Ashwini");
		propertyValues.put(	"default.admin.last.name","Sidhanti");
		propertyValues.put(	"jdbc.default.driverClassName","com.mysql.jdbc.Driver");
		propertyValues.put(	"jdbc.default.password","test");
		propertyValues.put(	"jdbc.default.url","jdbc:mysql://localhost:3307/locatievinderdxp?characterEncoding=UTF-8&dontTrackOpenResources=true&holdResultsOpenOverStatementClose=true&useFastDateParsing=false&useUnicode=true&useSSL=false");
		propertyValues.put(	"jdbc.default.username","root");
		propertyValues.put(	"liferay.home","C:/liferay-dxp-digital-enterprise-7.0-ga1");
		propertyValues.put(	"setup.wizard.enabled","false");
		return  propertyValues;
	}

}
