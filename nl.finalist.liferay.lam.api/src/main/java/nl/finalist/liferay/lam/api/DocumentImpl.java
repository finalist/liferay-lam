package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.document.library.kernel.exception.NoSuchFolderException;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.util.Constants;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.Document}
 */
@Component(immediate = true, service = Document.class)
public class DocumentImpl implements Document {

    @Reference
    private DLAppLocalService dlAppLocalService;

    @Reference
    private GroupLocalService groupLocalService;

    @Reference
    private ResourcePermissionLocalService resourcePermissionLocalService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private CompanyLocalService companyService;

    private static final Log LOG = LogFactoryUtil.getLog(DocumentImpl.class);

    @Override
    public void createOrUpdateDocument(String[] webIds, String siteFriendlyURL, String title, String fileUrl, Bundle bundle) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createOrUpdateDocumentInCompany(webId, siteFriendlyURL, title, fileUrl, bundle);
            }
        } else {
            createOrUpdateDocumentInCompany(defaultValue.getDefaultCompany().getWebId(), siteFriendlyURL, title, fileUrl, bundle);
        }

        LOG.debug(String.format("Document creation/update process completed for document with title '%s'", title));
    }

    private void createOrUpdateDocumentInCompany(String webId, String siteFriendlyURL, String title, String fileUrl, Bundle bundle) {
        Company company = null;
        long companyGroupId = 0;
        long userId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            userId = company.getDefaultUser().getUserId();
            companyGroupId = company.getGroupId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Create/Updated Document for this company", webId));
            LOG.error(e);
        }

        if (company != null) {
            long companyId = company.getCompanyId();
            long groupId = determineGroupId(siteFriendlyURL, companyGroupId, companyId);
            byte[] bytes = null;
            try {
                bytes = getBytesFromFile(fileUrl, bundle);
            } catch (IOException e) {
                LOG.error("IOException while getting bytes from bundle ", e);
            }

            FileEntry fileEntry = null;
            try {

                fileEntry = dlAppLocalService.getFileEntry(groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, title);
            } catch (NoSuchFolderException e) {
                LOG.info(String.format("No folder with key %s found while retrieving document %s in group %s",
                        DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, title, groupId));
            } catch (PortalException e) {
                LOG.error(String.format("PortalException while retrieving document %s in group %s", title, groupId), e);
            }

            String sourceFileName = title;
            String description = title;
            String mimeType = URLConnection.guessContentTypeFromName(fileUrl);
            if (fileEntry == null) {
                LOG.info(String.format("Document %s does not exist, adding new document", title));
                try {
                    fileEntry = dlAppLocalService.addFileEntry(userId, groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, sourceFileName, mimeType,
                            title, description, "", bytes, new ServiceContext());
                    LOG.info(String.format("Document %s added successfully in group %s", title, groupId));
                } catch (PortalException e) {
                    LOG.error("PortalException while adding document, document not added ", e);
                }
            } else {
                try {
                    fileEntry = dlAppLocalService.updateFileEntry(userId, fileEntry.getFileEntryId(), sourceFileName, mimeType, title, description,
                            "", false, bytes, new ServiceContext());
                    LOG.info(String.format("Document %s upadted successfully in group %s", title, groupId));
                } catch (PortalException e) {
                    LOG.error("PortalException while updating document, document not updated ", e);
                }
            }

            // in the first parameter here we need to use DLFileEntry and not
            // FileEntry, because the resource permission API works like that
            if (fileEntry != null) {
                setPermissions(DLFileEntry.class.getName(), fileEntry.getFileEntryId(), fileEntry.getCompanyId());
                try {
                    AssetEntry assetEntry = AssetEntryLocalServiceUtil.updateEntry(userId, groupId, FileEntry.class.getName(),
                            fileEntry.getFileEntryId(), new long[0], new String[0]);
                    assetEntry.setPublishDate(new Date());
                    assetEntry.setVisible(true);
                    assetEntry.setTitle(title);
                    AssetEntryLocalServiceUtil.updateAssetEntry(assetEntry);
                    LOG.info(String.format("Asset Entry updated for Document %s group %s", title, groupId));
                } catch (PortalException e) {
                    LOG.error("PortalException while creating assetEntry for document, assetEntry not created ", e);
                }
            }

        }

    }

    @Override
    public void deleteDocument(String[] webIds, String title) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                deleteDocumentInCompany(webId, title);
            }
        } else {
            deleteDocumentInCompany(defaultValue.getDefaultCompany().getWebId(), title);
        }

    }

    private void deleteDocumentInCompany(String webId, String title) {
        try {
            Company company = companyService.getCompanyByWebId(webId);
            FileEntry fileEntry = dlAppLocalService.getFileEntry(company.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, title);
            if (fileEntry != null) {
                dlAppLocalService.deleteFileEntry(fileEntry.getFileEntryId());
                LOG.info(String.format("Document with title %s deleted from group %s", title, company.getGroupId()));
            } else {
                LOG.info(String.format("Document with title %s doesn't exists so deletion was not possible", title));
            }
        } catch (PortalException e) {
            LOG.error("PortalException while deleting document " + e);
        }
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

    private void setPermissions(String className, long fileEntryId, long companyId) {
        Role role;
        try {
            Resource resource =
                    ResourceLocalServiceUtil.getResource(companyId, className, ResourceConstants.SCOPE_INDIVIDUAL, Long.toString(fileEntryId));
            role = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);
            resourcePermissionLocalService.setResourcePermissions(companyId, className, ResourceConstants.SCOPE_INDIVIDUAL, resource.getPrimKey(),
                    role.getRoleId(), new String[] {ActionKeys.VIEW});
        } catch (PortalException e) {
            LOG.error("PortalException while setting document resource permissions, permissions are not set ", e);
        }
    }

    private byte[] getBytesFromFile(String fileUrl, Bundle bundle) throws IOException {
        URL url = bundle.getResource(fileUrl);
        byte[] bytes = null;
        if (url != null) {
            bytes = extract(url.openStream());
        } else {
            File script = new File(Constants.TEMP_LAM_SUBDIR + StringPool.SLASH + fileUrl);
            InputStream is = new FileInputStream(script);
            bytes = extract(is);
        }

        return bytes;
    }

    static byte[] extract(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];

        int read = 0;
        while ((read = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();

        return baos.toByteArray();
    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
