package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMTemplateVersion;
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
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = Template.class)
public class TemplateImpl implements Template {
    private static final Log LOG = LogFactoryUtil.getLog(TemplateImpl.class);

    @Reference
    private ClassNameLocalService classNameLocalService;
    @Reference
    private DefaultValue defaultValue;
    @Reference
    private DDMTemplateLocalService ddmTemplateLocalService;
    @Reference
    private DDMTemplateLinkLocalService dDMTemplateLinkLocalService;
    @Reference
    private DDMTemplateVersionLocalService ddmTemplateVersionLocalService;
    @Reference
    private Structure structure;
    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Override
    public void createOrUpdateTemplate(String templateKey, String fileUrl, Bundle bundle, String structureKey, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap) {
        long resourceClassNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        long classNameId = classNameLocalService.getClassNameId(DDMStructure.class.getName());
        long groupId = defaultValue.getGlobalGroupId();
        long classPK = getClassPk(structureKey, groupId, resourceClassNameId);
        DDMTemplate template = getTemplate(templateKey, groupId, classNameId);
        if (Validator.isNull(template)) {
            LOG.info(String.format("Template %s does not exist, creating template", templateKey));
            createTemplate(templateKey,fileUrl, bundle, nameMap, descriptionMap, groupId, classNameId, resourceClassNameId,classPK);
        } else {
            LOG.info(String.format("Template %s already exist, updating template", templateKey));
            updateTemplate(fileUrl, bundle,  nameMap, descriptionMap, classPK, template, templateKey);
        }
    }

    private void createTemplate(String templateKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long groupId, long classNameId, long resourceClassNameId, long classPK) {

        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);
        try {
            DDMTemplate ddmTemplate  = ddmTemplateLocalService.addTemplate(defaultValue.getDefaultUserId(), groupId, classNameId, classPK,
                            resourceClassNameId, templateKey, nameMap, descriptionMap,  DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY,
                            DDMTemplateConstants.TEMPLATE_MODE_CREATE, scriptLanguage,
                            script, false, false, null, null, new ServiceContext());

            dDMTemplateLinkLocalService.addTemplateLink(resourceClassNameId, classPK, ddmTemplate.getTemplateId());
            LOG.info(String.format("Template %s succesfully created",templateKey));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while creating template %s", templateKey)+ e);
        }
    }

    private void updateTemplate(String fileUrl, Bundle bundle,Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long classPK, DDMTemplate ddmTemplate,String templateKey) {
        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);

        ddmTemplate.setScript(script);
        ddmTemplate.setLanguage(scriptLanguage);
        ddmTemplate.setNameMap(nameMap);
        ddmTemplate.setClassPK(classPK);
        ddmTemplate.setDescriptionMap(descriptionMap);
        DDMTemplateVersion  ddmTemplateVersion = null;
        String newVersion = String.valueOf(MathUtil.format(Double.valueOf(ddmTemplate.getVersion()) + 0.1, 1, 1));
        try {
            ddmTemplateVersion = ddmTemplate.getLatestTemplateVersion();
            if(Validator.isNotNull(ddmTemplateVersion)){
                ddmTemplateVersion.setVersion(newVersion);
                ddmTemplate.setVersion(newVersion);
                ddmTemplateLocalService.updateDDMTemplate(ddmTemplate);
                ddmTemplateVersionLocalService.updateDDMTemplateVersion(ddmTemplateVersion);
            }
            else{
                LOG.error("DDMTemplateVersion is null, template is not updated");
            }

        } catch (PortalException e) {
            LOG.error("PortalException while retrieving ddmtemplateversion, template is not updated" + e);
        }

        LOG.info(String.format("Template %s succesfully updated", templateKey));
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
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving structure %s ", structureKey) + e);
        }
        return ddmStructure;
    }

    private DDMTemplate getTemplate(String templateKey, long groupId, long classNameId) {
        DDMTemplate ddmTemplate = null;
        try{
            ddmTemplate = ddmTemplateLocalService.getTemplate(groupId, classNameId, templateKey);
        }
        catch(PortalException e){
            LOG.error(String.format("PortalException while retrieving template %s ", templateKey) + e);
        }
        return ddmTemplate;
    }

    private String getContentFromBundle(String fileUrl, Bundle bundle) {
        URL url = bundle.getResource(fileUrl);
        String template = "";
        InputStream input;

        try {
            input = url.openStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
                template = br.lines().collect(Collectors.joining(System.lineSeparator()));
            } finally {
                input.close();
            }
        }
        catch (IOException e) {
            LOG.error("IOException while reading input for template " + fileUrl + " " + e);
        }
        return template;
    }

}
