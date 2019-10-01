package nl.finalist.liferay.lam.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;

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
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PortalUtil.class})
public class RoleAndPermissionsImplTest {

    @Mock
    private Role role;

    @Mock
    private RoleLocalService roleLocalService;

    @Mock
    private ResourcePermissionLocalService resourcePermissionLocalService;

    @Mock
    private UserLocalService userLocalService;

    @InjectMocks
    private RoleAndPermissionsImpl rolesAndPermission;

    @Before
    public void setUp() throws PortalException {
        rolesAndPermission = new RoleAndPermissionsImpl();
        PowerMockito.mockStatic(PortalUtil.class);
        PowerMockito.when(PortalUtil.getDefaultCompanyId()).thenReturn(1L);
        initMocks(this);
        when(userLocalService.getDefaultUserId(anyLong())).thenReturn(1L);
    }

    @Test
    public void testAddCustomRoleAndPermissions() throws PortalException {
        Map<Locale, String> title = new HashMap<>();
        title.put(Locale.ENGLISH, "title");
        Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title, TypeOfRole.REGULARROLES.getValue(), null, null))
               .thenReturn(role);
        boolean result =
                rolesAndPermission.addCustomRoleAndPermission(null, "MockRole", TypeOfRole.REGULARROLES, title, title, getUserAccessPermissions());
        assertTrue(result);
    }

    private Map<String, List<String>> getUserAccessPermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        List<String> actionIds = new ArrayList<>();
        actionIds.add(ActionKeys.ACCESS);

        permissions.put(User.class.getName(), actionIds);
        return permissions;
    }

    private Map<String, List<String>> getUserAccessAndViewPermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        List<String> actionIds = new ArrayList<>();
        actionIds.add(ActionKeys.ACCESS);
        actionIds.add(ActionKeys.VIEW);

        permissions.put(User.class.getName(), actionIds);
        return permissions;
    }

    private Map<String, List<String>> getUserNoPermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        List<String> actionIds = new ArrayList<>();

        permissions.put(User.class.getName(), actionIds);
        return permissions;
    }

    @Test
    public void testFailCustomRoleAddition() throws PortalException {
        Map<Locale, String> title = new HashMap<>();
        title.put(Locale.ENGLISH, "title");

        Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title, TypeOfRole.REGULARROLES.getValue(), null, null))
               .thenThrow(new PortalException());
        boolean result =
                rolesAndPermission.addCustomRoleAndPermission(null, "MockRole", TypeOfRole.REGULARROLES, title, title, getUserAccessPermissions());
        assertFalse(result);
    }

    @Test
    public void testFailPermissionsAddition() throws PortalException {
        Map<Locale, String> title = new HashMap<>();
        title.put(Locale.ENGLISH, "title");
        role.setRoleId(1L);
        Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title, TypeOfRole.REGULARROLES.getValue(), null, null))
               .thenReturn(role);
        doThrow(new PortalException()).when(resourcePermissionLocalService)
                                      .addResourcePermission(1L, User.class.getName(), ResourceConstants.SCOPE_COMPANY, "1", 0L, ActionKeys.ACCESS);
        boolean result =
                rolesAndPermission.addCustomRoleAndPermission(null, "MockRole", TypeOfRole.REGULARROLES, title, title, getUserAccessPermissions());
        assertFalse(result);
    }

    @Test
    public void testFailSecondPermissionsAddition() throws PortalException {
        Map<Locale, String> title = new HashMap<>();
        title.put(Locale.ENGLISH, "title");
        role.setRoleId(1L);
        Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title, TypeOfRole.REGULARROLES.getValue(), null, null))
               .thenReturn(role);
        doNothing().when(resourcePermissionLocalService)
                   .addResourcePermission(1L, User.class.getName(), ResourceConstants.SCOPE_COMPANY, "1", 0L, ActionKeys.ACCESS);
        doThrow(new PortalException()).when(resourcePermissionLocalService)
                                      .addResourcePermission(1L, User.class.getName(), ResourceConstants.SCOPE_COMPANY, "1", 0L, ActionKeys.VIEW);
        boolean result = rolesAndPermission.addCustomRoleAndPermission(null, "MockRole", TypeOfRole.REGULARROLES, title, title,
                getUserAccessAndViewPermissions());
        assertFalse(result);
    }

    @Test
    public void testNoPermissionsAddition() throws PortalException {
        Map<Locale, String> title = new HashMap<>();
        title.put(Locale.ENGLISH, "title");
        role.setRoleId(1L);
        Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title, TypeOfRole.REGULARROLES.getValue(), null, null))
               .thenReturn(role);
        boolean result =
                rolesAndPermission.addCustomRoleAndPermission(null, "MockRole", TypeOfRole.REGULARROLES, title, title, getUserNoPermissions());
        assertTrue(result);
    }
}