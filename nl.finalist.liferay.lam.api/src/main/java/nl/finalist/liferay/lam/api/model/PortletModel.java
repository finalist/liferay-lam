package nl.finalist.liferay.lam.api.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class PortletModel {
	private String id;
	private Map<String, String> preferences;
	
	public PortletModel(LinkedHashMap<String, Object> map) {
		this.id = (String)map.get("id");
		if (map.get("preferences") != null) {
			this.preferences = (Map<String, String>) map.get("preferences");
		}
	}
	
	public PortletModel(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Map<String, String> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, String> preferences) {
		this.preferences = preferences;
	}
}
