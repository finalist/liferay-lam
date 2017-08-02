package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.DuplicateGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;

@Component(immediate = true, service=Site.class)
public class SiteImpl implements Site {

	@Reference
	private GroupLocalService groupService;
	@Reference
	private CompanyLocalService companyService;
	@Reference
	CustomFields customFieldsService;


	private static final Log LOG = LogFactoryUtil.getLog(SiteImpl.class);

    @Override
	public void addSite(Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL, Map<String, String> customFields) {
		try {
			Group group = groupService.addGroup(getDefaultUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L,
					GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap,
					descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION,
					friendlyURL, true, false, true, null);
			LOG.debug(String.format("Group %s was added", LocaleUtil.getDefault()));

			if (customFields != null) {
				for (String fieldName : customFields.keySet()) {
					customFieldsService.addCustomFieldValue(Group.class.getName(), fieldName, group.getPrimaryKey(), customFields.get(fieldName));
					LOG.info(String.format("Custom field %s now has value %s", fieldName, customFields.get(fieldName)));
				}
			}
		} catch(DuplicateGroupException dge) {
			LOG.error("The site already exists.");
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

    @Override
	public void updateSite(String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL, Map<String, String> customFields) {
		Group group;
		try {
			group = groupService.getGroup(PortalUtil.getDefaultCompanyId(), groupKey);
			groupService.updateGroup(group.getGroupId(), GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false, true, null);
			LOG.debug(String.format("Group %s was updated", groupKey));
			
			if (customFields != null) {
				for (String fieldName : customFields.keySet()) {
					customFieldsService.updateCustomFieldValue(Group.class.getName(), fieldName, group.getPrimaryKey(), customFields.get(fieldName));
					LOG.info(String.format("Custom field %s now has value %s", fieldName, customFields.get(fieldName)));
				}
			}
		} catch (PortalException e) {
			LOG.error("The group was not updated.");
		}
	}
	
    @Override
	public void deleteSite(String groupKey) {
		Group group;
		try {
			group = groupService.getGroup(PortalUtil.getDefaultCompanyId(), groupKey);
			groupService.deleteGroup(group.getGroupId());
			LOG.debug(String.format("Group %s was deleted", groupKey));
		} catch (PortalException e) {
			LOG.error("The group was not deleted.");
		}
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
