package nl.finalist.liferay.lam.api;

public interface PortalSettings {

    /**
     * Sets the name of the default company
     *
     * @param portalName
     *            The name that has to be set
     */
    public void setPortalName(String portalName);

    /**
     * Sets the email domain for the default company
     *
     * @param emailDomain
     *            The email domain that has to be set
     */
    public void setEmailDomain(String emailDomain);

    /**
     * Sets the virtual host name for the default company
     *
     * @param virtualHostName
     *            The virtual host name that has to be set
     */
    public void setVirtualHostName(String virtualHostName);

    /**
     * Sets the instance home URL for the default company
     *
     * @param homeURL
     *            The home URL that has to be set
     */
    public void setHomeURL(String homeURL);

    /**
     * Sets the default landing page for the default company
     *
     * @param defaultLandingPage
     *            The default landing page that has to be set
     */
    public void setDefaultLandingPage(String defaultLandingPage);

    /**
     * Sets the default logout page for the default company
     *
     * @param defaultLogoutPage
     *            The default logout page that has to be set
     */
    public void setDefaultLogoutPage(String defaultLogoutPage);

    /**
     * Sets whether a terms of use is required for the default company
     *
     * @param termsOfUseRequired
     *            The boolean representing whether the terms of use are required
     */
    public void setTermsOfUseRequired(boolean termsOfUseRequired);

    /**
     * Sets the sender name of email notifications from the default company
     *
     * @param emailNotificationName
     *            The name of the sender of email notifications
     */
    public void setEmailNotificationName(String emailNotificationName);

    /**
     * Sets the sender email address of email notifications from the default
     * company
     *
     * @param emailAddress
     *            The email address of the sender of email notifications
     */
    public void setEmailNotificationAddress(String emailAddress);

    /**
     * Sets the default language of the default company
     *
     * @param languageId
     *            The id of the language that has to be set
     */
    public void setDefaultLanguage(String languageId);

    /**
     * Sets the available languages for the default company
     *
     * @param languages
     *            A String array of the languages that have to be set
     */
    public void setAvailableLanguages(String languageIds);

    /**
     * Sets the time zone of the default company
     *
     * @param timezoneId
     *            The id of the time zone that has to be set
     */
    public void setTimeZone(String timezoneId);
}
