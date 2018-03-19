import nl.finalist.liferay.lam.dslglue.Templates

create.page(
	siteKey: "Fictional Bank",
	privatePage: false,
	nameMap:["nl_NL": "Pagina met kolommen", "en_US": "Page with columns"],
    titleMap: ["nl_NL": "Pagina met kolommen"],
    friendlyUrlMap: ["nl_NL": "/kolommen"],
) {
	column(id:1, portletIds:["com_liferay_login_web_portlet_LoginPortlet"])
	column(id:2, portletIds:["com_liferay_site_navigation_language_web_portlet_SiteNavigationLanguagePortlet", "com_liferay_social_activities_web_portlet_SocialActivitiesPortlet"])
}