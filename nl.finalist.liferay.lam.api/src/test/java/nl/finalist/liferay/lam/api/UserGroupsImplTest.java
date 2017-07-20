package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserGroupsImpl.class, PropsUtil.class })
public class UserGroupsImplTest {

    @Mock
    private CompanyLocalService companyService;
    @Mock
    private UserLocalService userService;
    @Mock
    private Company mockCompany;
    @Mock
    private User mockDefaultUser;
    @Mock
    private ServiceContext mockServiceContext;
    @Mock
    private UserGroupLocalService userGroupService;
    @InjectMocks
    private UserGroupsImpl userGroupsImpl;

    @Before
    public void setUp() {
        userGroupsImpl = new UserGroupsImpl();
        PowerMockito.mockStatic(PropsUtil.class);
        PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
        initMocks(this);
    }

    @Test
    public void testAddUserGroup() throws Exception {
        String groupName = "testName";
        String description = "some description";
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(mockCompany.getDefaultUser()).thenReturn(mockDefaultUser);
        when(mockDefaultUser.getUserId()).thenReturn(10L);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);

        userGroupsImpl.addUserGroup(groupName, description);

        verify(userGroupService).addUserGroup(10L, 1L, groupName, description, mockServiceContext);
    }
}
