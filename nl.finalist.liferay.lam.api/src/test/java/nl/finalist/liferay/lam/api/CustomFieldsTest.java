package nl.finalist.liferay.lam.api;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.liferay.expando.kernel.exception.NoSuchTableException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.StringPool;

public class CustomFieldsTest {

    private static final String FIELD_NAME = "TestField";
	private static final long COMPANY_ID = 1L;
	private static final String ENTITY_NAME = User.class.getName();
	@Mock
    private ExpandoTableLocalService tableService;
    @Mock
    private ExpandoColumnLocalService columnService;

    @Mock
    private ExpandoTable mockTable;
    @Mock
    private ExpandoColumn mockColumn;
    
    @InjectMocks
    private CustomFields customFields;
    

    @Before
    public void setUp() throws PortalException {
        customFields = new CustomFields();
        initMocks(this);
    }

    @Test
    public void testAddCustomTextField() throws PortalException {
        when(tableService.getDefaultTable(COMPANY_ID, ENTITY_NAME)).thenThrow(new NoSuchTableException());
        when(tableService.addTable(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME)).thenReturn(mockTable);
        when(mockTable.getTableId()).thenReturn(1L);
        when(columnService.getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME)).thenReturn(null);
        when(columnService.addColumn(1L, FIELD_NAME, ExpandoColumnConstants.STRING, StringPool.BLANK)).thenReturn(mockColumn);
    	
        customFields.addCustomTextField(COMPANY_ID, ENTITY_NAME, FIELD_NAME, "default");
        
        verify(tableService).getDefaultTable(COMPANY_ID, ENTITY_NAME);
        verify(tableService).addTable(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME);
        verify(columnService).getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME);
        verify(columnService).addColumn(1L, FIELD_NAME, ExpandoColumnConstants.STRING, StringPool.BLANK);
    }
    
    @Test
    public void testDeleteCustomTextField() throws PortalException {
    	when(tableService.getDefaultTable(COMPANY_ID, ENTITY_NAME)).thenReturn(mockTable);
    	when(columnService.getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME)).thenReturn(mockColumn);
    	
        customFields.deleteCustomTextField(COMPANY_ID, ENTITY_NAME, FIELD_NAME);
        
        verify(tableService).getDefaultTable(COMPANY_ID, ENTITY_NAME);
        verify(columnService).getColumn(COMPANY_ID, ENTITY_NAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, FIELD_NAME);
        verify(columnService).deleteExpandoColumn(mockColumn);
        verify(tableService).deleteExpandoTable(mockTable);
    }
}