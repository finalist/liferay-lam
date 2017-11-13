package nl.finalist.liferay.lam.api;

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
     */
    void createUser(String screenName, String emailAddress, String firstName, String lastName,
                    String[] roles, String[] groups, String[] userGroups);
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
     */
    void updateUser(String screenName, String newScreenName, String emailAddress,
                    String firstName, String lastName, String[] roles, String[] groups, String[] userGroups);
    /**
     * Deletes a user based on a screen name
     * @param screenName    The screen name of the user
     */
    void deleteUser(String screenName);
}
