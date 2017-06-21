package nl.finalist.liferay.lam.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.liferay.portal.kernel.util.PropsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropsUtil.class })
public class PortalPropertiesImplTest {

	@InjectMocks
	private PortalPropertiesImpl portalPropertiesImplementation;

	@Before
	public void setUp() {
		portalPropertiesImplementation = new PortalPropertiesImpl();
	}

	@Test
	public void testPortalExtPropertiesForTrue() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("include-and-override")).thenReturn("portal-ext.properties");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configPropertiesTrue());
		Assert.assertTrue(c);
	}
	
	@Test
	public void testPortalExtPropertiesForFalse() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("include-and-override")).thenReturn("portal-ext.properties");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configPropertiesFalse());
		Assert.assertFalse(c);
	}


	public Map<String, String> configPropertiesFalse() {
		Map<String, String> propertyValues = new HashMap<String, String>();
		propertyValues.put("include-and-override", "portal-ext.properties");
		propertyValues.put("admin.email.from.address", "test@liferay.com");
		propertyValues.put("admin.email.from.name", "Ashwini Sidhanti");
		propertyValues.put( "company.default.name","shlUpgrade");
		propertyValues.put("default.admin.first.name", "Ashwini");
		propertyValues.put("default.admin.last.name", "Sidhanti");
		propertyValues.put("jdbc.default.driverClassName", "com.mysql.jdbc.Driver");
		propertyValues.put("jdbc.default.password", "test");
		propertyValues.put("jdbc.default.url",
				"jdbc:mysql://localhost:3307/locatievinderdxp?characterEncoding=UTF-8&dontTrackOpenResources=true&holdResultsOpenOverStatementClose=true&useFastDateParsing=false&useUnicode=true&useSSL=false");
		propertyValues.put("jdbc.default.username", "root");
		propertyValues.put("liferay.home", "C:/liferay-dxp-digital-enterprise-7.0-ga1");
		propertyValues.put("setup.wizard.enabled", "false");
		return propertyValues;
	}

	public Map<String, String> configPropertiesTrue() {
		Map<String, String> propertyValues = new HashMap<String, String>();
		propertyValues.put("include-and-override", "portal-ext.properties");
		return propertyValues;
	}

}
