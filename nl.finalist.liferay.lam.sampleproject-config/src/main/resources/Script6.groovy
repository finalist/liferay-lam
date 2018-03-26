import nl.finalist.liferay.lam.dslglue.Templates

createOrUpdate.page(
	siteKey: "Fictional Bank",
	privatePage: false,
	nameMap:["nl_NL": "Pagina met kolommen", "en_US": "Page with columns"],
    titleMap: ["nl_NL": "Pagina met kolommen"],
    friendlyUrlMap: ["nl_NL": "/kolommen", "en_US": "/columns"],
) {
	column() {
		portlet(
			id:"com_liferay_login_web_portlet_LoginPortlet", 
			preferences:["portletSetupPortletDecoratorId": "decorate"]
		)
	}
	column() {
		portlet(
			id:"com_liferay_site_navigation_language_web_portlet_SiteNavigationLanguagePortlet", 
			preferences:["portletSetupPortletDecoratorId": "decorate"]
		)
		portlet(
			id:"com_liferay_social_activities_web_portlet_SocialActivitiesPortlet"
		)
		content(
			siteKey:"Fictional Bank",
			articleId:"NEWS1"
		)
	}
}
