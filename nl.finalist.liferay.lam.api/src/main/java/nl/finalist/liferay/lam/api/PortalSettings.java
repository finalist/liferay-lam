package nl.finalist.liferay.lam.api;

public interface PortalSettings {
    public void setPortalName(String portalName);

    public void setEmailDomain(String emailDomain);

    public void setVirtualHostName(String virtualHostName);

    public void setHomeURL(String homeURL);

    public void setDefaultLandingPage(String defaultLandingPage);

    public void setDefaultLogoutPage(String defaultLogoutPage);

    public void setTermsOfUseRequired(boolean termsOfUseRequired);

    public void setEmailNotificationName(String emailNotificationName);

    public void setEmailNotificationAddress(String emailAddress);

    public void setDefaultLanguage(String languageId);

    public void setAvailableLanguages(String languageIds);

    public void setTimeZone(String timezoneId);
}
