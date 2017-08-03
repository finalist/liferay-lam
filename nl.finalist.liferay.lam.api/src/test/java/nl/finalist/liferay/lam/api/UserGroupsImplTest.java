package nl.finalist.liferay.lam.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PropsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserGroupsImpl.class, PropsUtil.class})
public class UserGroupsImplTest {
private static final Long USER_ID = 10L;
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
    @Mock
    private CustomFields customFieldsService;
    @Mock
	private UserGroup mockUserGroup;
	@Mock
	private DefaultValue defaultValue;
	
    @InjectMocks
    private UserGroupsImpl userGroupsImpl;
    
    @Before
    public void setUp() {
        userGroupsImpl = new UserGroupsImpl();
        PowerMockito.mockStatic(PropsUtil.class);
        PowerMockito.mockStatic(DefaultValueImpl.class);
        initMocks(this);
    }

    @Test
    public void testAddUserGroupWithoutCustomFields() throws Exception {
        String groupName = "testName";
        String description = "some description";
        when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);

        userGroupsImpl.addUserGroup(groupName, description, null);

        verify(userGroupService).addUserGroup(10L, 1L, groupName, description, mockServiceContext);
    }

    @Test
    public void testAddUserGroupWithCustomFields() throws Exception {
        String groupName = "testName";
        String description = "some description";
        Map<String,String> customFields = new HashMap<>();
        customFields.put("someField", "someValue");
       
        when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);
        
        when(userGroupService.addUserGroup(10L, 1L, groupName, description, mockServiceContext)).thenReturn(mockUserGroup);
        when(mockUserGroup.getPrimaryKey()).thenReturn(1L);

        userGroupsImpl.addUserGroup(groupName, description, customFields);

        verify(userGroupService).addUserGroup(USER_ID, 1L, groupName, description, mockServiceContext);
        verify(customFieldsService).addCustomFieldValue(UserGroup.class.getName(), "someField", 1L, "someValue");
    }
    
}
