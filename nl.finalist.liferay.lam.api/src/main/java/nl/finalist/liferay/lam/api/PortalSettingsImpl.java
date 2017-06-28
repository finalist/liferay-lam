package nl.finalist.liferay.lam.api;

import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Account;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.AccountLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

public class PortalSettingsImpl implements PortalSettings {

	@Reference
	private CompanyLocalService companyService;
	@Reference
	private AccountLocalService accountService;
	@Reference
	private UserLocalService userService;
	
	private Company company;
	
	private static final Log LOG = LogFactoryUtil.getLog(CustomFieldsImpl.class);

	@Override
	public void setPortalName(String portalName) {
		company = getDefaultCompany();
		Account account = null;
		try {
			account = company.getAccount();
		} catch (PortalException e) {
			LOG.error(e);
		}
		account.setName(portalName);
		accountService.updateAccount(account);
	}

	@Override
	public void setEmailDomain(String emailDomain) {
		company = getDefaultCompany();
		company.setMx(emailDomain);
		companyService.updateCompany(company);
	}

	@Override
	public void setVirtualHostName(String virtualHostName) {
		company = getDefaultCompany();
		company.setVirtualHostname(virtualHostName);
		companyService.updateCompany(company);
	}

	@Override
	public void setHomeURL(String homeURL) {
		company = getDefaultCompany();
		company.setHomeURL(homeURL);
		companyService.updateCompany(company);
	}

	@Override
	public void setDefaultLandingPage(String defaultLandingPage) {
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("default.landing.page.path", defaultLandingPage);
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void setDefaultLogoutPage(String defaultLogoutPage) {
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("default.logout.page.path", defaultLogoutPage);
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void setTermsOfUseRequired(boolean termsOfUserequired) {
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("terms.of.use.required", String.valueOf(termsOfUserequired));
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void setEmailNotificationName(String emailNotificationName) {
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("admin.email.from.name", emailNotificationName);
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void setEmailNotificationAddress(String emailAddress) {
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("admin.email.from.address", emailAddress);
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void setAvailableLanguages(String[] languages) {
		String joinedLanguages = String.join(",", languages);
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("locales", joinedLanguages);
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}
	
	@Override
	public void setDefaultLanguage(String languageId) {
		company = getDefaultCompany();
		try {
			companyService.updateDisplay(company.getCompanyId(), languageId, company.getTimeZone().getID());
		} catch (PortalException e) {
			LOG.error(e);
		}
	}
	
	@Override
	public void setTimeZone(String timezoneId) {
		company = getDefaultCompany();
		User user = null;
		try {
			user = userService.getDefaultUser(company.getCompanyId());
			companyService.updateDisplay(company.getCompanyId(), user.getLanguageId(), timezoneId);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	private Company getDefaultCompany() {
		String webId = PropsUtil.get("company.default.web.id");
		try {
			company = companyService.getCompanyByWebId(webId);
		} catch (PortalException e) {
			LOG.error(e);
		}
		return company;
	}
}
