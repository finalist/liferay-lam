package nl.finalist.liferay.lam.api;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.exportimport.kernel.service.StagingLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.finalist.liferay.lam.api.model.PageModel;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocaleUtil.class, SiteImpl.class, PropsUtil.class, Locale.class, PortalUtil.class, DefaultValueImpl.class})
public class SiteImplTest {

    private static final long SITE_ID = 123L;

    private static final long USER_ID = 10L;

    private static final long GROUP_ID = 1L;

    @Mock
    private GroupLocalService siteService;

    @Mock
    private UserLocalService userService;

    @Mock
    private CounterLocalService counterService;

    @Mock
    private StagingLocalService stagingLocalService;

    @Mock
    private Group mockSite;

    @Mock
    private DefaultValue defaultValue;

    @Mock
    private HashMap<Locale, String> mockTitleMap;

    @Mock
    private ServiceContext mockServiceContext;

    @Mock
    private CustomFields customFieldsService;

    @Mock
    private Page pageService;

    @Mock
    private Layout pageLayout;

    @InjectMocks
    private SiteImpl siteImpl;

    @Mock
    private ServiceContext serviceContext;

    Map<Locale, String> nameMap;

    Map<Locale, String> descriptionMap;

    String friendlyURL;

    String siteKey;

    @Before
    public void setUp() throws PortalException {
        siteImpl = new SiteImpl();
        PowerMockito.mockStatic(LocaleUtil.class);
        PowerMockito.mockStatic(PropsUtil.class);
        PowerMockito.mockStatic(PortalUtil.class);

        initMocks(this);

        siteKey = "testName";
        nameMap = new HashMap<>();
        nameMap.put(Locale.US, siteKey);
        descriptionMap = new HashMap<>();
        descriptionMap.put(Locale.US, "testDescription");
        friendlyURL = "/test";
        mockSite.setGroupId(GROUP_ID);

    }

    @Test
    public void testAddSite() throws Exception {
        Locale mockLocale = new Locale("en_US");
        PowerMockito.when(LocaleUtil.getSiteDefault()).thenReturn(mockLocale);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        siteImpl.addSite(null, nameMap, descriptionMap, friendlyURL, null, null);

        verify(siteService).addGroup(USER_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID,
                nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false,
                true, null);
    }

    @Test
    public void testAddSiteWithCustomField() throws Exception {
        Map<String, String> customFields = createCustomFields();
        Locale mockLocale = new Locale("en_US");
        PowerMockito.when(LocaleUtil.getSiteDefault()).thenReturn(mockLocale);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);

        when(siteService.addGroup(USER_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID,
                nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false,
                true, null)).thenReturn(mockSite);
        when(mockSite.getPrimaryKey()).thenReturn(1L);

        siteImpl.addSite(null, nameMap, descriptionMap, friendlyURL, customFields, null);

