package nl.finalist.liferay.lam.api.model;

import java.util.ArrayList;
import java.util.List;

public class ColumnModel {
	private List<PortletModel> portlets = new ArrayList<>();
	private List<ContentModel> content = new ArrayList<>();
	
	public ColumnModel() {
	}
	
	public ColumnModel(List<PortletModel> portlets) {
		this.portlets = portlets;
	}
	
	public List<PortletModel> getPortlets() {
		return portlets;
	}
	public void setPortlets(List<PortletModel> portlets) {
		this.portlets = portlets;
	}
	
	public List<ContentModel> getContent() {
		return content;
	}
	public void setContent(List<ContentModel> content) {
		this.content = content;
	}
	/**
	 * Add a portlet to the list of portlets
	 * @param portlet
	 */
	public void addPortlet(PortletModel portlet) {
		this.portlets.add(portlet);
	}
	
	/**
	 * Add content to the list of content
	 * @param content
	 */
	public void addContent(ContentModel content) {
		this.content.add(content);
	}
	
	/**
	 * Return a list of all the ids contained in the list of portlets
	 * @return
	 */
	public List<String> getPortletIds() {
		List<String> ids = new ArrayList<>();
		for (PortletModel portlet : portlets) {
			ids.add(portlet.getId());
		}
		return ids;
	}
}
