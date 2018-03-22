package nl.finalist.liferay.lam.api;

import java.util.Map;

public interface User {

    /**
     * Creates a new user
     * @param screenName
     * @param emailAddress
     * @param firstName
     * @param lastName
     * @param roles
     * @param groups
     * @param userGroups
     * @param customFields
     */
    void createUser(String screenName, String emailAddress, String firstName, String lastName,
                    String[] roles, String[] groups, String[] userGroups, Map<String, String> customFields);
    /**
     * Updates a user
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
    void updateUser(String screenName, String newScreenName, String emailAddress,
                    String firstName, String lastName, String[] roles, String[] groups, String[] userGroups, 
                    Map<String, String> customFields);
    /**
     * Deletes a user based on a screen name
     * @param screenName    The screen name of the user
     */
    void deleteUser(String screenName);
}
