package nl.finalist.liferay.lam.api;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;

public class RoleAndPermissionsImplTest {

	@Mock
	private Role role;
	@Mock
	private RoleLocalService roleLocalService;
	@Mock
	private ResourcePermissionLocalService resourcePermissionLocalService;
	@InjectMocks
	private RoleAndPermissionsImpl rolesAndPermission;

	@Before
	public void setUp() throws PortalException {
		rolesAndPermission = new RoleAndPermissionsImpl();
		initMocks(this);
	}

	@Test
	public void testAddCustomRoleAndPermissions() throws PortalException {
		Map<Locale, String> title = new HashMap<>();
		title.put(Locale.ENGLISH, "title");

		Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title,
				TypeOfRole.REGULARROLES.getValue(), null, null)).thenReturn(role);
		boolean result = rolesAndPermission.addCustomRoleAndPermission("MockRole", 1L, TypeOfRole.REGULARROLES, title,
				title, 1L, new String[] { ActionKeys.ACCESS }, User.class.getName());
		assertTrue(result);
	}

	@Test
	public void testFailCustomRoleAddition() throws PortalException {
		Map<Locale, String> title = new HashMap<>();
		title.put(Locale.ENGLISH, "title");

		Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title,
				TypeOfRole.REGULARROLES.getValue(), null, null)).thenThrow(new PortalException());
		boolean result = rolesAndPermission.addCustomRoleAndPermission("MockRole", 1L, TypeOfRole.REGULARROLES, title,
				title, 1L, new String[] { ActionKeys.ACCESS }, User.class.getName());
		assertFalse(result);
	}

	@Test
	public void testFailPermissionsAddition() throws PortalException {
		Map<Locale, String> title = new HashMap<>();
		title.put(Locale.ENGLISH, "title");
		role.setRoleId(1L);
		Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title,
				TypeOfRole.REGULARROLES.getValue(), null, null)).thenReturn(role);
		doThrow(new PortalException()).when(resourcePermissionLocalService).addResourcePermission(1L,
				User.class.getName(), ResourceConstants.SCOPE_COMPANY, "1", 0L, ActionKeys.ACCESS);
		boolean result = rolesAndPermission.addCustomRoleAndPermission("MockRole", 1L, TypeOfRole.REGULARROLES, title,
				title, 1L, new String[] { ActionKeys.ACCESS }, User.class.getName());
		assertFalse(result);
	}

	@Test
	public void testFailSecondPermissionsAddition() throws PortalException {
		Map<Locale, String> title = new HashMap<>();
		title.put(Locale.ENGLISH, "title");
		role.setRoleId(1L);
		Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title,
				TypeOfRole.REGULARROLES.getValue(), null, null)).thenReturn(role);
		doNothing().when(resourcePermissionLocalService).addResourcePermission(1L, User.class.getName(),
				ResourceConstants.SCOPE_COMPANY, "1", 0L, ActionKeys.ACCESS);
		doThrow(new PortalException()).when(resourcePermissionLocalService).addResourcePermission(1L,
				User.class.getName(), ResourceConstants.SCOPE_COMPANY, "1", 0L, ActionKeys.VIEW);
		boolean result = rolesAndPermission.addCustomRoleAndPermission("MockRole", 1L, TypeOfRole.REGULARROLES, title,
				title, 1L, new String[] { ActionKeys.ACCESS, ActionKeys.VIEW }, User.class.getName());
		assertFalse(result);
	}

	@Test
	public void testNoPermissionsAddition() throws PortalException {
		Map<Locale, String> title = new HashMap<>();
		title.put(Locale.ENGLISH, "title");
		role.setRoleId(1L);
		Mockito.when(roleLocalService.addRole(1L, null, 0L, "MockRole", title, title,
				TypeOfRole.REGULARROLES.getValue(), null, null)).thenReturn(role);
		boolean result = rolesAndPermission.addCustomRoleAndPermission("MockRole", 1L, TypeOfRole.REGULARROLES, title,
				title, 1L, new String[] {}, User.class.getName());
		assertTrue(result);
	}
}