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
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.BufferedReader;
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

    private static final Log LOG = LogFactoryUtil.getLog(WebContentImpl.class);

    //    @Override
    //    public void addWebContent(Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, String content,
    //                    String urlTitle) {
    //        try {
    //            LOG.debug(" Starting to add the article");
    //            JournalArticle article = journalArticleService.fetchArticleByUrlTitle(defaultValue.getGlobalGroupId(), urlTitle);
    //            if (article == null) {
    //                ServiceContext serviceContext = new ServiceContext();
    //                serviceContext.setScopeGroupId(defaultValue.getGlobalGroupId());
    //                serviceContext.setCurrentURL(urlTitle);
    //                journalArticleService.addArticle(defaultValue.getDefaultUserId(), defaultValue.getGlobalGroupId(),
    //                                DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, titleMap, descriptionMap,
    //                                processJournalArticleContent(content), "BASIC-WEB-CONTENT", "BASIC-WEB-CONTENT",
    //                                serviceContext);
    //
    //                LOG.info(String.format("Added an article with Title Url as %s", urlTitle));
    //            } else {
    //                LOG.info(String.format("article with the URL Title %s already exists , so addition was not succesful",
    //                                urlTitle));
    //            }
    //        } catch (PortalException e) {
    //            LOG.error(e);
    //        }
    //    }

    @Override
    public void createOrUpdateWebcontent(String articleId, String siteFriendlyURL, Map<Locale,String> titleMap, String fileUrl, Bundle bundle,
                    String urlTitle, String structureKey, String templateKey){
        long globalGroupId = defaultValue.getGlobalGroupId();
        long groupId = globalGroupId;
        long companyId = defaultValue.getDefaultCompany().getCompanyId();
        long userId = defaultValue.getDefaultUserId();
        String xmlContent = getContentFromBundle(fileUrl, bundle);
        long classNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        if(Validator.isNull(structureKey)|| Validator.isBlank(structureKey)){
            structureKey = "BASIC-WEB-CONTENT";
        }
        if(Validator.isNull(templateKey) || Validator.isBlank(templateKey)){
            templateKey = "BASIC-WEB-CONTENT";
        }
        DDMStructure ddmStructure = getStructure(structureKey, globalGroupId, classNameId);
        if(Validator.isNotNull(siteFriendlyURL)  && !Validator.isBlank(siteFriendlyURL)){
            Group group = groupLocalService.fetchFriendlyURLGroup(companyId, siteFriendlyURL);
            if(Validator.isNotNull(group)){
                groupId = group.getGroupId();
            }
            else{
                LOG.error(String.format("Site %s can not be found, webcontent %s is added to global group", siteFriendlyURL, articleId));
            }
        }
        JournalArticle webcontent = null;
        try {
            webcontent = journalArticleService.getArticle(groupId, articleId);
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving webcontent %s c, creating", articleId) + e);
        }
        if(webcontent == null){
            webcontent = journalArticleService.createJournalArticle(counterLocalService.increment());
            LOG.info(String.format("Webcontent %s does not exist, Creating new webcontent", articleId));
            webcontent.setGroupId(groupId);
            webcontent.setUserId(userId);
            webcontent.setArticleId(articleId);
            webcontent.setCompanyId(companyId);
            webcontent.setCreateDate(new Date());
            webcontent.setVersion(MathUtil.format(1.0, 1, 1));

            JournalArticleResource resource;
            resource = journalArticleResourceLocalService.fetchArticleResource(groupId, articleId);

            if(resource == null){
                LOG.debug(String.format("Resource doesn't exist for webcontent %s , creating new resource", articleId));
                resource = journalArticleResourceLocalService.createJournalArticleResource(counterLocalService.increment());
                resource.setGroupId(groupId);
                resource.setArticleId(articleId);
                resource.persist();
            }
            webcontent.setResourcePrimKey(resource.getResourcePrimKey());
        }
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
            assetEntry = AssetEntryLocalServiceUtil.updateEntry(
                            userId,
                            groupId,
                            JournalArticle.class.getName(),
                            webcontent.getResourcePrimKey(),
                            new long[0],
                            new String[0]
                            );
        } catch (PortalException e) {
            LOG.error("PortalException while creating assetEntry for webcontent, assetentry not created" + e);
        }

        webcontent.setStatus(WorkflowConstants.STATUS_APPROVED);
        webcontent.persist();
        if(Validator.isNotNull(assetEntry)){
            assetEntry.setPublishDate(new Date());
            assetEntry.setVisible(true);
            assetEntry.setClassTypeId(ddmStructure.getStructureId());
            assetEntry.setTitleMap(titleMap);
            AssetEntryLocalServiceUtil.updateAssetEntry(assetEntry);
        }
        webcontent.setVersion((MathUtil.format(webcontent.getVersion() + 0.1, 1, 1)));
        journalArticleService.updateJournalArticle(webcontent);
        LOG.info(String.format("Article creation/update process completed ", articleId));
    }

    //    @Override
    //    public void updateWebContent(Map<Locale, String> newTitleMap, Map<Locale, String> newDescriptionMap, String content,
    //                    String urlTitle) {
    //        JournalArticle article = journalArticleService.fetchArticleByUrlTitle(defaultValue.getGlobalGroupId(), urlTitle);
    //        LOG.info(" Starting to update the article");
    //        if (article == null) {
    //            addWebContent(newTitleMap, newDescriptionMap, content, urlTitle);
    //            LOG.info(String.format("The article with UrlTitle %s does not exist yet, so it was added", urlTitle));
    //        } else {
    //            try {
    //                ServiceContext serviceContext = new ServiceContext();
    //                serviceContext.setScopeGroupId(defaultValue.getGlobalGroupId());
    //                journalArticleService.updateArticle(defaultValue.getDefaultUserId(), defaultValue.getGlobalGroupId(), 0, article.getArticleId(),
    //                                article.getVersion(), newTitleMap, newDescriptionMap,
    //                                processJournalArticleContent(content), null, serviceContext);
    //                LOG.info("Updated the article");
    //            } catch (PortalException e) {
    //                LOG.error(e);
    //            }
    //        }
    //
    //    }

    @Override
    public void deleteWebContent(String urlTitle) {
        JournalArticle article = journalArticleService.fetchArticleByUrlTitle(defaultValue.getGlobalGroupId(), urlTitle);
        if (article != null) {
            try {
                journalArticleService.deleteJournalArticle(article.getId());
            } catch (PortalException e) {
                LOG.error(e);
            }
        } else {
            LOG.info(String.format("article with urlTitle %s doesn't exists so deletion was not possible", urlTitle));
        }

    }

    private String processJournalArticleContent(String content) {

        if (content.contains("<?xml version=\"1.0\"")) {
            return content;
        }

        StringBundler sb = new StringBundler(13);

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<root available-locales=\"");
        sb.append(LocaleUtil.getDefault());
        sb.append("\" default-locale=\"");
        sb.append(LocaleUtil.getDefault());
        sb.append("\">");
        sb.append("<static-content language-id=\"");
        sb.append(LocaleUtil.getDefault());
        sb.append("\">");
        sb.append("<![CDATA[");
        sb.append(content);
        sb.append("]]>");
        sb.append("</static-content></root>");

        return sb.toString();
    }

    public void setPermissions(String className, long primaryKey, long companyId) {
        Role userRole;
        try {
            userRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);
            resourcePermissionLocalService.setResourcePermissions(
                            companyId,
                            className,
                            ResourceConstants.SCOPE_INDIVIDUAL,
                            Long.toString(primaryKey),
                            userRole.getRoleId(),
                            new String[]{ActionKeys.VIEW}
                            );
        } catch (PortalException e) {
            LOG.error("PortalException while setting webcontent resource permissions, permissions are not set" + e);
        }


    }
    private String getContentFromBundle(String fileUrl, Bundle bundle) {
        URL url = bundle.getResource(fileUrl);
        String xmlContent = "";
        InputStream input;

        try {
            input = url.openStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
                xmlContent = br.lines().collect(Collectors.joining(System.lineSeparator()));
            } finally {
                input.close();
            }
        }
        catch (IOException e) {
            LOG.error("IOException while reading input for xmlContent " + fileUrl + " " + e);
        }
        return xmlContent;
    }
    private DDMStructure getStructure(String structureKey, long groupId, long classNameId){
        DDMStructure ddmStructure = null;
        try {
            ddmStructure = ddmStructureLocalService.getStructure(groupId, classNameId, structureKey);
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving %s ", structureKey) + e);
        }
        return ddmStructure;
    }
}
