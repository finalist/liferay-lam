package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLinkLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateVersionLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = Template.class)
public class TemplateImpl extends ADTImpl implements Template {
    private static final Log LOG = LogFactoryUtil.getLog(TemplateImpl.class);

    @Reference
    private ClassNameLocalService classNameLocalService;
    @Reference
    private DefaultValue defaultValue;

    @Reference
    private DDMTemplateLinkLocalService dDMTemplateLinkLocalService;

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Reference
    private ScopeHelper scopeHelper;

    @Override
    public void createOrUpdateTemplate(String siteKey,String adtKey, String fileUrl, Bundle bundle, String structureKey, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap) {
        long resourceClassNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        long classNameId = classNameLocalService.getClassNameId(DDMStructure.class.getName());
        long groupId = scopeHelper.getGroupIdByName(siteKey);
        long classPK = getClassPk(structureKey, groupId, resourceClassNameId);
        DDMTemplate adt = getADT(adtKey, groupId, classNameId);
        if (Validator.isNull(adt)) {
            LOG.info(String.format("Template %s does not exist, creating template", adtKey));
            createTemplate(adtKey,fileUrl, bundle, nameMap, descriptionMap, groupId, classNameId, resourceClassNameId,classPK);
        } else {
            LOG.info(String.format("Template %s already exist, updating template", adtKey));
            updateADT(fileUrl, bundle,  nameMap, descriptionMap, classPK, adt, adtKey);
        }
    }

    private void createTemplate(String adtKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long groupId, long classNameId, long resourceClassNameId, long classPK) {

        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);
        try {
            ddmTemplateLocalService.addTemplate(defaultValue.getDefaultUserId(), groupId, classNameId, classPK,
                            resourceClassNameId, adtKey, nameMap, descriptionMap,  DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY,
                            DDMTemplateConstants.TEMPLATE_MODE_CREATE, scriptLanguage,
                            script, false, false, null, null, new ServiceContext());

            LOG.info(String.format("Template %s succesfully created", adtKey));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while creating template %s: %s", adtKey, e.getMessage()));
        }
    }


    private long getClassPk(String structureKey, long groupId, long classNameId){
        long classPk = 0;
        DDMStructure ddmStructure = getStructure(structureKey, groupId, classNameId);
        if(Validator.isNotNull(ddmStructure)){
            classPk = ddmStructure.getStructureId();
        }
        else{
            LOG.info(String.format("Structure  %s not found, creating/update template without classPk", structureKey));
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


    @Reference
    public void setDdmTemplateLocalService(DDMTemplateLocalService ddmTemplateLocalService) {
        this.ddmTemplateLocalService = ddmTemplateLocalService;
    }

    @Reference
    public void setDdmTemplateVersionLocalService(
        DDMTemplateVersionLocalService ddmTemplateVersionLocalService) {
        this.ddmTemplateVersionLocalService = ddmTemplateVersionLocalService;
    }
}
