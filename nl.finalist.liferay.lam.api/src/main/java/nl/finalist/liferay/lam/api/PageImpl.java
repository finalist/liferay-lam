package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.api.model.ColumnModel;
import nl.finalist.liferay.lam.api.model.ContentModel;
import nl.finalist.liferay.lam.api.model.PageModel;
import nl.finalist.liferay.lam.api.model.PortletModel;
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

    @Reference
    private PortletPreferencesLocalService portletPreferencesLocalService;

    @Reference
    private CompanyLocalService companyService;

    @Override
    public void addPage(String[] webIds, String siteKey, PageModel page) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                addPageInCompany(webId, siteKey, page);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            addPageInCompany(webId, siteKey, page);
        }

    }

    private void addPageInCompany(String webId, String siteKey, PageModel page) {

        Locale localeThreadSiteDefaultLocale = LocaleThreadLocal.getDefaultLocale();
        Locale localeThreadDefaultLocale = LocaleThreadLocal.getSiteDefaultLocale();

        long companyId = 0;
        long userId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            userId = company.getDefaultUser().getUserId();
            companyId = company.getCompanyId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Add Page for this company", webId));
            LOG.error(e);
        }
        if (companyId > 0 && userId > 0) {
            Group site = groupService.fetchGroup(companyId, siteKey);

            if (site != null) {
                Layout layout = null;

                convertContentToPortlets(companyId, page);

                if (MapUtil.isNotEmpty(page.getNameMap())) {
                    Set<String> nameMapSet = page.getNameMap().keySet();
                    String[] locales = nameMapSet.toArray(new String[nameMapSet.size()]);
                    if (ArrayUtil.isNotEmpty(locales)) {
                        LocaleThreadLocal.setDefaultLocale(LocaleUtil.fromLanguageId(locales[0]));
                        LocaleThreadLocal.setSiteDefaultLocale(LocaleUtil.fromLanguageId(locales[0]));
                    }

                }

                try {
                    Layout oldLayout = layoutService.fetchLayoutByFriendlyURL(site.getGroupId(), page.isPrivatePage(),
                            page.getFriendlyUrlMap().get(LocaleUtil.getDefault().toString()));
                    if (oldLayout == null) {
                        layout = layoutService.addLayout(userId, site.getGroupId(), page.isPrivatePage(),
                                determineParentLayoutId(site.getGroupId(), page), LocaleMapConverter.convert(page.getNameMap()), page.getTitleMap(),
                                page.getDescriptionMap(), null, null, determinePageType(page), determineTypeSettings(page, site), page.isHiddenPage(),
                                LocaleMapConverter.convert(page.getFriendlyUrlMap()), new ServiceContext());

                        updatePortletPreferences(page, layout);

                        LOG.debug(String.format("Page '%s' with url '%s' added", layout.getName(LocaleUtil.getDefault()), layout.getFriendlyURL()));
                    } else {
                        updatePage(oldLayout, site.getGroupId(), userId, page);
                    }
                } catch (PortalException e) {
                    LOG.error("While adding page: " + page, e);
                }

                Map<String, String> customFields = page.getCustomFields();
                if (layout != null && customFields != null) {
                    for (Map.Entry<String, String> field : customFields.entrySet()) {
                        String fieldKey = field.getKey();
                        String value = field.getValue();
                        LOG.debug(String.format("Page Custom field %s now has value %s", fieldKey, value));
                        customFieldsService.addCustomFieldValue(new String[] {webId}, Layout.class.getName(), fieldKey, layout.getPrimaryKey(),
                                value);
                    }
                }
            }

        }
        LocaleThreadLocal.setDefaultLocale(localeThreadDefaultLocale);
        LocaleThreadLocal.setSiteDefaultLocale(localeThreadSiteDefaultLocale);
    }

    private void convertContentToPortlets(long companyId, PageModel page) {
        for (ColumnModel column : page.getColumns()) {
            for (ContentModel content : column.getContent()) {
                String portletId = "com_liferay_journal_content_web_portlet_JournalContentPortlet_INSTANCE_" + StringUtil.randomString(12);
                while (page.getPortletIds().contains(portletId)) {
                    // the portletId already exists in the page, create a new
                    // one
                    portletId = "com_liferay_journal_content_web_portlet_JournalContentPortlet_INSTANCE_" + StringUtil.randomString(12);
                }
                PortletModel portlet = new PortletModel(portletId);
                Map<String, String> preferences = new HashMap<>();
                Group contentGroup = groupService.fetchGroup(companyId, content.getSiteKey());
                preferences.put("groupId", Long.toString(contentGroup.getGroupId()));
                preferences.put("articleId", content.getArticleId());
                portlet.setPreferences(preferences);
                column.addPortlet(portlet);
                LOG.info(String.format("Added content %s", content.getArticleId()));
            }
        }
    }

    private void updatePortletPreferences(PageModel page, Layout layout) {
        for (ColumnModel column : page.getColumns()) {
            for (PortletModel portlet : column.getPortlets()) {
                if (portlet.getPreferences() != null) {
                    PortletPreferences preferences = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portlet.getId());

                    for (String key : portlet.getPreferences().keySet()) {
                        try {
                            preferences.setValue(key, portlet.getPreferences().get(key));
                        } catch (ReadOnlyException e) {
                            LOG.error(String.format("Preference %s is read only and cannot be set", key));
                        }
                    }

                    portletPreferencesLocalService.updatePreferences(PortletKeys.PREFS_OWNER_ID_DEFAULT, PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
                            layout.getPlid(), portlet.getId(), preferences);
                }
            }
        }
    }

    private String determinePageType(PageModel page) {
        String type = "portlet";
        if (page.getLinkedPageUrl() != null) {
            type = "link_to_layout";
        } else if (page.getExternalUrl() != null) {
            type = "url";
        }
        return type;
    }

    private String determineTypeSettings(PageModel page, Group site) {
        List<String> addedTypeSettings = new ArrayList<>();

        // add typesettings from the script
        if (page.getTypeSettings() != null) {
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
        return StringUtil.merge(addedTypeSettings, "\n");
    }

    private String determinePageTypeSettings(PageModel page, Group site) {
        String pageTypeSettings = "";
        if (page.getLinkedPageUrl() != null) {
            pageTypeSettings = determineTypeSettingsForLinkedLayout(page, site);
        } else if (page.getExternalUrl() != null) {
            pageTypeSettings = "url=" + page.getExternalUrl();
        }
        return pageTypeSettings;
    }

    /**
     * Generates a String of the format column-1=portletId-1, portletId-2,
     * column-2=portletId-3
     * 
     * @param page
     *            Page containing the columns we want to add
     * @return
     */
    private String determineTypeSettingsForColumns(PageModel page) {
        StringBuilder columnSettings = new StringBuilder();
        for (int i = 0; i < page.getColumns().size(); i++) {
            ColumnModel column = page.getColumns().get(i);
            // the column counter starts at 1, and not at 0
            columnSettings.append("column-" + (i + 1));
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
            linkedLayoutTypeSettings = "linkToLayoutId=" + linkedLayoutId;
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
                LOG.info(String.format("Exception while fetching page by its friendly URL '%s', " + "will return default parent id. Exception: %s",
                        url, e.getMessage()));
            }
        }
        return layoutId;
    }

    @Override
    public void updatePage(Layout layout, long groupId, long userId, PageModel page) throws PortalException {
        byte[] iconBytes = new byte[0];
        Map<Locale, String> nameMap = LocaleMapConverter.convert(page.getNameMap());
        if (nameMap == null || nameMap.isEmpty()) {
            nameMap = layout.getNameMap();
        }
        Map<Locale, String> friendlyURLMap = LocaleMapConverter.convert(page.getFriendlyUrlMap());

        Map<Locale, String> descriptionMap = page.getDescriptionMap();
        if (descriptionMap == null || descriptionMap.isEmpty()) {
            descriptionMap = layout.getDescriptionMap();
        }
        long parentLayoutId = determineParentLayoutId(groupId, page);
        if (parentLayoutId == 0L) {
            parentLayoutId = layout.getParentLayoutId();
        }
        Map<Locale, String> titleMap = page.getTitleMap();
        if (titleMap == null || titleMap.isEmpty()) {
            titleMap = layout.getTitleMap();
        }

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setUserId(userId);
        layout = layoutService.updateLayout(groupId, page.isPrivatePage(), layout.getLayoutId(), parentLayoutId, nameMap, titleMap, descriptionMap,
                null, null, determinePageType(page), page.isHiddenPage(), friendlyURLMap, false, iconBytes, serviceContext);
        updatePortletPreferences(page, layout);
        LOG.info(String.format("Page %s updated in group %s", nameMap.get(LocaleUtil.getSiteDefault()), groupId));
    }

    @Override
    public Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL) {
        return layoutService.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyURL);
    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}