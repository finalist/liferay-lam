package nl.finalist.liferay.lam.api.model;

import java.util.ArrayList;
import java.util.List;

public class Column {
	private List<Portlet> portlets = new ArrayList<>();
	
	public Column() {
	}
	
	public Column(List<Portlet> portlets) {
		this.portlets = portlets;
	}
	
	public List<Portlet> getPortlets() {
		return portlets;
	}
	public void setPortlets(List<Portlet> portlets) {
		this.portlets = portlets;
	}
	
	/**
	 * Add a portlet to the list
	 * @param portlet
	 */
	public void addPortlet(Portlet portlet) {
		this.portlets.add(portlet);
	}
	
	/**
	 * Return a list of all the ids contained in the list of portlets
	 * @return
	 */
	public List<String> getPortletIds() {
		List<String> ids = new ArrayList<>();
		for (Portlet portlet : portlets) {
			ids.add(portlet.getId());
		}
		return ids;
	}
}
