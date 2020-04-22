package nl.finalist.liferay.lam.api;

import java.util.Map;

public interface User {

    /**
     * Creates a new user
     * 
     * @param webIds
     * @param screenName
     * @param emailAddress
     * @param firstName
     * @param lastName
     * @param roles
     * @param groups
     * @param userGroups
     * @param customFields
     */
    void createUser(String[] webIds, String screenName, String emailAddress, String firstName, String lastName, String[] roles, String[] groups,
                    String[] userGroups, Map<String, String> customFields);

    /**
     * Updates a user
     * 
     * @param webIds
     * @param screenName
     * @param newScreenName
     * @param emailAddress
     * @param firstName
     * @param lastName
     * @param roles
     * @param groups
     * @param userGroups
     * @param customFields
     */
    void updateUser(String[] webIds, String screenName, String newScreenName, String emailAddress, String firstName, String lastName, String[] roles,
                    String[] groups, String[] userGroups, Map<String, String> customFields);

    /**
     * Deletes a user based on a screen name
     * 
     * @param webIds
     *            WebIds of the user's company
     * @param screenName
     *            The screen name of the user
     */
    void deleteUser(String[] webIds, String screenName);
}
