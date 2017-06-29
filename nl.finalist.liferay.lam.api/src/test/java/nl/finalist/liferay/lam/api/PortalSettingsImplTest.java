package nl.finalist.liferay.lam.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropsUtil.class, PortalSettingsImpl.class })
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

	@InjectMocks
	private PortalSettingsImpl portalSettingsImplementation;

	@Before
	public void setUp() {
		portalSettingsImplementation = new PortalSettingsImpl();
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");

		initMocks(this);
	}

	@Test
	public void testSetPortalName() throws PortalException {
		String testPortalName = "testName";
		when(mockCompany.getAccount()).thenReturn(mockAccount);
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);

		portalSettingsImplementation.setPortalName(testPortalName);

		verify(mockAccount).setName(testPortalName);
		verify(accountService).updateAccount(mockAccount);
	}

	@Test
	public void testSetEmailDomain() throws PortalException {
		String testEmailDomain = "test.com";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);

		portalSettingsImplementation.setEmailDomain(testEmailDomain);

		verify(mockCompany).setMx(testEmailDomain);
		verify(companyService).updateCompany(mockCompany);
	}

	@Test
	public void testSetHomeURL() throws PortalException {
		String testHomeURL = "/test/home";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);

		portalSettingsImplementation.setHomeURL(testHomeURL);

		verify(mockCompany).setHomeURL(testHomeURL);
		verify(companyService).updateCompany(mockCompany);
	}

	@Test
	public void testSetVirtualHostName() throws PortalException {
		String testVirtualHostName = "testHostName";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);

		portalSettingsImplementation.setVirtualHostName(testVirtualHostName);

		verify(mockCompany).setVirtualHostname(testVirtualHostName);
		verify(companyService).updateCompany(mockCompany);
	}

	@Test
	public void testSetDefaultLandingPage() throws Exception {
		String testLandingPage = "/testLandingPage";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setDefaultLandingPage(testLandingPage);

		verify(mockProperties).setProperty("default.landing.page.path", testLandingPage);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetDefaultLogoutPage() throws Exception {
		String testLogoutPage = "/testLogoutPage";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setDefaultLogoutPage(testLogoutPage);

		verify(mockProperties).setProperty("default.logout.page.path", testLogoutPage);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetTermsOfUseRequired() throws Exception {
		boolean testTermsOfUseRequired = false;
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setTermsOfUseRequired(testTermsOfUseRequired);

		verify(mockProperties).setProperty("terms.of.use.required", String.valueOf(testTermsOfUseRequired));
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetEmailNotificationName() throws Exception {
		String testEmailName = "testEmailName";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setEmailNotificationName(testEmailName);

		verify(mockProperties).setProperty("admin.email.from.name", testEmailName);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetEmailNotificationAddress() throws Exception {
		String testEmailAdress = "test@test.com";
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setEmailNotificationAddress(testEmailAdress);

		verify(mockProperties).setProperty("admin.email.from.address", testEmailAdress);
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetAvailableLanguages() throws Exception {
		String[] testAvailableLanguages = { "TST_tst", "AB_ab" };
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockCompany.getCompanyId()).thenReturn(1L);
		whenNew(UnicodeProperties.class).withNoArguments().thenReturn(mockProperties);

		portalSettingsImplementation.setAvailableLanguages(testAvailableLanguages);

		verify(mockProperties).setProperty("locales", "TST_tst,AB_ab");
		verify(companyService).updatePreferences(1L, mockProperties);
	}

	@Test
	public void testSetTimezoneId() throws PortalException {
		String testTimezoneId = "TST";
		when(mockCompany.getCompanyId()).thenReturn(1L);
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
		when(mockDefaultUser.getLanguageId()).thenReturn("TST_tst");
		when(userService.getDefaultUser(1L)).thenReturn(mockDefaultUser);

		portalSettingsImplementation.setTimeZone(testTimezoneId);

		verify(companyService).updateDisplay(1L, "TST_tst", testTimezoneId);
	}

	@Test
	public void testSetDefaultLanguage() throws PortalException {
		String testLanguageId = "TST_tst";
		when(mockCompany.getCompanyId()).thenReturn(1L);
		when(mockCompany.getTimeZone()).thenReturn(mockTimezone);
		when(mockTimezone.getID()).thenReturn("TST");
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);

		portalSettingsImplementation.setDefaultLanguage(testLanguageId);

		verify(companyService).updateDisplay(1L, testLanguageId, "TST");
	}

}
