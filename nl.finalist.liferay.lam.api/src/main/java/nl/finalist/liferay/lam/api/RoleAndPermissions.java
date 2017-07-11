
package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

/**
 * Interface to implement the addition of Role and the respective permission.
 *
 */
public interface RoleAndPermissions {
	boolean addCustomRoleAndPermission(String role, Long userId, TypeOfRole typeOfRole, Map<Locale, String> title,
			Map<Locale, String> description, Long companyId, String[] actionId, String entityName);

}