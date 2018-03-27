package nl.finalist.liferay.lam.dslglue.model

import nl.finalist.liferay.lam.api.model.PageModel

class SiteModel {
	Map<String, String> nameMap
	Map<String, String> descriptionMap
	String friendlyURL
	String siteKey
	Map<String, String> customFields
	List<PageModel> pages = new ArrayList<>();
    Boolean stagingEnabled;
    
	void addPage(PageModel page) {
        pages += page
    }
	
	
}