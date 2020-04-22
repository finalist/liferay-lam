package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;

import nl.finalist.liferay.lam.api.model.PageModel;

public interface Page {

    /**
     * Add a page (in Liferay called layout) of type 'portlet' to a site (in
     * Liferay called group) exists in given instances (in Liferay called
     * company)
     *
     * @param webIds
     *            Ids of the companies the page belongs to
     * @param siteKey
     *            Key of the site the page belongs to
     * @param page
     *            Contains all the information of the page
     */
    void addPage(String[] webIds, String siteKey, PageModel page);

    /**
     * Update a page (in Liferay called layout) of type 'portlet' to a site (in
     * Liferay called group)
     * 
     * Mind you, this method does not update the typesettings at the moment!
     *
     * @param layout
     *            The existing page
     * @param groupId
     *            Id of the site the page belongs to
     * @param page
     *            Contains all the information of the page
     * @throws PortalException
     */
    void updatePage(Layout layout, long groupId, long userId, PageModel page) throws PortalException;

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
