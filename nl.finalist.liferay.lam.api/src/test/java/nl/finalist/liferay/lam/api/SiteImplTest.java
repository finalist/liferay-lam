package nl.finalist.liferay.lam.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
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

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocaleUtil.class, SiteImpl.class, PropsUtil.class, Locale.class, PortalUtil.class })
public class SiteImplTest {

    private static final long USER_ID = 10L;
	private static final long COMPANY_ID = 1L;
	@Mock
    private CompanyLocalService companyService;
    @Mock
    private GroupLocalService siteService;
    @Mock
    private UserLocalService userService;
    @Mock
    private CounterLocalService counterService;
    @Mock
    private Company mockCompany;
    @Mock
    private Group mockSite;
    @Mock
    private User mockDefaultUser;
    @Mock
    private HashMap<Locale, String> mockTitleMap;
    @Mock
    private ServiceContext mockServiceContext;
    @InjectMocks
    private SiteImpl siteImpl;

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
        PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
        PowerMockito.when(PortalUtil.getDefaultCompanyId()).thenReturn(COMPANY_ID);
        initMocks(this);
        
        siteKey = "testName";
        nameMap = new HashMap<>();
        nameMap.put(Locale.US, siteKey);
        descriptionMap = new HashMap<>();
        descriptionMap.put(Locale.US, "testDescription");
        friendlyURL = "/test";
        
    }

    @Test
    public void testAddSite() throws Exception {
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(COMPANY_ID);
        Locale mockLocale = new Locale("en_US");
        PowerMockito.when(LocaleUtil.getSiteDefault()).thenReturn(mockLocale);
        when(mockCompany.getDefaultUser()).thenReturn(mockDefaultUser);
        when(mockDefaultUser.getUserId()).thenReturn(USER_ID);
        siteImpl.addSite(nameMap, descriptionMap, friendlyURL);

        verify(siteService).addGroup(USER_ID, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), 0L, GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, false, true, null);
    }

    @Test
    public void testDeleteExistingSite() throws PortalException {
        when(siteService.getGroup(COMPANY_ID, siteKey)).thenReturn(mockSite);
        when(mockSite.getGroupId()).thenReturn(123L);

        siteImpl.deleteSite(siteKey);

        verify(siteService).deleteGroup(123L);
    }

    @Test
    public void testUpdateSiteTranslation() throws PortalException {
    	when(siteService.getGroup(COMPANY_ID, siteKey)).thenReturn(mockSite);
        when(mockSite.getGroupId()).thenReturn(123L);

        siteImpl.updateSite(siteKey, nameMap, descriptionMap, friendlyURL);

        verify(siteService).updateGroup(123L, GroupConstants.DEFAULT_PARENT_GROUP_ID, nameMap, descriptionMap, GroupConstants.TYPE_SITE_OPEN, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, true, true, null);
    }

}
