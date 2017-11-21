package nl.finalist.liferay.lam.api.model;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class PageModel {

	private String siteKey;
	private boolean privatePage;
	private Map<String, String> nameMap;
	private Map<Locale, String> titleMap;
	private Map<Locale, String> descriptionMap;
	private Map<String, String> friendlyUrlMap;
	private Map<String, String> customFields;
	private String typeSettings;
	private String parentUrl;
	private String type;
	private boolean hiddenPage;
	private String linkedPageUrl;
	
	public PageModel(LinkedHashMap<String, Object> map) {
		this((String)map.get("siteKey"), (Boolean)map.get("privatePage"), (Map)map.get("nameMap"), (Map)map.get("titleMap"),
				(Map)map.get("descriptionMap"), (Map)map.get("friendlyUrlMap"), (String)map.get("typeSettings"), 
				(Map)map.get("customFields"), (String)map.get("parentUrl"), (String)map.get("type"), (String)map.get("linkedPageUrl"));
	}
    public PageModel(String siteKey, boolean privatePage, Map<String, String> nameMap, Map<Locale, String> titleMap,
            Map<Locale, String> descriptionMap, Map<String, String> friendlyUrlMap, String typeSettings, 
            Map<String, String> customFields, String parentUrl, String type, String linkedPageUrl) {
        this.privatePage = privatePage;
        this.nameMap = nameMap;
        this.titleMap = titleMap;
        this.descriptionMap = descriptionMap;
        this.friendlyUrlMap = friendlyUrlMap;
        this.typeSettings = typeSettings;
        this.customFields = customFields;
        this.parentUrl = parentUrl;
        this.setType(type);
        this.linkedPageUrl = linkedPageUrl;
    }

	public boolean isPrivatePage() {
		return privatePage;
	}
	public void setPrivatePage(boolean privatePage) {
		this.privatePage = privatePage;
	}
	public Map<String, String> getNameMap() {
		return nameMap;
	}
	public void setNameMap(Map<String, String> nameMap) {
		this.nameMap = nameMap;
	}
	public Map<Locale, String> getTitleMap() {
		return titleMap;
	}
	public void setTitleMap(Map<Locale, String> titleMap) {
		this.titleMap = titleMap;
	}
	public Map<Locale, String> getDescriptionMap() {
		return descriptionMap;
	}
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}
	public Map<String, String> getFriendlyUrlMap() {
		return friendlyUrlMap;
	}
	public void setFriendlyUrlMap(Map<String, String> friendlyUrlMap) {
		this.friendlyUrlMap = friendlyUrlMap;
	}
	public String getTypeSettings() {
		return typeSettings;
	}
	public void setTypeSettings(String typeSettings) {
		this.typeSettings = typeSettings;
	}

	public Map<String, String> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, String> customFields) {
		this.customFields = customFields;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public boolean isHiddenPage() {
		return hiddenPage;
	}

	public void setHiddenPage(boolean hiddenPage) {
		this.hiddenPage = hiddenPage;

	}

	public void setSiteKey(String siteKey) {
		this.siteKey = siteKey;
	}

	public String getSiteKey() {
		return siteKey;
	}

	public String getLinkedPageUrl() {
		return linkedPageUrl;
	}
	public void setLinkedPageUrl(String linkedPageUrl) {
		this.linkedPageUrl = linkedPageUrl;
	}
	@Override
	public String toString() {
		return "PageModel [privatePage=" + privatePage + ", nameMap=" + nameMap + ", titleMap=" + titleMap
				+ ", descriptionMap=" + descriptionMap + ", friendlyUrlMap=" + friendlyUrlMap + ", typeSettings="
				+ typeSettings + ", customFields=" + customFields + ", parentUrl="+parentUrl+", type ="
				+ type + ", hidden = "+ hiddenPage + "]";
	}

}
