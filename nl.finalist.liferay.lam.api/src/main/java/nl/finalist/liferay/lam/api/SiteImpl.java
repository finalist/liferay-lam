package nl.finalist.liferay.lam.api;

import com.liferay.exportimport.kernel.service.StagingLocalService;
import com.liferay.portal.kernel.exception.DuplicateGroupException;
import com.liferay.portal.kernel.exception.GroupFriendlyURLException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.api.model.PageModel;

@Component(immediate = true, service = Site.class)
public class SiteImpl implements Site {

    @Reference
    private GroupLocalService groupService;

    @Reference
    private StagingLocalService stagingLocalService;

    @Reference
    private CustomFields customFieldsService;

    @Reference
    private Page pageService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private CompanyLocalService companyService;

    private static final Log LOG = LogFactoryUtil.getLog(SiteImpl.class);

    @Override
    public void addSite(String[] webIds, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL,
                        Map<String, String> customFields, List<PageModel> pages) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                addSiteInCompany(webId, friendlyURL, pages, nameMap, descriptionMap, customFields);
            }
        } else {
            addSiteInCompany(defaultValue.getDefaultCompany().getWebId(), friendlyURL, pages, nameMap, descriptionMap, customFields);
        }

    }

    private void addSiteInCompany(String webId, String friendlyURL, List<PageModel> pages, Map<Locale, String> nameMap,
                                  Map<Locale, String> descriptionMap, Map<String, String> customFields) {

        Locale localeThreadSiteDefaultLocale = LocaleThreadLocal.getDefaultLocale();
        Locale localeThreadDefaultLocale = LocaleThreadLocal.getSiteDefaultLocale();

        long userId = getDefaultUserId(webId);
        if (userId > 0) {
            try {

                if (MapUtil.isNotEmpty(nameMap)) {
                    Set<Locale> nameMapSet = nameMap.keySet();
                    Locale[] locales = nameMapSet.toArray(new Locale[nameMapSet.size()]);
                    if (ArrayUtil.isNotEmpty(locales)) {
                        LocaleThreadLocal.setDefaultLocale(locales[0]);
                        LocaleThreadLocal.setSiteDefaultLocale(locales[0]);
                    }

                }

                Group group = groupService.addGroup(userId, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L,
                        GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true,
                        GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false, true, null);
                LOG.info(String.format("Site %s was added in company with webId %s ", nameMap.get(LocaleUtil.getSiteDefault()), webId));

                if (customFields != null) {
                    for (String fieldName : customFields.keySet()) {
                        customFieldsService.addCustomFieldValue(new String[] {webId}, Group.class.getName(), fieldName, group.getPrimaryKey(),
                                customFields.get(fieldName));
                        LOG.info(String.format("Custom field '%s' now has value '%s'", fieldName, customFields.get(fieldName)));
                    }
                }

                if (pages != null) {

                    LOG.debug(String.format("Adding pages defaultuser : %d , group : %d", userId, group.getGroupId()));

                    for (PageModel page : pages) {
                        LOG.debug("Add page : " + page.getNameMap().get(LocaleUtil.getSiteDefault().toString()));
                        pageService.addPage(new String[] {webId}, group.getGroupKey(), page);
                    }
                }

            } catch (DuplicateGroupException | GroupFriendlyURLException e1) {
                LOG.warn(String.format("The site %s already exists in company with webId %s .", nameMap.get(LocaleUtil.getSiteDefault()), webId));
            } catch (PortalException e) {
                LOG.error(String.format("While adding group for site '%s'", friendlyURL), e);
            }
        }
        LocaleThreadLocal.setDefaultLocale(localeThreadDefaultLocale);
        LocaleThreadLocal.setSiteDefaultLocale(localeThreadSiteDefaultLocale);

    }

    private long getDefaultUserId(String webId) {
        try {
            Company company = companyService.getCompanyByWebId(webId);
            return company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Add Site for this company", webId));
            LOG.error(e);
            return 0l;
        }

    }

    @Override
    public void updateSite(String[] webIds, String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL,
                           Map<String, String> customFields, List<PageModel> pages, Boolean stagingEnabled) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                updateSiteInCompany(stagingEnabled, webId, groupKey, friendlyURL, pages, nameMap, descriptionMap, customFields);
            }
        } else {
            updateSiteInCompany(stagingEnabled, defaultValue.getDefaultCompany().getWebId(), groupKey, friendlyURL, pages, nameMap, descriptionMap,
                    customFields);
        }

    }

    private void updateSiteInCompany(Boolean stagingEnabled, String webId, String groupKey, String friendlyURL, List<PageModel> pages,
                                     Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, Map<String, String> customFields) {

        Locale localeThreadSiteDefaultLocale = LocaleThreadLocal.getDefaultLocale();
        Locale localeThreadDefaultLocale = LocaleThreadLocal.getSiteDefaultLocale();

        Group group = null;
        Company company = null;
        long defaultUserID = 0;

        try {
            company = companyService.getCompanyByWebId(webId);
            defaultUserID = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Update Site for this company", webId));
            LOG.error(e);
        }
        if (company != null) {
            try {
                group = groupService.getGroup(company.getCompanyId(), groupKey);
            } catch (PortalException e) {
                LOG.error(String.format("group not found with key %s, skipping Add Vocabulary for company with webId %s", groupKey, webId));
                LOG.error(e);
            }
        }

        if (group != null && defaultUserID > 0) {

            if (MapUtil.isEmpty(nameMap)) {
                nameMap = group.getNameMap();
            }
            if (MapUtil.isEmpty(descriptionMap)) {
                descriptionMap = group.getDescriptionMap();
            }
            if (Validator.isNull(friendlyURL)) {
                friendlyURL = group.getFriendlyURL();
            }

            try {

                if (MapUtil.isNotEmpty(nameMap)) {
                    Set<Locale> nameMapSet = nameMap.keySet();
                    Locale[] locales = nameMapSet.toArray(new Locale[nameMapSet.size()]);
                    if (ArrayUtil.isNotEmpty(locales)) {
                        LocaleThreadLocal.setDefaultLocale(locales[0]);
                        LocaleThreadLocal.setSiteDefaultLocale(locales[0]);
                    }
                }

                groupService.updateGroup(group.getGroupId(), GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap,
                        GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false, true, null);
                LOG.info(String.format("Group (aka site) %s was updated", group.getGroupKey()));

                if (customFields != null) {
                    for (String fieldName : customFields.keySet()) {
                        customFieldsService.updateCustomFieldValue(new String[] {webId}, Group.class.getName(), fieldName, group.getPrimaryKey(),
                                customFields.get(fieldName));
                        LOG.debug(String.format("Custom field %s now has value %s", fieldName, customFields.get(fieldName)));
                    }
                }
                for (PageModel page : pages) {
                    Set<String> locales = page.getFriendlyUrlMap().keySet();
                    // TODO: I don't think this for-loop is correct. It now adds
                    // pages for each locale, whereas it should just add a page
                    // for
                    // the default locale
                    for (String locale : locales) {
                        Layout existingPage = pageService.fetchLayout(group.getGroupId(), false, page.getFriendlyUrlMap().get(locale));
                        if (existingPage != null) {
                            pageService.updatePage(existingPage, group.getGroupId(), page);
                            LOG.info(String.format("page %s is updated ", page.getNameMap().get(locale)));
                            break;
                        } else {
                            pageService.addPage(new String[] {webId}, groupKey, page);
                            LOG.info(String.format("page doesn't exists so it has been added: %s", page.getNameMap().get(locale)));
                        }
                    }
                }

                if (group != null && stagingEnabled != null) {
                    long groupId = group.getGroupId();
                    if (stagingEnabled) {
                        if (!group.hasStagingGroup()) {
                            stagingLocalService.enableLocalStaging(defaultUserID, group, false, false, new ServiceContext());
                            LOG.info(String.format("Staging was enabled for group %s", groupId));
                        } else {
                            LOG.info(String.format("No changes propagated; staging was already enabled for group %s", groupId));
                        }
                    } else {
                        if (group.hasStagingGroup()) {
                            stagingLocalService.disableStaging(group, new ServiceContext());
                            LOG.info(String.format("Staging was disabled for group %s", groupId));
                        } else {
                            LOG.info(String.format("No changes propagated; staging was already disabled for group %s", groupId));
                        }
                    }
                }
            } catch (PortalException e) {
                LOG.error("The group was not updated.", e);
            }
        }
        LocaleThreadLocal.setDefaultLocale(localeThreadDefaultLocale);
        LocaleThreadLocal.setSiteDefaultLocale(localeThreadSiteDefaultLocale);
    }

    @Override
    public void deleteSite(String[] webIds, String groupKey) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                try {
                    Company company = companyService.getCompanyByWebId(webId);
                    deleteSiteInCompany(company.getCompanyId(), groupKey);
                } catch (PortalException e) {
                    LOG.error("The group was not deleted .", e);
                }
            }
        } else {
            deleteSiteInCompany(PortalUtil.getDefaultCompanyId(), groupKey);
        }

    }

    private void deleteSiteInCompany(long companyId, String groupKey) {
        Group group;
        try {
            group = groupService.getGroup(companyId, groupKey);
            groupService.deleteGroup(group.getGroupId());
            LOG.info(String.format("Group %s was deleted", groupKey));
        } catch (PortalException e) {
            LOG.error("The group was not deleted.");
        }
    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }

}
