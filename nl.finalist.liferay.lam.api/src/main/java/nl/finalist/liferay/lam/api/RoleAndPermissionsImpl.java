package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;

/**
 * Add the Role and the respective Permissions to the role.
 */
@Component(immediate = true, service = RoleAndPermissions.class)
public class RoleAndPermissionsImpl implements RoleAndPermissions {
	private static final Log LOG = LogFactoryUtil.getLog(RoleAndPermissionsImpl.class);

	/**
	 * Add a custom Role.
	 * 
	 * @param roleName
	 *            name of the role to be added.
	 * @param userId
	 *            Id of the User who is performing the operation.
	 * @param typeOfRole
	 *            type of the role.
	 * @param titles
	 *            role titles which is Map with the Locale.
	 * @param description
	 *            role descriptions which is Map with the Locale.
	 * @param companyId
	 *            companyId for which Role should be added.
	 * @param actionIds
	 *            permissions to be added for the Role.
	 * @param entityName
	 *            entity for which permissions should be added.
	 */
	@Override
	public boolean addCustomRoleAndPermission(String roleName, Long userId, TypeOfRole typeOfRole,
			Map<Locale, String> titles, Map<Locale, String> descriptions, Long companyId, String[] actionIds,
			String entityName) {
		LOG.info(String.format("Adding the Role with %s with userId= %d", roleName, userId));
		try {
			Role role = RoleLocalServiceUtil.addRole(userId, null, 0L, roleName, titles, descriptions,
					typeOfRole.getValue(), null, null);
			LOG.info(String.format("Added the role", role.getCreateDate()));
			LOG.info("Strting to set the permission");
			if (actionIds != null) {
				for (String actionId : actionIds)
					addPermissions(role.getRoleId(), companyId, actionId, entityName);
			} else {
				LOG.info("Permissions to the roles where not found. You should add the permissions later.");
			}
			return true;
		} catch (PortalException e) {
			LOG.info("For some reason Role was not added. Please check and confirm your Role credentials");
			return false;
		}
	}

	/**
	 * Add a Permissions to the custom Role.
	 * 
	 * @param roleId
	 *            name of the role to be added.
	 * @param companyId
	 *            company Id for which role should be added.
	 * @param actionId
	 *            permission which should be added to the role.
	 * @param entityName
	 *            entity for which permissions should be added.
	 */
	private boolean addPermissions(Long roleId, Long companyId, String actionId, String entityName) {
		try {
			LOG.info(String.format("Starting to add the permision %s to entity %s", actionId, entityName));

			ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, entityName,
					ResourceConstants.SCOPE_COMPANY, String.valueOf(companyId), roleId, actionId);

			LOG.info(String.format("Added the permision %s to entity %s", actionId, entityName));
			return true;
		} catch (PortalException e) {
			LOG.info(String.format("Adding permission %s to entity %s was not successful", actionId, entityName));
			return false;
		}
	}

}
