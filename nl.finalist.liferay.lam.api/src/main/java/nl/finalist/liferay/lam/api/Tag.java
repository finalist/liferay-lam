package nl.finalist.liferay.lam.api;

/**
 * Service for asset tags
 */
public interface Tag {

    /**
     * Create a tag. Tags are lowercase, not i18n'd.
     * 
     * @param webIds
     *            the webIds of Companies where this tag is to be created
     * @param name
     *            the name of the tag
     * @param siteFriendlyURL
     *            optional site url (friendly URL), starting with '/'. Leave
     *            null to create tag in the global site.
     */
    void createTag(String[] webIds, String name, String siteFriendlyURL);

    /**
     * Delete the given tag.
     * 
     * @param webIds
     *            the webIds of Companies from where this tag is to be deleted
     * @param name
     *            Name of the tag
     * @param siteFriendlyURL
     *            optional site url (friendly URL), starting with '/'. Leave
     *            null to target a tag in the global site.
     */
    void deleteTag(String[] webIds, String name, String siteFriendlyURL);
}
