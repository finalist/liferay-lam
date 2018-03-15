package nl.finalist.liferay.lam.api.model;

import java.util.LinkedHashMap;
import java.util.List;

public class Column {
	private List<String> portletIds;
	private int id;
	
	
	public Column(LinkedHashMap<String, Object> map) {
		this.portletIds = (List<String>)map.get("portletIds");
		this.setId((Integer)map.get("id"));
	}
	
	public Column(int id, List<String> portletIds) {
		this.id = id;
		this.portletIds = portletIds;
	}
	
	public List<String> getPortletIds() {
		return portletIds;
	}
	public void setPortletIds(List<String> portletIds) {
		this.portletIds = portletIds;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
