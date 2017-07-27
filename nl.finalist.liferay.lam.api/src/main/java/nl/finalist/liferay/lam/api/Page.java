package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;

import nl.finalist.liferay.lam.api.model.PageModel;

public interface Page {

	/**
	 * Add a a page (in Liferay called layout) to a site (in Liferay called group)
	 * 
	 * @param userId Id of the user adding the page
	 * @param groupId Id of the site the page belongs to
	 * @param page Contains all the information of the page
	 * @throws PortalException
	 */
	void addPage(long userId, long groupId, PageModel page) throws PortalException;

}