        verify(siteService).addGroup(USER_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID,
                nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false,
                true, null);
        verify(customFieldsService).addCustomFieldValue(null, Group.class.getName(), "someField", 1L, "someValue");
    }

    @Test
    public void testAddSiteWithPages() throws Exception {
        List<PageModel> pages = createListWithTwoPages();

        Locale mockLocale = new Locale("en_US");
        PowerMockito.when(LocaleUtil.getSiteDefault()).thenReturn(mockLocale);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);

        when(siteService.addGroup(USER_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID,
                nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false,
                true, null)).thenReturn(mockSite);
        when(mockSite.getGroupId()).thenReturn(1L);

        siteImpl.addSite(null, nameMap, descriptionMap, friendlyURL, null, pages);

        verify(siteService).addGroup(USER_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID,
                nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false,
                true, null);
    }

    @Test
    public void testDeleteExistingSite() throws PortalException {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        siteImpl.deleteSite(null, siteKey);

        verify(siteService).deleteGroup(SITE_ID);
    }

    @Test
    public void testUpdateSiteTranslation() throws PortalException {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);

        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);
        List<PageModel> pages = createListWithOnePage();
        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, null, pages, false);

        verify(siteService).updateGroup(SITE_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true,
                GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false, true, null);
    }

    @Test
    public void testUpdateSiteTranslationWithCustomFields() throws PortalException {

        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        when(siteService.updateGroup(SITE_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true,
                GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false, true, null)).thenReturn(mockSite);
        when(mockSite.getPrimaryKey()).thenReturn(1L);

        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, createCustomFields(), createListWithOnePage(), false);

        verify(siteService).updateGroup(SITE_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true,
                GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false, true, null);
        verify(customFieldsService).updateCustomFieldValue(null, Group.class.getName(), "someField", 1L, "someValue");
    }

    @Test
    public void testEnableStagingForSite() throws Exception {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        when(mockSite.hasStagingGroup()).thenReturn(false);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(serviceContext);

        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, null, new ArrayList<PageModel>(), true);

        verify(stagingLocalService).enableLocalStaging(USER_ID, mockSite, false, false, serviceContext);
    }

    @Test
    public void testEnableStagingAttemptForStagedSite() throws Exception {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        when(mockSite.hasStagingGroup()).thenReturn(true);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(serviceContext);

        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, null, new ArrayList<PageModel>(), true);

        verify(stagingLocalService, never()).enableLocalStaging(USER_ID, mockSite, false, false, serviceContext);
    }

    @Test
    public void testDisableStagingForSite() throws Exception {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        when(mockSite.hasStagingGroup()).thenReturn(true);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(serviceContext);

        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, null, new ArrayList<PageModel>(), false);

        verify(stagingLocalService).disableStaging(mockSite, serviceContext);
    }

    @Test
    public void testDisableStagingAttemptForNonStagedSite() throws Exception {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        when(mockSite.hasStagingGroup()).thenReturn(false);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(serviceContext);

        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, null, new ArrayList<PageModel>(), false);

        verify(stagingLocalService, never()).disableStaging(mockSite, serviceContext);
    }

    private Map<String, String> createCustomFields() {
        Map<String, String> customFields = new HashMap<>();
        customFields.put("someField", "someValue");
        return customFields;
    }

    @Test
    public void updatePageOfSite() throws PortalException {
        when(siteService.getGroup(0L, siteKey)).thenReturn(mockSite);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockSite.getGroupId()).thenReturn(SITE_ID);

        when(siteService.updateGroup(SITE_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true,
                GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false, true, null)).thenReturn(mockSite);

        pageLayout.setFriendlyURL(friendlyURL);
        pageLayout.setGroupId(SITE_ID);
        when(pageService.fetchLayout(SITE_ID, false, friendlyURL)).thenReturn(pageLayout);

        siteImpl.updateSite(null, siteKey, nameMap, descriptionMap, friendlyURL, createCustomFields(), createListWithOnePage(), false);

        verify(pageService).updatePage(any(), anyLong(), any(PageModel.class));
        verify(pageService).updatePage(any(), anyLong(), any(PageModel.class));

    }

    private List<PageModel> createListWithTwoPages() {
        List<PageModel> pages = new ArrayList<>();
        pages.add(createPage());
        pages.add(createChildPage());
        return pages;
    }

    private List<PageModel> createListWithOnePage() {
        List<PageModel> pages = new ArrayList<>();
        pages.add(createPage());
        return pages;
    }

    private PageModel createPage() {
        Map<Locale, String> testLocaleMap = new HashMap<>();
        testLocaleMap.put(Locale.US, "test");
        Map<String, String> testStringMap = new HashMap<String, String>();
        testStringMap.put(Locale.US.toString(), "friendlyName");
        PageModel page = new PageModel(null, "", true, testStringMap, testLocaleMap, testLocaleMap, createFriendlyUrlMap(), StringPool.BLANK,
                createCustomFields(), null, "portlet", null);
        return page;
    }

    private Map<String, String> createFriendlyUrlMap() {
        Map<String, String> testUrlMap = new HashMap<>();
        testUrlMap.put(Locale.US.toString(), friendlyURL);
        return testUrlMap;
    }

    private PageModel createChildPage() {
        Map<Locale, String> testLocaleMap = new HashMap<>();
        testLocaleMap.put(Locale.US, "test");
        Map<String, String> testStringMap = new HashMap<String, String>();
        testStringMap.put(Locale.US.toString(), "friendlyChildName");
        Map<String, String> testUrlMap = createFriendlyUrlMap();
        PageModel page = new PageModel(null, "", true, testStringMap, testLocaleMap, testLocaleMap, testUrlMap, StringPool.BLANK,
                createCustomFields(), "/friendlyNameus", "portlet", null);
        return page;
    }
}
