package nl.finalist.liferay.lam.api;

import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Account;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.AccountLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropsUtil.class, PortalSettingsImpl.class})
public class PortalSettingsImplTest {
	@Mock
	private CompanyLocalService companyService;
	@Mock
	private AccountLocalService accountService;
	@Mock
	private UserLocalService userService;

	@Mock
	private Company mockCompany;
	@Mock
	private Account mockAccount;
	@Mock
	private User mockDefaultUser;
	@Mock
	private TimeZone mockTimezone;
	@Mock
	private UnicodeProperties mockProperties;
	@Mock
	private DefaultValue defaultValue;
	@InjectMocks
	private PortalSettingsImpl portalSettingsImplementation;

	@Before
	public void setUp() {
		portalSettingsImplementation = new PortalSettingsImpl();
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.mockStatic(DefaultValueImpl.class);
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);
		initMocks(this);
	}

	@Test
	public void testSetPortalName() throws PortalException {
		String testPortalName = "testName";
		when(mockCompany.getAccount()).thenReturn(mockAccount);
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		portalSettingsImplementation.setPortalName(testPortalName);

		verify(mockAccount).setName(testPortalName);
		verify(accountService).updateAccount(mockAccount);
	}

	@Test
	public void testSetEmailDomain() throws PortalException {
		String testEmailDomain = "test.com";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		portalSettingsImplementation.setEmailDomain(testEmailDomain);

		verify(mockCompany).setMx(testEmailDomain);
		verify(companyService).updateCompany(mockCompany);
	}

	@Test
	public void testSetHomeURL() throws PortalException {
		String testHomeURL = "/test/home";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		portalSettingsImplementation.setHomeURL(testHomeURL);

		verify(mockCompany).setHomeURL(testHomeURL);
		verify(companyService).updateCompany(mockCompany);
	}

	@Test
	public void testSetVirtualHostName() throws PortalException {
		String testVirtualHostName = "testHostName";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getMaxUsers()).thenReturn(10);
		when(mockCompany.getMx()).thenReturn("TESTMX");
		when(mockCompany.getCompanyId()).thenReturn(1L);
		when(mockCompany.isActive()).thenReturn(true);

		portalSettingsImplementation.setVirtualHostName(testVirtualHostName);

		verify(companyService).updateCompany(1L, testVirtualHostName, "TESTMX", 10, true);
	}

	@Test
	public void testSetDefaultLandingPage() throws Exception {
		String testLandingPage = "/testLandingPage";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setDefaultLandingPage(testLandingPage);

		verify(mockProperties).setProperty("default.landing.page.path", testLandingPage);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetDefaultLogoutPage() throws Exception {
		String testLogoutPage = "/testLogoutPage";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setDefaultLogoutPage(testLogoutPage);

		verify(mockProperties).setProperty("default.logout.page.path", testLogoutPage);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetTermsOfUseRequired() throws Exception {
		boolean testTermsOfUseRequired = false;
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setTermsOfUseRequired(testTermsOfUseRequired);

		verify(mockProperties).setProperty("terms.of.use.required", String.valueOf(testTermsOfUseRequired));
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetEmailNotificationName() throws Exception {
		String testEmailName = "testEmailName";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setEmailNotificationName(testEmailName);

		verify(mockProperties).setProperty("admin.email.from.name", testEmailName);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetEmailNotificationAddress() throws Exception {
		String testEmailAdress = "test@test.com";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setEmailNotificationAddress(testEmailAdress);

		verify(mockProperties).setProperty("admin.email.from.address", testEmailAdress);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetAvailableLanguages() throws Exception {
		String testAvailableLanguages = "tst_TST,abc_ABC";
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setAvailableLanguages(testAvailableLanguages);

		verify(mockProperties).setProperty("locales", "tst_TST,abc_ABC");
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetTimezoneId() throws PortalException {
		String testTimezoneId = "TST";
		when(mockCompany.getCompanyId()).thenReturn(1L);
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		when(mockDefaultUser.getLanguageId()).thenReturn("tst_TST");
		when(userService.getDefaultUser(1L)).thenReturn(mockDefaultUser);

		portalSettingsImplementation.setTimeZone(testTimezoneId);

		verify(companyService).updateDisplay(1L, "tst_TST", testTimezoneId);
	}

	@Test
	public void testSetDefaultLanguage() throws PortalException {
		String testLanguageId = "tst_TST";
		when(mockCompany.getCompanyId()).thenReturn(1L);
		when(mockCompany.getTimeZone()).thenReturn(mockTimezone);
		when(mockTimezone.getID()).thenReturn("TST");
		when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);

		portalSettingsImplementation.setDefaultLanguage(testLanguageId);

		verify(companyService).updateDisplay(1L, testLanguageId, "TST");
	}

}
