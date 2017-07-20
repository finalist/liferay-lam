package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.util.PropsUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

@Component(immediate = true, service = UserGroups.class)
public class UserGroupsImpl implements UserGroups {

    @Reference
    private UserGroupLocalService userGroupLocalService;
    @Reference
    CompanyLocalService companyService;
    @Reference
    CustomFields customFieldsService;

    private static final Log LOG = LogFactoryUtil.getLog(UserGroupsImpl.class);

    @Override
    public boolean addUserGroup(String name, String description, Map<String, String> customFields) {
        try {
        	UserGroup group = userGroupLocalService.fetchUserGroup(getDefaultCompany().getCompanyId(), name);
        	if (group != null) {
        		LOG.debug("The user group already exists");
        	} else {
				userGroupLocalService.addUserGroup(getDefaultUserId(), getDefaultCompany().getCompanyId(), name, description, new ServiceContext());
				return true;
        	}

            for (String fieldName : customFields.keySet()) {
                customFieldsService.addCustomFieldValue(UserGroup.class.getName(), fieldName, group.getPrimaryKey(), customFields.get(fieldName));
            }
		} catch (PortalException e) {
			LOG.error("Adding the user group failed");
		}
        return false;
    }

    private long getDefaultUserId() {
        Company defaultCompany = getDefaultCompany();
        long userId = 0;
        try {
            userId = defaultCompany.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving default userId, error is %s", e.getMessage()));
        }
        return userId;
    }

    private Company getDefaultCompany() {
        Company defaultCompany = null;
        String webId = PropsUtil.get("company.default.web.id");
        try {
            defaultCompany = companyService.getCompanyByWebId(webId);
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving default company, error is %s", e.getMessage()));
        }
        return defaultCompany;
    }
}
