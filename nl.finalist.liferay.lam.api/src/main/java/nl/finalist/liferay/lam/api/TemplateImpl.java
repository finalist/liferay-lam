package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateVersionLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = Template.class)
public class TemplateImpl extends ADTImpl implements Template {
    private static final Log LOG = LogFactoryUtil.getLog(TemplateImpl.class);

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Override
    public void createOrUpdateTemplate(String templateKey, String fileUrl, Bundle bundle, String structureKey, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap) {
        long journalArticleClassNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        long structureClassNameId = classNameLocalService.getClassNameId(DDMStructure.class.getName());
        long groupId = defaultValue.getGlobalGroupId();
        long classPK = getClassPk(structureKey, groupId, journalArticleClassNameId);
        DDMTemplate adt = getADT(templateKey, groupId, structureClassNameId);
        if (Validator.isNull(adt)) {
            LOG.info(String.format("Template %s does not exist, creating template", templateKey));
            super.createTemplate(templateKey,fileUrl, bundle, nameMap, descriptionMap, groupId, structureClassNameId, journalArticleClassNameId,classPK);
        } else {
            LOG.info(String.format("Template %s already exist, updating template", templateKey));
            super.updateTemplate(fileUrl, bundle,  nameMap, descriptionMap, classPK, adt, templateKey);
        }
    }

    private long getClassPk(String structureKey, long groupId, long classNameId){
        long classPk = 0;
        DDMStructure ddmStructure = getStructure(structureKey, groupId, classNameId);
        if(ddmStructure != null) {
            classPk = ddmStructure.getStructureId();
        }
        else{
            LOG.info(String.format("Structure %s not found, creating/update template without classPk", structureKey));
        }
        return classPk;
    }


    private DDMStructure getStructure(String structureKey, long groupId, long classNameId){
        DDMStructure ddmStructure = null;
        try {
            ddmStructure = ddmStructureLocalService.getStructure(groupId, classNameId, structureKey);
        } catch (NoSuchStructureException e) {
            LOG.debug(String.format("Structure with key %s does not yet exist", structureKey));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving %s ", structureKey), e);
        }
        return ddmStructure;
    }


    @Override
    @Reference
    public void setDdmTemplateLocalService(DDMTemplateLocalService ddmTemplateLocalService) {
        this.ddmTemplateLocalService = ddmTemplateLocalService;
    }

    @Override
    @Reference
    public void setDdmTemplateVersionLocalService(
        DDMTemplateVersionLocalService ddmTemplateVersionLocalService) {
        this.ddmTemplateVersionLocalService = ddmTemplateVersionLocalService;
    }

    @Override
    @Reference
    public void setClassNameLocalService(ClassNameLocalService classNameLocalService) {
        this.classNameLocalService = classNameLocalService;
    }

    @Override
    @Reference
    public void setDefaultValue(DefaultValue defaultValue) {
    	this.defaultValue = defaultValue;
    }
}
