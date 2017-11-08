package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;

import nl.finalist.liferay.lam.api.model.PageModel;

public interface Page {

	/**
	 * Add a page (in Liferay called layout) of type 'portlet' to a site (in Liferay called
	 * group)
	 * 
	 * @param userId
	 *            Id of the user adding the page
	 * @param groupId
	 *            Id of the site the page belongs to
	 * @param page
	 *            Contains all the information of the page
	 * @throws PortalException
	 */
	void addPage(long userId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException;

	/**
	 * Update a page (in Liferay called layout) of type 'portlet' to a site (in Liferay called
	 * group)
	 *
	 * @param layoutId
	 *            Id of the page to be update
	 * @param groupId
	 *            Id of the site the page belongs to
	 * @param page
	 *            Contains all the information of the page
	 * @throws PortalException
	 */
	void updatePage(long layoutId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException;

	/**
	 * fetch a page (in Liferay called layout) to a site (in Liferay called
	 * group)
	 *
	 * @param privateLayout
	 *            whether the layout is private to the group
	 * @param groupId
	 *            Id of the site the page belongs to
	 * @param friendlyURL
	 *            url of the page
	 * @throws PortalException
	 */
	Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL);
}
