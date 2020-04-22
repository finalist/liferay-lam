package nl.finalist.liferay.lam.api;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.ContactLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({DefaultValueImpl.class, LocaleUtil.class})
public class UserImplTest {

    @Mock
    private DefaultValue defaultValue;

    @Mock
    private UserLocalService userLocalService;

    @Mock
    private GroupLocalService groupLocalService;

    @Mock
    private UserGroupLocalService usergroupLocalService;

    @Mock
    private RoleLocalService roleLocalService;

    @Mock
    private ContactLocalService contactLocalService;

    @Mock
    private CustomFields customFieldsService;

    @Mock
    private Company company;

    @Mock
    private Role role;

    @Mock
    private com.liferay.portal.kernel.model.User mockUser;

    @Mock
    private Group group;

    @Mock
    private UserGroup usergroup;

    @InjectMocks
    private UserImpl user;

    Locale locale;

    private static final long USER_ID = 1L;

    private static final long COMPANY_ID = 1L;

    @Before
    public void setUp() throws PortalException {
        user = new UserImpl();
        PowerMockito.mockStatic(LocaleUtil.class);
        initMocks(this);

        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(defaultValue.getDefaultCompany()).thenReturn(company);
        when(company.getCompanyId()).thenReturn(COMPANY_ID);
        locale = new Locale("en_US");
        PowerMockito.when(LocaleUtil.getDefault()).thenReturn(locale);
    }

    @Test
    public void createUser_shouldAddUser() throws PortalException {
        user.createUser(null, "tester", "tester@test.test", "Theo", "Tester", null, null, null, null);

        verify(userLocalService, times(1)).addUser(eq(USER_ID), eq(COMPANY_ID), eq(true), eq(null), eq(null), eq(false), eq("tester"),
                eq("tester@test.test"), eq(0L), eq(null), eq(locale), eq("Theo"), eq(null), eq("Tester"), eq(0L), eq(0L), eq(true), eq(1), eq(1),
                eq(1970), eq(null), eq(null), eq(null), eq(null), eq(null), eq(true), any(ServiceContext.class));
    }

    @Test
    public void createUser_shouldAddCustomFields_whenCustomFieldsHaveBeenSpecified() throws PortalException {
        Map<String, String> customFields = new HashMap<>();
        customFields.put("test", "test");
        when(mockUser.getPrimaryKey()).thenReturn(USER_ID);
        when(userLocalService.addUser(anyLong(), anyLong(), anyBoolean(), any(), any(), anyBoolean(), anyString(), anyString(), anyLong(), any(),
                any(), anyString(), any(), anyString(), anyLong(), anyLong(), anyBoolean(), anyInt(), anyInt(), anyInt(), any(), any(), any(), any(),
                any(), anyBoolean(), any())).thenReturn(mockUser);

        user.createUser(null, "tester", "tester@test.test", "Theo", "Tester", null, null, null, customFields);

        verify(userLocalService, times(1)).addUser(anyLong(), anyLong(), anyBoolean(), any(), any(), anyBoolean(), anyString(), anyString(),
                anyLong(), any(), any(), anyString(), any(), anyString(), anyLong(), anyLong(), anyBoolean(), anyInt(), anyInt(), anyInt(), any(),
                any(), any(), any(), any(), anyBoolean(), any());
        verify(customFieldsService, times(1)).addCustomFieldValue(null, com.liferay.portal.kernel.model.User.class.getName(), "test", USER_ID,
                "test");
    }

    @Test
    public void updateUser_shouldOnlyUpdateUser_whenAllOtherFieldsAreEmpty() {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);

        user.updateUser(null, "tester", "tester2", "tester@test.test", "Theo", "Tester", null, null, null, null);

