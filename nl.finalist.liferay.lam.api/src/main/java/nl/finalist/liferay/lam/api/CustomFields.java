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
import com.liferay.portal.kernel.util.StringPool;

@Component(immediate = true)
public class CustomFields {

    @Reference
    private ExpandoTableLocalService tableService;

    @Reference
    private ExpandoColumnLocalService columnService;

    private static final Log LOG = LogFactoryUtil.getLog(CustomFields.class);
     
    
     
    public void addCustomTextField(long companyId, String entityName, String fieldName, String value) {
        LOG.info(String.format("Start adding custom field %s for company %d to entity %s with default value %s", fieldName, companyId, entityName, value));
     
        ExpandoTable expandoTable = getOrAddExpandoTable(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME);
        LOG.info("Expando Table ID : " + expandoTable.getTableId());
     
        ExpandoColumn expandoColumn = getOrAddExpandoColumn(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME, fieldName, expandoTable);
        expandoColumn.setDefaultData(value);
        LOG.info("Expando Column ID : " + expandoColumn.getColumnId());
     
        LOG.info("Done adding custom field");
    }
     
    public void deleteCustomTextField(long companyId, String entityName, String fieldName) {
        LOG.info(String.format("Start deleting custom field %s for company %d of entity %s ", fieldName, companyId, entityName));
     
        try {
    		ExpandoTable expandoTable = tableService.getDefaultTable(companyId, entityName);
    		
    		ExpandoColumn exandoColumn = columnService.getColumn(companyId, entityName, ExpandoTableConstants.DEFAULT_TABLE_NAME, fieldName);
            if (exandoColumn != null) {
                columnService.deleteExpandoColumn(exandoColumn);
            } else {
            	LOG.info("No column to delete");
            }
            
    		tableService.deleteExpandoTable(expandoTable);
    	} catch (NoSuchTableException e) {
    		LOG.info("No table to delete");
    	} catch (PortalException e) {
			LOG.error(e);
		} 
     
        LOG.info("Done deleting custom field");
    }
     
    
    public ExpandoTable getOrAddExpandoTable(long companyId, String className, String tableName) {
        ExpandoTable expandoTable = null;
        try {
            expandoTable = tableService.getDefaultTable(companyId, className);
        } catch (NoSuchTableException e) {
        	LOG.info("The table did not yet exist");
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
    
     
    public ExpandoColumn getOrAddExpandoColumn(long companyId, String className, String tableName, String columnName,
            ExpandoTable expandoTable) {
        ExpandoColumn exandoColumn = null;
        try {
            exandoColumn = columnService.getColumn(companyId, className, tableName, columnName);
            if (exandoColumn == null) {
                exandoColumn = columnService.addColumn(expandoTable.getTableId(), columnName,
                        ExpandoColumnConstants.STRING, StringPool.BLANK);
            }
        } catch (SystemException | PortalException e) {
            LOG.error(e);
        }       
     
        return exandoColumn;
    }
}