package nl.finalist.liferay.lam.api;


import com.liferay.expando.kernel.exception.NoSuchTableException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
@RunWith(PowerMockRunner.class)
@PrepareForTest({PortalUtil.class})
public class CustomFieldsTest {

    private static final String FIELD_NAME = "TestField";
	private static final long COMPANY_ID = 1L;
	private static final String ENTITY_NAME = User.class.getName();
	@Mock
    private ExpandoTableLocalService tableService;
    @Mock
    private ExpandoColumnLocalService columnService;
    @Mock
    private ExpandoValueLocalService valueService;
    @Mock
    private RoleLocalService roleService;
    @Mock
    private ResourcePermissionLocalService resourcePermissionService;

    @Mock
    private ExpandoTable mockTable;
    @Mock
    private ExpandoColumn mockColumn;
    @Mock
    private Role mockGuestRole;

    @InjectMocks
    private CustomFieldsImpl customFields;


    @Before
    public void setUp() throws PortalException {
        customFields = new CustomFieldsImpl();
        PowerMockito.mockStatic(PortalUtil.class);
		PowerMockito.when(PortalUtil.getDefaultCompanyId()).thenReturn(1L);
        initMocks(this);
    }

    @Test
    public void testAddCustomTextField() throws PortalException {
        when(tableService.getDefaultTable(COMPANY_ID, ENTITY_NAME)).thenThrow(new NoSuchTableException());
        when(tableService.addTable(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME)).thenReturn(mockTable);
        when(mockTable.getTableId()).thenReturn(1L);
        when(columnService.getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME)).thenReturn(null);
        when(columnService.addColumn(1L, FIELD_NAME, ExpandoColumnConstants.STRING, StringPool.BLANK)).thenReturn(mockColumn);
        when(roleService.getRole(COMPANY_ID, RoleConstants.GUEST)).thenReturn(mockGuestRole);

        customFields.addCustomTextField(ENTITY_NAME, FIELD_NAME, "default", new String[]{RoleConstants.GUEST});

        verify(tableService).getDefaultTable(COMPANY_ID, ENTITY_NAME);
        verify(tableService).addTable(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME);
        verify(columnService).getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME);
        verify(columnService).addColumn(1L, FIELD_NAME, ExpandoColumnConstants.STRING, StringPool.BLANK);
        verify(columnService).updateExpandoColumn(mockColumn);
        verify(roleService).getRole(COMPANY_ID, RoleConstants.GUEST);
        verify(resourcePermissionService).setResourcePermissions(COMPANY_ID,
            ExpandoColumn.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
            String.valueOf(mockColumn.getColumnId()), mockGuestRole.getRoleId(),
            new String[]{ ActionKeys.VIEW, ActionKeys.UPDATE });
    }

    @Test
    public void testAddCustomIntegerField() throws PortalException {
        when(tableService.getDefaultTable(COMPANY_ID, ENTITY_NAME)).thenThrow(new NoSuchTableException());
        when(tableService.addTable(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME)).thenReturn(mockTable);
        when(mockTable.getTableId()).thenReturn(1L);
        when(columnService.getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME)).thenReturn(null);
        when(columnService.addColumn(1L, FIELD_NAME, ExpandoColumnConstants.INTEGER, 0)).thenReturn(mockColumn);
        when(roleService.getRole(COMPANY_ID, RoleConstants.GUEST)).thenReturn(mockGuestRole);

        customFields.addCustomIntegerField(ENTITY_NAME, FIELD_NAME, 1, new String[]{RoleConstants.GUEST});

        verify(tableService).getDefaultTable(COMPANY_ID, ENTITY_NAME);
        verify(tableService).addTable(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME);
        verify(columnService).getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME);
        verify(columnService).addColumn(1L, FIELD_NAME, ExpandoColumnConstants.INTEGER, 0);
        verify(columnService).updateExpandoColumn(mockColumn);
        verify(roleService).getRole(COMPANY_ID, RoleConstants.GUEST);
        verify(resourcePermissionService).setResourcePermissions(COMPANY_ID,
            ExpandoColumn.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
            String.valueOf(mockColumn.getColumnId()), mockGuestRole.getRoleId(),
            new String[]{ ActionKeys.VIEW, ActionKeys.UPDATE });
    }

    @Test
    public void testDeleteCustomField() throws PortalException {
    	when(tableService.getDefaultTable(COMPANY_ID, ENTITY_NAME)).thenReturn(mockTable);
    	when(columnService.getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME)).thenReturn(mockColumn);

        customFields.deleteCustomField(ENTITY_NAME, FIELD_NAME);

        verify(tableService).getDefaultTable(COMPANY_ID, ENTITY_NAME);
        verify(columnService).getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME);
        verify(columnService).deleteExpandoColumn(mockColumn);
        verify(tableService).deleteExpandoTable(mockTable);
    }
    
    @Test
    public void testAddCustomFieldValue() throws PortalException {
    	customFields.addCustomFieldValue(ENTITY_NAME, FIELD_NAME, 0L, "content");
    	
    	verify(valueService).addValue(1L, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME, 0L, "content");
    }
}