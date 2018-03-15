package nl.finalist.liferay.lam.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
import com.liferay.portal.kernel.util.StringUtil;

import nl.finalist.liferay.lam.api.model.Column;
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
    	Group site = groupService.fetchGroup(defaultValue.getDefaultCompany().getCompanyId(), siteKey);
        Layout layout = null;
        try {
            layout = layoutService.addLayout(defaultValue.getDefaultUserId(), site.getGroupId(), page.isPrivatePage(),
                determineParentLayoutId(site.getGroupId(), page),
                LocaleMapConverter.convert(page.getNameMap()),
                page.getTitleMap(),
                page.getDescriptionMap(), null, null,
                determinePageType(page), determineTypeSettings(page, site), page.isHiddenPage(),
                LocaleMapConverter.convert(page.getFriendlyUrlMap()),
                new ServiceContext());
        } catch (PortalException e) {
            LOG.error("While adding page: " + page, e);
        }

        LOG.info(String.format("Page '%s' with url '%s' added", 
            layout.getName(LocaleUtil.getDefault()), layout.getFriendlyURL()));

        Map<String, String> customFields = page.getCustomFields();
        if (customFields != null) {
            for (Map.Entry<String, String> field : customFields.entrySet()) {
                String fieldKey = field.getKey();
                String value = field.getValue();
                LOG.debug(String.format("Page Custom field %s now has value %s", fieldKey, value));
                customFieldsService.addCustomFieldValue(Layout.class.getName(), fieldKey, layout.getPrimaryKey(), value);
            }
        }
    }
    
    private String determinePageType (PageModel page) {
    	String type = "portlet";
    	if (page.getLinkedPageUrl() != null) {
        	type="link_to_layout";
        } else if(page.getExternalUrl() != null) {
        	type = "url";
        }
    	return type;
    }
    
    private String determineTypeSettings(PageModel page, Group site) {
    	List<String> addedTypeSettings = new ArrayList<>();

    	// add typesettings from the script
    	if(page.getTypeSettings() != null) {
    		addedTypeSettings.add(page.getTypeSettings());
    	}
    	
    	// add typesettings for the page type
        String pageTypeSettings = determinePageTypeSettings(page, site);
        if (!pageTypeSettings.equals("")) {
        	addedTypeSettings.add(pageTypeSettings);
        }
        
        // add typesettings for the columns
        if (page.getColumns() != null) {
        	addedTypeSettings.add(determineTypeSettingsForColumns(page));
        }

        LOG.info("TEST:"+StringUtil.merge(addedTypeSettings));
        return StringUtil.merge(addedTypeSettings);
    }

	private String determinePageTypeSettings(PageModel page, Group site) {
    	String pageTypeSettings = "";
		if (page.getLinkedPageUrl() != null) {
        	pageTypeSettings = determineTypeSettingsForLinkedLayout(page, site);
        } else if(page.getExternalUrl() != null) {
        	pageTypeSettings = "url="+page.getExternalUrl();
        }
		return pageTypeSettings;
	}

    /**
     * Generates a String of the format column-1=portletId-1, portletId-2, column-2=portletId-3
     * 
     * @param page Page containing the columns we want to add
     * @return
     */
	private String determineTypeSettingsForColumns(PageModel page) {
		StringBuilder columnSettings = new StringBuilder();
		for (int i = 0; i < page.getColumns().size(); i++) {
			Column column = page.getColumns().get(i);
			// the column counter starts at 1, and not at 0
			columnSettings.append("column-"+(i+1));
			columnSettings.append("=");
			columnSettings.append(StringUtil.merge(column.getPortletIds()));
			columnSettings.append("\n");
		}
		return columnSettings.toString();
	}

	private String determineTypeSettingsForLinkedLayout(PageModel page, Group site) {
		String linkedLayoutTypeSettings = "";
    	long linkedLayoutId = 0;
    	try {
			linkedLayoutId = determineLayoutId(site.getGroupId(), page.isPrivatePage(), page.getLinkedPageUrl());
		} catch (NoSuchLayoutException e) {
			LOG.info(String.format("No linked layout found for url: %s", page.getLinkedPageUrl()));
		}
    	if (linkedLayoutId > 0) {
    		linkedLayoutTypeSettings = "linkToLayoutId="+linkedLayoutId;
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
        long layoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
        Layout page;

        if (url != null) {
            try {
                page = layoutService.getFriendlyURLLayout(groupId, isPrivatePage, url);
                layoutId = page.getLayoutId();
            } catch (PortalException e) {
                LOG.info(String.format("Exception while fetching page by its friendly URL '%s', " +
                    "will return default parent id. Exception: %s", url, e.getMessage()));
            }
        }
        return layoutId;
    }

    @Override
    public void updatePage(long layoutId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException {
        byte[] iconBytes = new byte[0];
        layoutService.updateLayout(groupId, page.isPrivatePage(), layoutId, determineParentLayoutId(groupId, page),
            LocaleMapConverter.convert(page.getNameMap()), page.getTitleMap(),
            page.getDescriptionMap(), null, null, determinePageType(page), false, LocaleMapConverter.convert(page.getFriendlyUrlMap()), false, iconBytes, new ServiceContext());
        LOG.info(String.format("Page %s updated", page.getNameMap().get(LocaleUtil.getSiteDefault())));
    }

    @Override
    public Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL) {
        return layoutService.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyURL);
    }
}
