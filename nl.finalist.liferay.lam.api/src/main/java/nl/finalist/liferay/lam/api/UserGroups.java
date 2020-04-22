
package nl.finalist.liferay.lam.api;

import java.util.Map;

/**
 * Interface to implement the addition of user groups.
 *
 */
public interface UserGroups {

    void addUserGroup(String[] webIds, String name, String description, Map<String, String> customFields);

}
