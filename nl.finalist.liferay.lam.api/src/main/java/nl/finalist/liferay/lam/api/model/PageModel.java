package nl.finalist.liferay.lam.api.model;

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

	public void setSiteKey(String siteKey) {
		this.siteKey = siteKey;
	}

	public String getSiteKey() {
		return siteKey;
	}

	@Override
	public String toString() {
		return "PageModel [privatePage=" + privatePage + ", nameMap=" + nameMap + ", titleMap=" + titleMap
				+ ", descriptionMap=" + descriptionMap + ", friendlyUrlMap=" + friendlyUrlMap + ", typeSettings="
				+ typeSettings + ", customFields=" + customFields + ", parentUrl="+parentUrl+", type ="
				+ type + "]";
	}

}
