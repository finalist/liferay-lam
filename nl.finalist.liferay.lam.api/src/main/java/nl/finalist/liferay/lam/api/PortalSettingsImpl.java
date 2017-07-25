package nl.finalist.liferay.lam.api;

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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = PortalSettings.class)
public class PortalSettingsImpl implements PortalSettings {

    @Reference
    private CompanyLocalService companyService;
    @Reference
    private AccountLocalService accountService;
    @Reference
    private UserLocalService userService;

    private Company company;

    private static final Log LOG = LogFactoryUtil.getLog(PortalSettingsImpl.class);

    @Override
    public void setPortalName(String portalName) {
        company = getDefaultCompany();
        Account account = null;
        try {
            account = company.getAccount();
            account.setName(portalName);
            accountService.updateAccount(account);
        } catch (PortalException e) {
            LOG.error("Error while setting portalName", e);
        }
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
        try {
            companyService.updateCompany(company.getCompanyId(), virtualHostName, company.getMx(),
                            company.getMaxUsers(), company.isActive());
        } catch (PortalException e) {
            LOG.error("Error while setting virtualHostName", e);
        }
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
            LOG.error("Error while setting default landing page", e);
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
            LOG.error("Error while setting the default logout page", e);
        }
    }

    @Override
    public void setTermsOfUseRequired(boolean termsOfUseRequired) {
        company = getDefaultCompany();
        UnicodeProperties properties = new UnicodeProperties();
        properties.setProperty("terms.of.use.required", String.valueOf(termsOfUseRequired));
        try {
            companyService.updatePreferences(company.getCompanyId(), properties);
        } catch (PortalException e) {
            LOG.error("Error while setting whether the terms of use is required", e);
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
            LOG.error("Error while setting name of email notification sender", e);
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
            LOG.error("Error while setting address of email notification sender", e);
        }
    }

    @Override
    public void setDefaultLanguage(String languageId) {
        company = getDefaultCompany();
        try {
            companyService.updateDisplay(company.getCompanyId(), languageId, company.getTimeZone().getID());
        } catch (PortalException e) {
            LOG.error("Error while setting defualt language", e);
        }
    }

    @Override
    public void setAvailableLanguages(String languageIds) {
        company = getDefaultCompany();
        UnicodeProperties properties = new UnicodeProperties();
        properties.setProperty("locales", languageIds);
        try {
            companyService.updatePreferences(company.getCompanyId(), properties);
        } catch (PortalException e) {
            LOG.error("Error while setting available languages", e);
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
            LOG.error("Error while setting portal timezone", e);
        }
    }

    private Company getDefaultCompany() {
        String webId = PropsUtil.get("company.default.web.id");
        try {
            company = companyService.getCompanyByWebId(webId);
        } catch (PortalException e) {
            LOG.error("Error while retrieving default company", e);
        }
        return company;
    }
}
