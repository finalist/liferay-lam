package nl.finalist.liferay.lam.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.osgi.framework.Bundle;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AssetEntryLocalServiceUtil.class, RoleLocalServiceUtil.class, ResourceLocalServiceUtil.class, DocumentImpl.class})
public class DocumentImplTest {
    private static final long USER_ID = 10L;
    private static final long GLOBAL_GROUP_ID = 10L;

    @Mock
    private DefaultValue defaultValue;
    @Mock
    private Company company;
    @Mock
    private User user;
    @Mock
    private Role role;
    @Mock
    private Group group;
    @Mock
    private Resource resource;
    @Mock
    private FileEntry fileEntry;
    @Mock
    private AssetEntry assetEntry;
    @Mock
    private DLAppLocalService dlAppLocalService;
    @Mock
    private GroupLocalService groupLocalService;
    @Mock
    private ResourcePermissionLocalService resourcePermissionLocalService;
    @Mock
    private ServiceContext serviceContext;
    @Mock
    private Bundle bundle ;

    @InjectMocks
    private DocumentImpl documentImpl;

    String title;
    String fileUrl;
    String siteFriendlyURL;
    
    @Before
    public void setUp() {
        documentImpl = new DocumentImpl();
        PowerMockito.mockStatic(AssetEntryLocalServiceUtil.class);
        PowerMockito.mockStatic(RoleLocalServiceUtil.class);
        PowerMockito.mockStatic(ResourceLocalServiceUtil.class);
        
        initMocks(this);

        title = "Unibank";
        fileUrl = "/unibank.png";
        siteFriendlyURL = "/test";
    }

    @Test
    public void testAddNewDocument() throws Exception {
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(defaultValue.getGlobalGroupId()).thenReturn(GLOBAL_GROUP_ID);
        when(defaultValue.getDefaultCompany()).thenReturn(company);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(serviceContext);
        when(company.getCompanyId()).thenReturn(1L);

        URL url = this.getClass().getResource(fileUrl);
        byte[] bytes = DocumentImpl.extract(url.openStream());
        when(bundle.getResource(fileUrl)).thenReturn(url);

        when(groupLocalService.fetchFriendlyURLGroup(company.getCompanyId(), siteFriendlyURL)).thenReturn(group);
        when(group.getGroupId()).thenReturn(GLOBAL_GROUP_ID);

        when(dlAppLocalService.getFileEntry(GLOBAL_GROUP_ID, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, title)).thenReturn(null);
        when(dlAppLocalService.addFileEntry(
                USER_ID,
                GLOBAL_GROUP_ID,
                DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, 
                title, 
                "image/png", 
                title, 
                title, 
                "",
                bytes,
                serviceContext)).thenReturn(fileEntry);

        when(fileEntry.getFileEntryId()).thenReturn(1L);
        when(fileEntry.getCompanyId()).thenReturn(1L);

        PowerMockito.when(ResourceLocalServiceUtil.getResource(company.getCompanyId(), DLFileEntry.class.getName(),
                ResourceConstants.SCOPE_INDIVIDUAL, Long.toString(fileEntry.getFileEntryId()))).thenReturn(resource);
        when(resource.getPrimKey()).thenReturn("primkey");
        PowerMockito.when(RoleLocalServiceUtil.getRole(company.getCompanyId(), RoleConstants.GUEST)).thenReturn(role);
        when(role.getRoleId()).thenReturn(1L);
        
        PowerMockito.when(AssetEntryLocalServiceUtil.updateEntry(USER_ID, GLOBAL_GROUP_ID, FileEntry.class.getName(),
                1L, new long[0], new String[0])).thenReturn(assetEntry);
        PowerMockito.when(AssetEntryLocalServiceUtil.updateAssetEntry(assetEntry)).thenReturn(assetEntry);
        
        documentImpl.createOrUpdateDocument(siteFriendlyURL, title, fileUrl, bundle);        
        
        verify(dlAppLocalService).addFileEntry(
                defaultValue.getDefaultUserId(),
                group.getGroupId(),
                DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, 
                title, 
                "image/png", 
                title, 
                title, 
                "",
                bytes,
                serviceContext);
    }

    @Test
    public void testDeleteDocument() throws Exception {
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(defaultValue.getGlobalGroupId()).thenReturn(GLOBAL_GROUP_ID);
        when(defaultValue.getDefaultCompany()).thenReturn(company);
        when(company.getCompanyId()).thenReturn(1L);

        when(dlAppLocalService.getFileEntry(GLOBAL_GROUP_ID, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, title)).thenReturn(fileEntry);
        when(fileEntry.getFileEntryId()).thenReturn(1L);

        documentImpl.deleteDocument(title);
        
        verify(dlAppLocalService).deleteFileEntry(1L);
    }

    @Test
    public void testGuess() throws IOException {
        String mime = URLConnection.guessContentTypeFromName("unibank.png");
        assertEquals(mime, "image/png");
    }

    @Test
    public void testProbe() throws URISyntaxException, IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Path path = Paths.get(classLoader.getResource("unibank.png").toURI());
        String mime = Files.probeContentType(path);
        assertEquals(mime, "image/png");
    }
}
