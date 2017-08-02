package nl.finalist.liferay.lam.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.DuplicateGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import nl.finalist.liferay.lam.api.model.PageModel;

@Component(immediate = true, service = Site.class)
public class SiteImpl implements Site {

	@Reference
	private GroupLocalService groupService;

	@Reference
	private CustomFields customFieldsService;
	@Reference
	private Page pageService;

	private static final Log LOG = LogFactoryUtil.getLog(SiteImpl.class);

	@Override
	public void addSite(Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL,
			Map<String, String> customFields, List<PageModel> pages) {
		try {
			Group group = groupService.addGroup(DeafultCompanyUtil.getDefaultUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID,
					Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, descriptionMap,
					GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL,
					true, false, true, null);
			LOG.info(String.format("Group %s was added", LocaleUtil.getDefault()));

			if (customFields != null) {
				for (String fieldName : customFields.keySet()) {
					customFieldsService.addCustomFieldValue(Group.class.getName(), fieldName, group.getPrimaryKey(),
							customFields.get(fieldName));
					LOG.info(String.format("Custom field %s now has value %s", fieldName, customFields.get(fieldName)));
				}
			}

			if (pages != null) {
				for (PageModel page : pages) {
					pageService.addPage(DeafultCompanyUtil.getDefaultUserId(), group.getGroupId(), page);
				}
			}
		} catch (DuplicateGroupException dge) {
			LOG.error("The site already exists.");
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void updateSite(String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String friendlyURL, Map<String, String> customFields, List<PageModel> pages) {
		Group group;
		try {
			group = groupService.getGroup(PortalUtil.getDefaultCompanyId(), groupKey);
			groupService.updateGroup(group.getGroupId(), GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap,
					descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION,
					friendlyURL, false, true, null);
			LOG.info(String.format("Group %s was updated", groupKey));

			if (customFields != null) {
				for (String fieldName : customFields.keySet()) {
					customFieldsService.updateCustomFieldValue(Group.class.getName(), fieldName, group.getPrimaryKey(),
							customFields.get(fieldName));
					LOG.debug(
							String.format("Custom field %s now has value %s", fieldName, customFields.get(fieldName)));
				}
			}
			for (PageModel page : pages) {
				Set<Locale> locales = page.getFriendlyUrlMap().keySet();
			for (Locale locale : locales) {
				Layout existingPage = pageService.fetchLayout(group.getGroupId(), false, page.getFriendlyUrlMap().get(locale));
				if (existingPage != null) {					
					pageService.updatePage(existingPage.getLayoutId(), group.getGroupId(), page);
					LOG.info(String.format("page is updated %s", page.getNameMap().size()));
					break;
				} else {
                    pageService.addPage(DeafultCompanyUtil.getDefaultUserId(), group.getGroupId(), page);
                    LOG.info(String.format("page doesn't exists so it has been added"));
				}
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
			LOG.info(String.format("Group %s was deleted", groupKey));
		} catch (PortalException e) {
			LOG.error("The group was not deleted.");
		}
	}

	
}
