package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = UserGroups.class)
public class UserGroupsImpl implements UserGroups {

    @Reference
    private UserGroupLocalService userGroupLocalService;

    @Reference
    private CustomFields customFieldsService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private CompanyLocalService companyService;

    private static final Log LOG = LogFactoryUtil.getLog(UserGroupsImpl.class);

    @Override
    public void addUserGroup(String[] webIds, String name, String description, Map<String, String> customFields) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                addUserGroupInCompany(webId, name, description, customFields);
            }
        } else {
            addUserGroupInCompany(defaultValue.getDefaultCompany().getWebId(), name, description, customFields);
        }

    }

    private void addUserGroupInCompany(String webId, String name, String description, Map<String, String> customFields) {
        long companyId = 0;
        long userId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            companyId = company.getCompanyId();
            userId = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Add UserGroup for this company", webId));
            LOG.error(e);
        }
        if (companyId > 0 && userId > 0) {
            try {
                UserGroup group = userGroupLocalService.fetchUserGroup(companyId, name);
                if (group != null) {
                    LOG.info(String.format("The user group %s already exists in company with webId %s", name, webId));
                } else {
                    group = userGroupLocalService.addUserGroup(userId, companyId, name, description, new ServiceContext());
                    LOG.info(String.format("Added user group %s in company with webId %s", name, webId));
                }

                if (customFields != null) {
                    for (String fieldName : customFields.keySet()) {
                        customFieldsService.addCustomFieldValue(new String[] {webId}, UserGroup.class.getName(), fieldName, group.getPrimaryKey(),
                                customFields.get(fieldName));
                        LOG.info("Add a custom field was successful");
                    }
                }
            } catch (PortalException e) {
                LOG.error(String.format("Adding the user group %s in company with webId %s failed", name, webId), e);
            }
        }

    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
