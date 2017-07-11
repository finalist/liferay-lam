package nl.finalist.liferay.lam.api;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.expando.kernel.exception.NoSuchTableException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Arrays;

@Component(immediate = true, service=CustomFields.class)
public class CustomFieldsImpl implements CustomFields {

    @Reference
    private ExpandoTableLocalService tableService;

    @Reference
    private ExpandoColumnLocalService columnService;
    
    @Reference
    private RoleLocalService roleService;
    
    @Reference
    private ResourcePermissionLocalService resourcePermissionService;

    private static final Log LOG = LogFactoryUtil.getLog(CustomFieldsImpl.class);
     

    /**
     * Add a custom field of type text. 
     * 
     * @param companyId company ID that the field is valid for
     * @param entityName entity that the field is added to
     * @param fieldName name of the field
     * @param value default value of the field
     */
    public void addCustomTextField(long companyId, String entityName, String fieldName, String value, String [] roles) {
        LOG.debug(String.format("Start adding custom text field %s for company %d to entity %s with default value %s and roles %s", fieldName, companyId, entityName, value, Arrays.toString(roles)));
     
        ExpandoTable expandoTable = getOrAddExpandoTable(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME);
        LOG.debug("Expando Table ID : " + expandoTable.getTableId());
     
        ExpandoColumn expandoColumn = getOrAddExpandoTextColumn(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME, fieldName, expandoTable);
        saveDefaultValueForColumn(value, expandoColumn);
        for (String role : roles) {
        	addExpandoPermissions(companyId, expandoColumn, role);
		}
        LOG.debug("Expando Column ID : " + expandoColumn.getColumnId());
     
        LOG.debug("Done adding text custom field");
                
    }

	/**
     * Add a custom field of type integer
     * 
     * @param companyId company ID that the field is valid for
     * @param entityName entity that the field is added to
     * @param fieldName name of the field
     * @param value default value of the field
     */
    public void addCustomIntegerField(long companyId, String entityName, String fieldName, String value, String [] roles) {
        LOG.debug(String.format("Start adding custom integer field %s for company %d to entity %s with default value %s", fieldName, companyId, entityName, value));
     
        ExpandoTable expandoTable = getOrAddExpandoTable(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME);
        LOG.debug("Expando Table ID : " + expandoTable.getTableId());
     
        ExpandoColumn expandoColumn = getOrAddExpandoIntegerColumn(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME, fieldName, expandoTable);
        saveDefaultValueForColumn(value, expandoColumn);
        for (String role : roles) {
        	addExpandoPermissions(companyId, expandoColumn, role);
		}
        LOG.debug("Expando Column ID : " + expandoColumn.getColumnId());
     
        LOG.debug("Done adding integer custom field");
    }
     
    /**
     * Delete a custom field
     * 
     * @param companyId company ID that the field is valid for
     * @param entityName entity to which the field applies
     * @param fieldName name of the field
     */
    public void deleteCustomField(long companyId, String entityName, String fieldName) {
        LOG.debug(String.format("Start deleting custom field %s for company %d of entity %s ", fieldName, companyId, entityName));
     
        try {
    		ExpandoTable expandoTable = tableService.getDefaultTable(companyId, entityName);
    		
    		ExpandoColumn exandoColumn = columnService.getColumn(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME, fieldName);
            if (exandoColumn != null) {
                columnService.deleteExpandoColumn(exandoColumn);
            } else {
            	LOG.warn("No column to delete");
            }
            
    		tableService.deleteExpandoTable(expandoTable);
    	} catch (NoSuchTableException e) {
    		LOG.warn("No table to delete");
    	} catch (PortalException e) {
			LOG.error(e);
		} 
     
        LOG.debug("Done deleting custom field");
    }
     
    
    private ExpandoTable getOrAddExpandoTable(long companyId, String className, String tableName) {
        ExpandoTable expandoTable = null;
        try {
            expandoTable = tableService.getDefaultTable(companyId, className);
        } catch (NoSuchTableException e) {
        	LOG.warn("The table did not yet exist");
            try {
                expandoTable = tableService.addTable(companyId, className, tableName);
            } catch (Exception e1) {
                LOG.error(e);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return expandoTable;
    }
    
    private void saveDefaultValueForColumn(String value, ExpandoColumn expandoColumn) {
		expandoColumn.setDefaultData(value);
        columnService.updateExpandoColumn(expandoColumn);
	}
    
    private ExpandoColumn getOrAddExpandoTextColumn(long companyId, String className, String tableName, String columnName,
            ExpandoTable expandoTable) {
        return getOrAddExpandoColumn(companyId, className, tableName, columnName, expandoTable, ExpandoColumnConstants.STRING);
    }
    
    private ExpandoColumn getOrAddExpandoIntegerColumn(long companyId, String className, String tableName, String columnName,
            ExpandoTable expandoTable) {
        return getOrAddExpandoColumn(companyId, className, tableName, columnName, expandoTable, ExpandoColumnConstants.INTEGER);
    }
    
    private ExpandoColumn getOrAddExpandoColumn(long companyId, String className, String tableName, String columnName,
            ExpandoTable expandoTable, int columnType) {
        ExpandoColumn exandoColumn = null;
        try {
            exandoColumn = columnService.getColumn(companyId, className, tableName, columnName);
            if (exandoColumn == null) {
                exandoColumn = columnService.addColumn(expandoTable.getTableId(), columnName,
                        columnType, columnType == ExpandoColumnConstants.INTEGER ? Integer.valueOf(0) :  StringPool.BLANK);
            }
        } catch (SystemException | PortalException e) {
            LOG.error(e);
        }       
     
        return exandoColumn;
    }
    
    private void addExpandoPermissions(long companyId, ExpandoColumn column, String role) {
        try {
            Role guestUserRole = roleService.getRole(companyId, role);
            LOG.debug("Guest role fetched");
            if (guestUserRole != null) {
                  // define actions 
                  String[] actionIds = new String[] { ActionKeys.VIEW, ActionKeys.UPDATE };
                  // set the permission
                  resourcePermissionService.setResourcePermissions(companyId, 
                    ExpandoColumn.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, 
                    String.valueOf(column.getColumnId()), guestUserRole.getRoleId(), actionIds);
                  LOG.debug(role + " permissions set");
              }
        } catch (PortalException pe) {
        	LOG.error(pe);
        }
    }
}