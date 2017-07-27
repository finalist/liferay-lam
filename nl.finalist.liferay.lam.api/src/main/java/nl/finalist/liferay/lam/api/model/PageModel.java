package nl.finalist.liferay.lam.api.model;

import java.util.Locale;
import java.util.Map;

public class PageModel {
	private boolean privatePage;
	private Map<Locale, String> nameMap;
	private Map<Locale, String> titleMap;
	private Map<Locale, String> descriptionMap;
	private Map<Locale, String> friendlyUrlMap;
	private String typeSettings;
	
	public PageModel(boolean privatePage, Map<Locale, String> nameMap, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, Map<Locale, String> friendlyUrlMap, String typeSettings) {
		super();
		this.privatePage = privatePage;
		this.nameMap = nameMap;
		this.titleMap = titleMap;
		this.descriptionMap = descriptionMap;
		this.friendlyUrlMap = friendlyUrlMap;
		this.typeSettings = typeSettings;
	}
	
	public boolean isPrivatePage() {
		return privatePage;
	}
	public void setPrivatePage(boolean privatePage) {
		this.privatePage = privatePage;
	}
	public Map<Locale, String> getNameMap() {
		return nameMap;
	}
	public void setNameMap(Map<Locale, String> nameMap) {
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
	public Map<Locale, String> getFriendlyUrlMap() {
		return friendlyUrlMap;
	}
	public void setFriendlyUrlMap(Map<Locale, String> friendlyUrlMap) {
		this.friendlyUrlMap = friendlyUrlMap;
	}
	public String getTypeSettings() {
		return typeSettings;
	}
	public void setTypeSettings(String typeSettings) {
		this.typeSettings = typeSettings;
	}
}
