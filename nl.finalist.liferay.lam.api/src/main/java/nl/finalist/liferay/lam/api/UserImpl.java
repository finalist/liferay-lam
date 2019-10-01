package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ContactLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = User.class)
public class UserImpl implements nl.finalist.liferay.lam.api.User {

    private static final Log LOG = LogFactoryUtil.getLog(UserImpl.class);

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private UserLocalService userLocalService;

    @Reference
    private GroupLocalService groupLocalService;

    @Reference
    private UserGroupLocalService usergroupLocalService;

    @Reference
    private RoleLocalService roleLocalService;

    @Reference
    private ContactLocalService contactLocalService;

    @Reference
    private CustomFields customFieldsService;

    @Reference
    private CompanyLocalService companyService;

    @Override
    public void createUser(String[] webIds, String screenName, String emailAddress, String firstName, String lastName, String[] roles,
                           String[] groups, String[] userGroups, Map<String, String> customFields) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                addUserInCompany(webId, screenName, emailAddress, firstName, lastName, roles, groups, userGroups, customFields);
            }
        } else {
            addUserInCompany(defaultValue.getDefaultCompany().getWebId(), screenName, emailAddress, firstName, lastName, roles, groups, userGroups,
                    customFields);
        }

    }

    private void addUserInCompany(String webId, String screenName, String emailAddress, String firstName, String lastName, String[] roles,
                                  String[] groups, String[] userGroups, Map<String, String> customFields) {

        Company company = getCompany(webId);
        Locale locale = LocaleUtil.getDefault();
        boolean autoPassword = true;
        String password1 = null;
        String password2 = null;
        long[] groupIds = getIdsFromNames(groups, "GROUP", webId);
        long[] organizationIds = null;
        long[] roleIds = getIdsFromNames(roles, "ROLE", webId);
        long[] userGroupIds = getIdsFromNames(userGroups, "USERGROUP", webId);
        boolean sendEmail = true;
        try {
            User user = userLocalService.addUser(company.getDefaultUser().getUserId(), company.getCompanyId(), autoPassword, password1, password2,
                    false, screenName, emailAddress, 0, null, locale, firstName, null, lastName, 0L, 0L, true, 1, 1, 1970, null, groupIds,
                    organizationIds, roleIds, userGroupIds, sendEmail, new ServiceContext());
            LOG.info(String.format("User %s successfully added in company with webId %s.", screenName, webId));

            if (customFields != null) {
                for (String fieldName : customFields.keySet()) {
                    customFieldsService.addCustomFieldValue(new String[] {webId}, User.class.getName(), fieldName, user.getPrimaryKey(),
                            customFields.get(fieldName));
                    LOG.info(String.format("Adding value %s to custom field %s of user %s was successful", customFields.get(fieldName), fieldName,
                            screenName));
                }
            }
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while adding user %s in company with webId %s.", screenName, webId) + e);
        }
    }

    @Override
    public void updateUser(String[] webIds, String screenName, String newScreenName, String emailAddress, String firstName, String lastName,
                           String[] roles, String[] groups, String[] userGroups, Map<String, String> customFields) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                updateUserInCompany(webId, screenName, newScreenName, emailAddress, firstName, lastName, roles, groups, userGroups, customFields);
            }
        } else {
            updateUserInCompany(defaultValue.getDefaultCompany().getWebId(), screenName, newScreenName, emailAddress, firstName, lastName, roles,
                    groups, userGroups, customFields);
        }

    }

    private void updateUserInCompany(String webId, String screenName, String newScreenName, String emailAddress, String firstName, String lastName,
                                     String[] roles, String[] groups, String[] userGroups, Map<String, String> customFields) {

        LOG.info(String.format("Started updating user %s in company with webId %s.", screenName, webId));
        User user = getUserIfExists(webId, screenName);
        if (Validator.isNotNull(user)) {
            long[] roleIds = user.getRoleIds();
            long[] userGroupIds = user.getUserGroupIds();
            if (!ArrayUtil.isEmpty(groups)) {
                long[] newGroupIds = getIdsFromNames(groups, "GROUP", webId);
                try {
                    userLocalService.updateGroups(user.getUserId(), newGroupIds, new ServiceContext());
                } catch (PortalException e) {
                    LOG.info(String.format("PortalException while updating site memberships for user %s. Sites not updated.", screenName) + e);
                }
            }
            if (!ArrayUtil.isEmpty(roles)) {
                roleIds = getIdsFromNames(roles, "ROLE", webId);
                try {
                    roleLocalService.addUserRoles(user.getUserId(), roleIds);
                } catch (PortalException e) {
                    LOG.info(String.format("PortalException while updating roles for user %s. Roles not updated", screenName) + e);
                }
            }
            if (customFields != null) {
                for (String fieldName : customFields.keySet()) {
                    customFieldsService.updateCustomFieldValue(new String[] {webId}, User.class.getName(), fieldName, user.getPrimaryKey(),
                            customFields.get(fieldName));
                    LOG.debug(String.format("Custom field %s now has value %s", fieldName, customFields.get(fieldName)));
                }
            }
            if (!ArrayUtil.isEmpty(userGroups)) {
                userGroupIds = getIdsFromNames(userGroups, "USERGROUP", webId);
                usergroupLocalService.addUserUserGroups(user.getUserId(), userGroupIds);
            }
            if (!Validator.isBlank(newScreenName)) {
                user.setScreenName(newScreenName);
            }
            if (!Validator.isBlank(emailAddress)) {
                user.setEmailAddress(emailAddress);
            }
            if (!Validator.isBlank(firstName)) {
                user.setFirstName(firstName);
            }
            if (Validator.isNotNull(lastName) && !Validator.isBlank(lastName)) {
                user.setLastName(lastName);
            }
            userLocalService.updateUser(user);
            LOG.info(String.format("User %s  successfully updated in company with webId %s.", user.getScreenName(), webId));
        } else {
            LOG.info(String.format("User %s can not be updated in company with webId %s because it does not exist.", screenName, webId));
        }
    }

    @Override
    public void deleteUser(String[] webIds, String screenName) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                deleteUserInCompany(webId, screenName);
            }
        } else {
            deleteUserInCompany(defaultValue.getDefaultCompany().getWebId(), screenName);
        }

    }

    private void deleteUserInCompany(String webId, String screenName) {
        User user = getUserIfExists(webId, screenName);
        if (Validator.isNotNull(user)) {
            try {
                userLocalService.deleteUser(user);
                LOG.info(String.format("User with screenname %s successfully deleted from company with webId %s", screenName, webId));
            } catch (PortalException e) {
                LOG.error(String.format(
                        "PortalException while deleting user with screenname %s from company with webId %s, user not succesfully deleted.",
                        screenName, webId) + e);
            }
        } else {
            LOG.info(String.format("No user with screenname %s could be found in company with webId %s.", screenName, webId));
        }
    }

    private User getUserIfExists(String webId, String screenName) {
        Company company = getCompany(webId);
        long companyId = company.getCompanyId();
        return userLocalService.fetchUserByScreenName(companyId, screenName);
    }

    private long getGroupId(String webId, String siteFriendlyURL) {
        long companyId = getCompany(webId).getCompanyId();
        long groupId = 0;
        if (!Validator.isBlank(siteFriendlyURL)) {
            Group group = groupLocalService.fetchFriendlyURLGroup(companyId, siteFriendlyURL);
            if (Validator.isNotNull(group)) {
                groupId = group.getGroupId();
            } else {
                LOG.error(String.format("Site %s can not be found, user is added to global group", siteFriendlyURL));
            }
        }
        return groupId;

    }

    private long getRoleId(String webId, String name) {
        long companyId = getCompany(webId).getCompanyId();
        long roleId = 0;
        if (!Validator.isBlank(name)) {
            Role role = roleLocalService.fetchRole(companyId, name);
            if (Validator.isNotNull(role)) {
                roleId = role.getRoleId();
            } else {
                LOG.error(String.format("Role %s can not be found, user is added without this role", name));
            }
        }
        return roleId;

    }

    private long getUserGroupId(String webId, String name) {
        long companyId = getCompany(webId).getCompanyId();
        long userGroupId = 0;
        if (Validator.isNotNull(name) && !Validator.isBlank(name)) {
            UserGroup userGroup = usergroupLocalService.fetchUserGroup(companyId, name);
            if (Validator.isNotNull(userGroup)) {
                userGroupId = userGroup.getUserGroupId();
            } else {
                LOG.error(String.format("Usergroup %s can not be found, user is added without usergroup", name));
            }
        }
        return userGroupId;

    }

    private long[] getIdsFromNames(String[] names, String type, String webId) {
        if (ArrayUtil.isEmpty(names)) {
            return null;
        } else {
            return Arrays.stream(names).mapToLong(name -> {
                switch (type) {
                case "USERGROUP":
                    return getUserGroupId(webId, name);
                case "ROLE":
                    return getRoleId(webId, name);
                default:
                    return getGroupId(webId, name);
                }
            }).toArray();
        }
    }

    private Company getCompany(String webId) {
        try {
            return companyService.getCompanyByWebId(webId);
        } catch (PortalException e) {
            return defaultValue.getDefaultCompany();
        }

    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }

}
