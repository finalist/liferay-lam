package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * Add the Role and the respective Permissions to the role.
 */
@Component(immediate = true, service = RoleAndPermissions.class)
public class RoleAndPermissionsImpl implements RoleAndPermissions {
	private static final Log LOG = LogFactoryUtil.getLog(RoleAndPermissionsImpl.class);
	@Reference
	private RoleLocalService roleLocalService;
	@Reference
	private ResourcePermissionLocalService resourcePermissionLocalService;

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
	 *            role titles which is a map of titles by Locale.
	 * @param description
	 *            role descriptions which is a map of descriptions by Locale.
	 * @param actionIds
	 *            permissions to be added for the Role.
	 * @param entityName
	 *            entity for which permissions should be added.
	 */
	@Override
	public boolean addCustomRoleAndPermission(String roleName, Long userId, TypeOfRole typeOfRole,
			Map<Locale, String> titles, Map<Locale, String> descriptions, String[] actionIds,
			String entityName) {
		long companyId = PortalUtil.getDefaultCompanyId();
		LOG.info(String.format("Adding role  %s with userId = %d", roleName, userId));
		try {
			Role role = roleLocalService.addRole(userId, null, 0L, roleName, titles, descriptions,
					typeOfRole.getValue(), null, null);
			LOG.info(String.format("Added the role", role.getCreateDate()));
			if (actionIds != null) {
				for (String actionId : actionIds) {
					addPermission(role.getRoleId(), companyId, actionId, entityName);
				}
			} else {
				LOG.info("Permissions to the roles were not found. You should add the permissions later.");
			}
			return true;
		} catch (PortalException e) {
			LOG.info("For some reason the role was not added. Please check and confirm your role credentials");
			return false;
		}
	}

	/**
	 * Add a Permission to the custom Role.
	 * 
	 * @param roleId
	 *            name of the role to be added.
	 * @param actionId
	 *            permission which should be added to the role.
	 * @param entityName
	 *            entity for which the permission should be added.
	 * @throws PortalException
	 */
	private void addPermission(Long roleId, Long companyId, String actionId, String entityName)
			throws PortalException {

		LOG.info(String.format("Starting to add permision %s to entity %s", actionId, entityName));

		resourcePermissionLocalService.addResourcePermission(companyId, entityName, ResourceConstants.SCOPE_COMPANY,
				String.valueOf(companyId), roleId, actionId);

		LOG.info(String.format("Added permision %s to entity %s", actionId, entityName));
	}
}
