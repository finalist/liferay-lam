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
	
	/**
	 * Sets the name of the default company
	 * @param portalName The name that has to be set
	 */
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
	/**
	 * Sets the email domain for the default company
	 * @param emailDomain The email domain that has to be set
	 */
	@Override
	public void setEmailDomain(String emailDomain) {
		company = getDefaultCompany();
		company.setMx(emailDomain);
		companyService.updateCompany(company);
	}
	/**
	 * Sets the virtual host name for the default company
	 * @param virtualHostName The virtual host name that has to be set
	 */
	@Override
	public void setVirtualHostName(String virtualHostName) {
		company = getDefaultCompany();
		company.setVirtualHostname(virtualHostName);
		companyService.updateCompany(company);
	}
	/**
	 * Sets the instance home URL for the default company
	 * @param homeURL The home URL that has to be set
	 */
	@Override
	public void setHomeURL(String homeURL) {
		company = getDefaultCompany();
		company.setHomeURL(homeURL);
		companyService.updateCompany(company);
	}
	/**
	 * Sets the default landing page for the default company
	 * @param defaultLandingPage The default landing page that has to be set
	 */
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
	/**
	 * Sets the default logout page for the default company
	 * @param defaultLogoutPage The default logout page that has to be set
	 */
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
	/**
	 * Sets whether a terms of use is required for the default company
	 * @param termsOfUseRequired The boolean representing whether the terms of use are required
	 */
	@Override
	public void setTermsOfUseRequired(boolean termsOfUseRequired) {
		company = getDefaultCompany();
		UnicodeProperties properties = new UnicodeProperties();
		properties.setProperty("terms.of.use.required", String.valueOf(termsOfUseRequired));
		try {
			companyService.updatePreferences(company.getCompanyId(), properties);
		} catch (PortalException e) {
			LOG.error(e);
		}
	}
	/**
	 * Sets the sender name of email notifications from the default company
	 * @param emailNotificationName The name of the sender of email notifications
	 */
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
	/**
	 * Sets the sender email address of email notifications from the default company
	 * @param emailAddress The email address of the sender of email notifications
	 */
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
	/**
	 * Sets the available languages for the default company
	 * @param languages A String array of the languages that have to be set
	 */
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
	/**
	 * Sets the default language of the default company
	 * @param languageId The id of the language that has to be set
	 */
	@Override
	public void setDefaultLanguage(String languageId) {
		company = getDefaultCompany();
		try {
			companyService.updateDisplay(company.getCompanyId(), languageId, company.getTimeZone().getID());
		} catch (PortalException e) {
			LOG.error(e);
		}
	}
	/**
	 * Sets the time zone of the default company
	 * @param timezoneId The id of the time zone that has to be set
	 */
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
