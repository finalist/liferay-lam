
package nl.finalist.liferay.lam.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Interface to implement the addition of Role and the respective permission.
 *
 */
public interface RoleAndPermissions {
	boolean addCustomRoleAndPermission(String role, TypeOfRole typeOfRole, Map<Locale, String> title,
			Map<Locale, String> description, Map<String, List<String>> permissions);

}
