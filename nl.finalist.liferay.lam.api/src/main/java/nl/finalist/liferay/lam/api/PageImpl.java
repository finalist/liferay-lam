package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.api.model.PageModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;

@Component(immediate = true, service = Page.class)
public class PageImpl implements Page {

    private static final Log LOG = LogFactoryUtil.getLog(PageImpl.class);
    @Reference
    LayoutLocalService layoutService;
    @Reference
    private CustomFields customFieldsService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private GroupLocalService groupService;

    @Override
    public void addPage(String siteKey, PageModel page) {

        if (Validator.isNull(page.getType())) {
            page.setType("portlet");
        }

        Group site = groupService.fetchGroup(defaultValue.getDefaultCompany().getCompanyId(), siteKey);
        
        String linkedLayoutTypeSettings = determineTypeSettingsForLinkedLayout(page, site);

        String typeSettings = page.getTypeSettings() == null 
        		? linkedLayoutTypeSettings
        		: page.getTypeSettings()+linkedLayoutTypeSettings;
        
        Layout layout = null;
        try {
            layout = layoutService.addLayout(defaultValue.getDefaultUserId(), site.getGroupId(), page.isPrivatePage(),
                determineParentLayoutId(site.getGroupId(), page),
                LocaleMapConverter.convert(page.getNameMap()),
                page.getTitleMap(),
                page.getDescriptionMap(), null, null,
                page.getType(), typeSettings, page.isHiddenPage(),
                LocaleMapConverter.convert(page.getFriendlyUrlMap()),
                new ServiceContext());
        } catch (PortalException e) {
            LOG.error("While adding page: " + page, e);
        }

        LOG.info(String.format("Page '%s' with url '%s' added", 
            layout.getName(LocaleUtil.ENGLISH, true), layout.getFriendlyURL()));

        Map<String, String> customFields = page.getCustomFields();
        if (customFields != null) {
            for (String fieldName : customFields.keySet()) {
                LOG.debug(String.format("Page Custom field %s now has value %s", fieldName, customFields.get(fieldName)));

                customFieldsService.addCustomFieldValue(Layout.class.getName(), fieldName, layout.getPrimaryKey(),
                    customFields.get(fieldName));
            }
        }
    }

	private String determineTypeSettingsForLinkedLayout(PageModel page, Group site) {
		String linkedLayoutTypeSettings = "";
        if (page.getLinkedPageUrl() != null) {
        	long linkedLayoutId = 0;
        	try {
				linkedLayoutId = determineLayoutId(site.getGroupId(), page.isPrivatePage(), page.getLinkedPageUrl());
			} catch (NoSuchLayoutException e) {
				LOG.info(String.format("No linked layout found for url: %s", page.getLinkedPageUrl()));
			}
        	if (linkedLayoutId > 0) {
        		linkedLayoutTypeSettings = "linkToLayoutId="+linkedLayoutId;
        	}
        }
		return linkedLayoutTypeSettings;
	}

    private long determineParentLayoutId(long groupId, PageModel page) {
        long parentId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
       
        try {
            parentId = determineLayoutId(groupId, page.isPrivatePage(), page.getParentUrl());
        } catch (NoSuchLayoutException e) {
            LOG.info("The parent page could not be found, creating page at top level instead.");
        }
        
        return parentId;
    }
    
    private long determineLayoutId(long groupId, boolean isPrivatePage, String url) throws NoSuchLayoutException {
        long parentId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
        Layout page;

        if (url != null) {
            try {
                page = layoutService.getFriendlyURLLayout(groupId, isPrivatePage, url);
                parentId = page.getLayoutId();
            } catch (PortalException e) {
                LOG.info(String.format("Exception while fetching parent page by its friendly URL '%s', will create " +
                        "page at top level instead", url), e);
            }
        }
        return parentId;
    }

    @Override
    public void updatePage(long layoutId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException {
        if (page.getType() == null || page.getType().isEmpty()) {
            page.setType("portlet");
        }

        byte[] iconBytes = new byte[0];
        layoutService.updateLayout(groupId, page.isPrivatePage(), layoutId, determineParentLayoutId(groupId, page),
            LocaleMapConverter.convert(page.getNameMap()), page.getTitleMap(),
            page.getDescriptionMap(), null, null, page.getType(), false, LocaleMapConverter.convert(page.getFriendlyUrlMap()), false, iconBytes, new ServiceContext());
        LOG.info(String.format("Page %s updated", page.getNameMap().get(LocaleUtil.getSiteDefault())));
    }

    @Override
    public Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL) {
        return layoutService.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyURL);
    }
}
