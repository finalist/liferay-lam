package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.UserGroup;
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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = User.class)
public class UserImpl implements User {
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

    @Override
    public void createUser(String screenName, String emailAddress, String firstName, String lastName,
                    String[] roles, String[] groups, String[] userGroups) {
        Company company = defaultValue.getDefaultCompany();
        long companyId = company.getCompanyId();
        Locale locale = LocaleUtil.getDefault();
        long creatorUserId = defaultValue.getDefaultUserId();
        boolean autoPassword = true;
        String password1 = null;
        String password2 = null;
        long[] groupIds = getIdsFromNames(groups, "GROUP");
        long[] organizationIds = null;
        long[] roleIds = getIdsFromNames(roles, "ROLE");
        long[] userGroupIds = getIdsFromNames(userGroups, "USERGROUP");
        boolean sendEmail = true;
        try {
            userLocalService.addUser(creatorUserId, companyId, autoPassword, password1, password2, false, screenName,
                            emailAddress, 0, null, locale, firstName, null, lastName, 0L, 0L, true, 1, 1, 1970, null,
                            groupIds, organizationIds, roleIds, userGroupIds, sendEmail, new ServiceContext());
            LOG.info(String.format("User %s successfully added.", screenName));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while adding user %s", screenName) + e);
        }
    }

    @Override
    public void updateUser(String screenName, String newScreenName, String emailAddress,
                    String firstName, String lastName,String[] roles, String[] groups, String[] userGroups) {
        com.liferay.portal.kernel.model.User user = getUserIfExists(screenName);
        if (Validator.isNotNull(user)) {
            long[] roleIds = user.getRoleIds();
            long[] userGroupIds = user.getUserGroupIds();
            if(!ArrayUtil.isEmpty(groups)){
                long[] newGroupIds = getIdsFromNames(groups, "GROUP");
                try {
                    userLocalService.updateGroups(user.getUserId(), newGroupIds, new ServiceContext());
                } catch (PortalException e) {
                    LOG.info(String.format("PortalException while updating site memberships for user %s. Sites not updated.", screenName)+e);
                }
            }
            if(!ArrayUtil.isEmpty(roles)){
                roleIds = getIdsFromNames(roles, "ROLE");
                try {
                    roleLocalService.addUserRoles(user.getUserId(), roleIds);
                } catch (PortalException e) {
                    LOG.info(String.format("PortalException while updating roles for user %s. Roles not updated", screenName)+e);
                }
            }
            if(!ArrayUtil.isEmpty(userGroups)){
                userGroupIds = getIdsFromNames(userGroups, "USERGROUP");
                usergroupLocalService.addUserUserGroups(user.getUserId(), userGroupIds);
            }
            if(!Validator.isBlank(newScreenName)){
                user.setScreenName(newScreenName);
            }
            if(!Validator.isBlank(emailAddress)){
                user.setEmailAddress(emailAddress);
            }
            if(!Validator.isBlank(firstName)){
                user.setFirstName(firstName);
            }
            if(Validator.isNotNull(lastName) && !Validator.isBlank(lastName)){
                user.setLastName(lastName);
            }
            userLocalService.updateUser(user);
            LOG.info(String.format("User %s  successfully updated.", user.getScreenName()));
        }
        else{
            LOG.info(String.format("User %s can not be updated because it does not exist.", screenName));
        }
    }

    @Override
    public void deleteUser(String screenName) {
        com.liferay.portal.kernel.model.User user = getUserIfExists(screenName);
        if (Validator.isNotNull(user)) {
            try {
                userLocalService.deleteUser(user);
                LOG.info(String.format("User with screenname %s successfully deleted", screenName));
            } catch (PortalException e) {
                LOG.error(String.format(
                                "PortalException while deleting user with screenname %s , user not succesfully deleted.",
                                screenName) + e);
            }
        } else {
            LOG.info(String.format("No user with screenname %s could be found.", screenName));
        }
    }

    private com.liferay.portal.kernel.model.User getUserIfExists(String screenName) {
        Company company = defaultValue.getDefaultCompany();
        long companyId = company.getCompanyId();
        return userLocalService.fetchUserByScreenName(companyId, screenName);
    }

    private long getGroupId(String siteFriendlyURL) {
        long companyId = defaultValue.getDefaultCompany().getCompanyId();
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

    private long getRoleId(String name) {
        long companyId = defaultValue.getDefaultCompany().getCompanyId();
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

    private long getUserGroupId(String name) {
        long companyId = defaultValue.getDefaultCompany().getCompanyId();
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

    private long[] getIdsFromNames(String[] names, String type) {
        if (ArrayUtil.isEmpty(names)) {
            return null;
        } else {
            return Arrays.stream(names)
                .mapToLong(name -> {
                    switch (type) {
                        case "USERGROUP":
                            return getUserGroupId(name);
                        case "ROLE":
                            return getRoleId(name);
                        default:
                            return getGroupId(name);
                    }
                })
                .toArray();
        }
    }
}