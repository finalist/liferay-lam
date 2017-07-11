package nl.finalist.liferay.lam.builder.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalSettings;

public class UpdatePortalSettingsFactory extends AbstractFactory {
	
	
	private static final Log LOG = LogFactoryUtil.getLog(UpdatePortalSettingsFactory.class);

	PortalSettings portalSettingsservice;

	public UpdatePortalSettingsFactory(PortalSettings portalSettingsService) {
		this.portalSettingsservice = portalSettingsService;
	}

	@Override
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
			throws InstantiationException, IllegalAccessException {
		String virtualHostName = (String)attributes.get("virtualHostName");
		String homeUrl = (String) attributes.get("homeUrl");
		
		LOG.info(String.format("VirtualHostName %s & homeUrl %s", virtualHostName, homeUrl));
		return attributes;
	}
	
	@Override
	public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
		super.onNodeCompleted(builder, parent, node);
		LOG.info("NODE Portalsettings COMPLETED");
	}
	
// TODO
//	private void callUpdateMethod(String methodName, String value){
//		String methodName = "setHomeURL";
//		Class<?> c;
//		Method method = null;
//		try {
//			c = portalSettingsservice.getClass();
//			method = c.getDeclaredMethod (methodName, String.class);
//		} catch (NoSuchMethodException | SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			method.invoke (portalSettingsservice,  value);
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//	}
}
