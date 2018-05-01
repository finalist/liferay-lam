package nl.finalist.liferay.lam.api;

public interface ScopeHelper {
    /**
     * Method will return group id that is associated with the given site name. If a match
     * is not found the default group ID is returned.
     *
     * @param siteKey Group (Site) name
     * @return given group id or default group id
     */
    long getGroupIdByName(String siteKey);
}
