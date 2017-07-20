
package nl.finalist.liferay.lam.api;

import java.util.Map;

/**
 * Interface to implement the addition of user groups.
 *
 */
public interface UserGroups {
	boolean addUserGroup(String name, String description, Map<String, String> customFields);

}
