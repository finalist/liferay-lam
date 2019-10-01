package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.NoSuchResourceActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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

    @Reference
    private UserLocalService userLocalService;

    @Reference
    private CompanyLocalService companyService;

    /**
     * Add a custom Role.
     * 
     * @param webIds
     *            webIds of companies where roles are to be created.
     * @param roleName
     *            name of the role to be added.
     * @param typeOfRole
     *            type of the role.
     * @param titles
     *            role titles which is a map of titles by Locale.
     * @param descriptions
     *            role descriptions which is a map of descriptions by Locale.
     * @param permissions
     *            Map that holds the entityName as key and a String array of
     *            actionids as the value
     */
    @Override
    public boolean addCustomRoleAndPermission(String[] webIds, String roleName, TypeOfRole typeOfRole, Map<Locale, String> titles,
                                              Map<Locale, String> descriptions, Map<String, List<String>> permissions) {

        boolean roleCreated = false;
        int roleCreatedCount = 0;
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                boolean curRoleCreated = createRoleInCompany(getCompanyId(webId), roleName, typeOfRole, titles, descriptions, permissions);
                if (curRoleCreated) {
                    roleCreatedCount++;
                }
            }
            if (roleCreatedCount == webIds.length) {
                roleCreated = true;
            }
        } else {
            roleCreated = createRoleInCompany(PortalUtil.getDefaultCompanyId(), roleName, typeOfRole, titles, descriptions, permissions);
        }
        return roleCreated;
    }

    private boolean createRoleInCompany(long companyId, String roleName, TypeOfRole typeOfRole, Map<Locale, String> titles,
                                        Map<Locale, String> descriptions, Map<String, List<String>> permissions) {

        LOG.debug(String.format("Adding role %s in company with id %s", roleName, companyId));
        try {
            Long userId = userLocalService.getDefaultUserId(companyId);

            if (roleLocalService.fetchRole(companyId, roleName) != null) {
                LOG.debug("This role already exists");
                return false;
            } else {
                Role role = roleLocalService.addRole(userId, null, 0L, roleName, titles, descriptions, typeOfRole.getValue(), null, null);
                LOG.info(String.format("Added role %s", roleName));
                addPermission(role.getRoleId(), companyId, permissions);
                return true;
            }
        } catch (PortalException e) {
            LOG.error(e);
            return false;
        }
    }

    /**
     * Add a Permission to the custom Role.
     * 
     * @param roleId
     *            name of the role to be added.
     * @param permissions
     *            Map that holds the entityName as key and a String array of
     *            actionids as the value
     * @throws PortalException
     */
    private void addPermission(Long roleId, Long companyId, Map<String, List<String>> permissions) throws PortalException {
        Set<String> entityNames = permissions.keySet();
        for (String entityName : entityNames) {
            List<String> actionIds = permissions.get(entityName);
            for (String actionId : actionIds) {
                try {
                    resourcePermissionLocalService.addResourcePermission(companyId, entityName, ResourceConstants.SCOPE_COMPANY,
                            String.valueOf(companyId), roleId, actionId);
                    LOG.info(String.format("Added permision %s to entity %s", actionId, entityName));
                } catch (NoSuchResourceActionException e) {
                    LOG.error(String.format("The action %s is not valid for entity %s", actionId, entityName));
                }
            }
        }
    }

    private long getCompanyId(String webId) {
        try {
            Company company = companyService.getCompanyByWebId(webId);
            return company.getCompanyId();
        } catch (PortalException e) {
            return 0l;
        }

    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
