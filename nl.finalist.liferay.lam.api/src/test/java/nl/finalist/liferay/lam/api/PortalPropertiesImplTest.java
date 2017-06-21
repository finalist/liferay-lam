package nl.finalist.liferay.lam.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

	/*
	 * Property exists in both the files
	 */
	@Test
	public void testPortalExtPropertiesForTrue() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		
		PowerMockito.when(PropsUtil.get("admin.email.from.address")).thenReturn("test@liferay.com");
		PowerMockito.when(PropsUtil.get("admin.email.from.name")).thenReturn("Test Test");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configProperties());
		Assert.assertTrue(c);
	}
	
	/*
	 * User config has only one property but protal-ext has lot more property values.
	 * Then it should return false.
	 */
	
	@Test
	public void testPortalExtPropertiesForFalse() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("admin.email.from.address")).thenReturn("test@liferay.com");
		PowerMockito.when(PropsUtil.get("admin.email.from.name")).thenReturn("Test Test");
		PowerMockito.when(PropsUtil.get("company.default.name")).thenReturn("shlUgrade");
		PowerMockito.when(PropsUtil.get("default.admin.first.name")).thenReturn("Test");
		PowerMockito.when(PropsUtil.get("default.admin.last.name")).thenReturn("Test");
		PowerMockito.when(PropsUtil.get("jdbc.default.password")).thenReturn("test");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configProperties());
		Assert.assertTrue(c);
	}

	/*
	 * Mismatch between the values of property of user config and portal-ext.properties
	 */
	@Test
	public void testPortalExtPropertiesMismatchValues() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("admin.email.from.address")).thenReturn("test@liferay.nl");
		PowerMockito.when(PropsUtil.get("admin.email.from.name")).thenReturn("Test Test");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configProperties());
		Assert.assertFalse(c);
	}
	
	/*
	 * Properties doesn't exist in portal-ext.property file
	 */
	@Test
	public void testPortalExtPropertiesDoesNotExist() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("admin.email.from.address")).thenReturn("test@liferay.com");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configProperties());
		Assert.assertFalse(c);
	}
	
	/*
	 * Property exist in portal-ext.property file but not in User config property.
	 */
	
	@Test
	public void testPortalExtPropertiesDoesnotExistInUserConfig() throws IOException {
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("include-and-override")).thenReturn("xxxxx");
		boolean c = portalPropertiesImplementation.checkingPortalProperties(configPropertiesFalse());
		Assert.assertFalse(c);
	}


	public Map<String, String> configPropertiesFalse() {
		Map<String, String> propertyValues = new HashMap<String,String>();
		propertyValues.put(	"admin.email.from.address","test@liferay.com");
		propertyValues.put(	"admin.email.from.name","Test Test");
		propertyValues.put(	"company.default.name","shlUpgrade");
		propertyValues.put(	"default.admin.first.name","Test");
		propertyValues.put(	"default.admin.last.name","Test");
		propertyValues.put(	"jdbc.default.driverClassName","com.mysql.jdbc.Driver");
		propertyValues.put(	"jdbc.default.password","test");
		propertyValues.put(	"jdbc.default.url","jdbc:mysql://localhost:3307/locatievinderdxp?characterEncoding=UTF-8&dontTrackOpenResources=true&holdResultsOpenOverStatementClose=true&useFastDateParsing=false&useUnicode=true&useSSL=false");
		propertyValues.put(	"jdbc.default.username","root");
		propertyValues.put(	"liferay.home","C:/liferay-dxp-digital-enterprise-7.0-ga1");
		propertyValues.put(	"setup.wizard.enabled","false");
		return propertyValues;
	}

	public Map<String, String> configProperties() {
		Map<String, String> propertyValues = new HashMap<String, String>();
		propertyValues.put(	"admin.email.from.address","test@liferay.com");
		propertyValues.put(	"admin.email.from.name","Test Test");
		return propertyValues;
	}

}