        verify(userLocalService, times(1)).updateUser(any());
        verify(mockUser, times(1)).setScreenName(any());
        verify(mockUser, times(1)).setEmailAddress(any());
        verify(mockUser, times(1)).setFirstName(any());
        verify(mockUser, times(1)).setLastName(any());
    }

    @Test
    public void updateUser_shouldDoNothing_whenUserDoesntExist() {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(null);

        user.updateUser(null, "tester", "tester2", "tester@test.test", "Theo", "Tester", null, null, null, null);

        verify(userLocalService, never()).updateUser(any());
    }

    @Test
    public void updateUser_shouldUpdateGroups_whenGroupsAreGiven() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(USER_ID);
        when(groupLocalService.fetchFriendlyURLGroup(COMPANY_ID, "TestGroup")).thenReturn(group);
        when(group.getGroupId()).thenReturn(1L);
        String[] groups = {"TestGroup"};

        user.updateUser(null, "tester", "tester2", "tester@test.test", "Theo", "Tester", null, groups, null, null);

        verify(userLocalService, times(1)).updateUser(any());
        long[] groupIds = {1L};
        verify(userLocalService, times(1)).updateGroups(eq(USER_ID), eq(groupIds), any());
    }

    @Test
    public void updateUser_shouldUpdateRoles_whenRolesAreGiven() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(USER_ID);
        when(roleLocalService.fetchRole(COMPANY_ID, "TestRole")).thenReturn(role);
        when(role.getRoleId()).thenReturn(1L);
        String[] roles = {"TestRole"};

        user.updateUser(null, "tester", "tester2", "tester@test.test", "Theo", "Tester", roles, null, null, null);

        verify(userLocalService, times(1)).updateUser(any());
        long[] roleIds = {1L};
        verify(roleLocalService, times(1)).addUserRoles(eq(USER_ID), eq(roleIds));
    }

    @Test
    public void updateUser_shouldUpdateUserGroups_whenUserGroupsAreGiven() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);
        when(mockUser.getUserId()).thenReturn(USER_ID);
        when(usergroupLocalService.fetchUserGroup(COMPANY_ID, "TestGroup")).thenReturn(usergroup);
        when(usergroup.getUserGroupId()).thenReturn(1L);
        String[] groups = {"TestGroup"};

        user.updateUser(null, "tester", "tester2", "tester@test.test", "Theo", "Tester", null, null, groups, null);

        verify(userLocalService, times(1)).updateUser(any());
        long[] groupIds = {1L};
        verify(usergroupLocalService, times(1)).addUserUserGroups(eq(USER_ID), eq(groupIds));
    }

    @Test
    public void updateUser_shouldUpdateCustomFields_whenCustomFieldsAreGiven() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);
        when(mockUser.getPrimaryKey()).thenReturn(USER_ID);

        Map<String, String> customFields = new HashMap<>();
        customFields.put("test", "test");
        user.updateUser(null, "tester", "tester2", "tester@test.test", "Theo", "Tester", null, null, null, customFields);

        verify(userLocalService, times(1)).updateUser(any());
        verify(customFieldsService, times(1)).updateCustomFieldValue(null, com.liferay.portal.kernel.model.User.class.getName(), "test", USER_ID,
                "test");
    }

    @Test
    public void updateUser_shouldNotUpdateEmptyFields() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);

        user.updateUser(null, "tester", null, null, null, null, null, null, null, null);

        verify(userLocalService, times(1)).updateUser(any());
        verify(mockUser, never()).setScreenName(any());
        verify(mockUser, never()).setEmailAddress(any());
        verify(mockUser, never()).setFirstName(any());
        verify(mockUser, never()).setLastName(any());
    }

    @Test
    public void deleteUser_shouldDeleteUser() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(mockUser);

        user.deleteUser(null, "tester");

        verify(userLocalService, times(1)).deleteUser(mockUser);
    }

    @Test
    public void deleteUser_shouldDoNothing_whenUserDoesntExist() throws PortalException {
        when(userLocalService.fetchUserByScreenName(COMPANY_ID, "tester")).thenReturn(null);

        user.deleteUser(null, "tester");

        verify(userLocalService, never()).deleteUser(mockUser);
    }
}
