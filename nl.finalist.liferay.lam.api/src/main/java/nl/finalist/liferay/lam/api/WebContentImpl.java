package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleResource;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.service.JournalArticleResourceLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.util.Constants;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.WebContent}
 */
@Component(immediate = true, service = WebContent.class)
public class WebContentImpl implements WebContent {

    @Reference
    private JournalArticleLocalService journalArticleService;

    @Reference
    private CounterLocalService counterLocalService;

    @Reference
    private GroupLocalService groupLocalService;

    @Reference
    private JournalArticleResourceLocalService journalArticleResourceLocalService;

    @Reference
    private ResourcePermissionLocalService resourcePermissionLocalService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private ClassNameLocalService classNameLocalService;

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Reference
    private CompanyLocalService companyService;

    private static final Log LOG = LogFactoryUtil.getLog(WebContentImpl.class);

    @Override
    public void createOrUpdateWebcontent(String[] webIds, String articleId, String siteFriendlyURL, Map<Locale, String> titleMap, String fileUrl,
                                         Bundle bundle, String urlTitle, String structureKey, String templateKey) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createOrUpdateWebcontentInCompany(webId, articleId, siteFriendlyURL, fileUrl, urlTitle, structureKey, templateKey, titleMap, bundle);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            createOrUpdateWebcontentInCompany(webId, articleId, siteFriendlyURL, fileUrl, urlTitle, structureKey, templateKey, titleMap, bundle);
        }
        LOG.debug(String.format("Article creation/update process completed for article with id '%s'", articleId));
    }

    private void createOrUpdateWebcontentInCompany(String webId, String articleId, String siteFriendlyURL, String fileUrl, String urlTitle,
                                                   String structureKey, String templateKey, Map<Locale, String> titleMap, Bundle bundle) {

        long globalGroupId = 0;
        long companyId = 0;
        long userId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            companyId = company.getCompanyId();
            globalGroupId = company.getGroupId();
            userId = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Create/Updated Webcontent for this company", webId));
            LOG.error(e);
        }

        if (globalGroupId > 0 && companyId > 0 && userId > 0) {
            LOG.info(String.format("Started CreateOrUpdate Webcontent with article Id %s for company with webId %s", articleId, webId));
            String xmlContent = getContentFromBundle(fileUrl, bundle);
            long classNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
            structureKey = addDefaultKeyIfNeeded(structureKey);
            templateKey = addDefaultKeyIfNeeded(templateKey);
            DDMStructure ddmStructure = getStructure(structureKey, globalGroupId, classNameId);
            long groupId = determineGroupId(siteFriendlyURL, globalGroupId, companyId);
            JournalArticle webcontent = journalArticleService.fetchArticle(groupId, articleId);
            if (webcontent == null) {
                webcontent = createNewWebcontent(articleId, companyId, userId, groupId);
            }
            updateWebcontent(titleMap, urlTitle, structureKey, templateKey, userId, xmlContent, ddmStructure, groupId, webcontent);
        }
    }

    private String addDefaultKeyIfNeeded(String key) {
        if (Validator.isNull(key) || Validator.isBlank(key)) {
            key = "BASIC-WEB-CONTENT";
        }
        return key;
    }

    private void updateWebcontent(Map<Locale, String> titleMap, String urlTitle, String structureKey, String templateKey, long userId,
                                  String xmlContent, DDMStructure ddmStructure, long groupId, JournalArticle webcontent) {
        webcontent.setDDMStructureKey(structureKey);
        webcontent.setDDMTemplateKey(templateKey);
        webcontent.setTitleMap(titleMap);
        webcontent.setUrlTitle(urlTitle);
        webcontent.setContent(xmlContent);
        webcontent.setDisplayDate(new Date());
        webcontent.setModifiedDate(new Date());
        webcontent.setStatusDate(new Date());
        webcontent.setIndexable(true);

        setPermissions(JournalArticle.class.getName(), webcontent.getResourcePrimKey(), webcontent.getCompanyId());

        AssetEntry assetEntry = null;
        try {
            assetEntry = AssetEntryLocalServiceUtil.updateEntry(userId, groupId, JournalArticle.class.getName(), webcontent.getResourcePrimKey(),
                    new long[0], new String[0]);
        } catch (PortalException e) {
            LOG.error("PortalException while creating assetEntry for webcontent, assetentry not created", e);
        }

        publishWebcontent(titleMap, ddmStructure, webcontent, assetEntry);
    }

    private void publishWebcontent(Map<Locale, String> titleMap, DDMStructure ddmStructure, JournalArticle webcontent, AssetEntry assetEntry) {
        webcontent.setStatus(WorkflowConstants.STATUS_APPROVED);
        webcontent.persist();
        if (Validator.isNotNull(assetEntry)) {
            assetEntry.setPublishDate(new Date());
            assetEntry.setVisible(true);
            assetEntry.setClassTypeId(ddmStructure.getStructureId());
            assetEntry.setTitleMap(titleMap);
            AssetEntryLocalServiceUtil.updateAssetEntry(assetEntry);
        }
        // This is how Liferay determines the version in
        // 'JournalArticleLocalServiceImpl'
        webcontent.setVersion((MathUtil.format(webcontent.getVersion() + 0.1, 1, 1)));
        journalArticleService.updateJournalArticle(webcontent);
    }

    private JournalArticle createNewWebcontent(String articleId, long companyId, long userId, long groupId) {
        JournalArticle webcontent;
        webcontent = journalArticleService.createJournalArticle(counterLocalService.increment());
        LOG.info(String.format("Webcontent %s does not exist, creating new webcontent", articleId));
        webcontent.setGroupId(groupId);
        webcontent.setUserId(userId);
        webcontent.setArticleId(articleId);
        webcontent.setCompanyId(companyId);
        webcontent.setCreateDate(new Date());
        webcontent.setVersion(1.0d);

        JournalArticleResource resource;
        resource = journalArticleResourceLocalService.fetchArticleResource(groupId, articleId);

        if (resource == null) {
            LOG.debug(String.format("Resource doesn't exist for webcontent %s, creating new resource", articleId));
            resource = journalArticleResourceLocalService.createJournalArticleResource(counterLocalService.increment());
            resource.setGroupId(groupId);
            resource.setArticleId(articleId);
            resource.persist();
        }
        webcontent.setResourcePrimKey(resource.getResourcePrimKey());
        return webcontent;
    }

    private long determineGroupId(String siteFriendlyURL, long globalGroupId, long companyId) {
        long groupId = globalGroupId;
        if (Validator.isNotNull(siteFriendlyURL) && !Validator.isBlank(siteFriendlyURL)) {
            Group group = groupLocalService.fetchFriendlyURLGroup(companyId, siteFriendlyURL);
            if (Validator.isNotNull(group)) {
                groupId = group.getGroupId();
            } else {
                LOG.error(String.format("Site %s can not be found, webcontent is added to global group", siteFriendlyURL));
            }
        }
        return groupId;
    }

    @Override
    public void deleteWebContent(String[] webIds, String urlTitle) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                deleteWebContentInCompany(webId, urlTitle);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            deleteWebContentInCompany(webId, urlTitle);
        }

    }

    private void deleteWebContentInCompany(String webId, String urlTitle) {

        long groupId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Delete Webcontent for this company", webId));
            LOG.error(e);
        }
        if (groupId > 0) {
            JournalArticle article = journalArticleService.fetchArticleByUrlTitle(groupId, urlTitle);
            if (article != null) {
                try {
                    journalArticleService.deleteJournalArticle(article.getId());
                    LOG.info(String.format("article with urlTitle %s deleted from company with webId %s.", urlTitle, webId));
                } catch (PortalException e) {
                    LOG.error(e);
                }
            } else {
                LOG.info(String.format("article with urlTitle %s doesn't exists in company with webId %s so deletion was not possible", urlTitle,
                        webId));
            }
        }

    }

    public void setPermissions(String className, long primaryKey, long companyId) {
        Role userRole;
        try {
            userRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);
            resourcePermissionLocalService.setResourcePermissions(companyId, className, ResourceConstants.SCOPE_INDIVIDUAL, Long.toString(primaryKey),
                    userRole.getRoleId(), new String[] {ActionKeys.VIEW});
        } catch (PortalException e) {
            LOG.error("PortalException while setting webcontent resource permissions, permissions are not set", e);
        }

    }

    private String getContentFromBundle(String fileUrl, Bundle bundle) {
        URL url = bundle.getResource(fileUrl);
        String xmlContent = "";
        InputStream input;

        try {
            if (url != null) {
                input = url.openStream();
            } else {
                File script = new File(Constants.TEMP_LAM_SUBDIR + StringPool.SLASH + fileUrl);
                input = new FileInputStream(script);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
                xmlContent = br.lines().collect(Collectors.joining(System.lineSeparator()));
            } finally {
                input.close();
            }
        } catch (IOException e) {
            LOG.error(String.format("IOException while reading input for xmlContent %s", fileUrl), e);
        }

        return xmlContent;
    }

    private DDMStructure getStructure(String structureKey, long groupId, long classNameId) {
        DDMStructure ddmStructure = null;
        try {
            ddmStructure = ddmStructureLocalService.getStructure(groupId, classNameId, structureKey);
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving %s ", structureKey), e);
        }
        return ddmStructure;
    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
