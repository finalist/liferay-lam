package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLinkLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
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
import java.util.List;
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
    private Structure structure;
    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Override
    public void createOrUpdateTemplate(String fileUrl, Bundle bundle, String forStructure, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap) {
        long resourceClassNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        long classNameId = classNameLocalService.getClassNameId(DDMStructure.class.getName());
        long groupId = defaultValue.getGlobalGroupId();
        long classPK = getClassPk(forStructure, groupId, resourceClassNameId);
        DDMTemplate template = getTemplate(nameMap, groupId, classNameId);

        String name = "";
        if (nameMap.entrySet().iterator().hasNext()) {
            name = nameMap.entrySet().iterator().next().getValue();
        }

        if (Validator.isNull(template)) {
            LOG.info(String.format("Template %s does not exist, creating template", name));
            createTemplate(fileUrl, bundle, nameMap, descriptionMap, groupId, classNameId, resourceClassNameId,classPK, name);
        } else {
            LOG.info(String.format("Template %s already exist, updating template", name));
            updateTemplate(fileUrl, bundle,  nameMap, descriptionMap, classPK, template, name);
        }
    }

    private void createTemplate(String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long groupId, long classNameId, long resourceClassNameId, long classPK,String name) {

        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);

        try {
            DDMTemplate ddmTemplate = ddmTemplateLocalService.addTemplate(defaultValue.getDefaultUserId(), groupId, classNameId,
                            classPK, resourceClassNameId, nameMap, descriptionMap,
                            DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, DDMTemplateConstants.TEMPLATE_MODE_CREATE, scriptLanguage, script, new ServiceContext());
            dDMTemplateLinkLocalService.addTemplateLink(resourceClassNameId, classPK, ddmTemplate.getTemplateId());
            LOG.info(String.format("Template %s succesfully created", name));
        } catch (PortalException e) {
            LOG.error("PortalException while creating template " + name, e);
        }
    }

    private void updateTemplate(String fileUrl, Bundle bundle,Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long classPK, DDMTemplate ddmTemplate,String name) {
        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);

        ddmTemplate.setScript(script);
        ddmTemplate.setLanguage(scriptLanguage);
        ddmTemplate.setNameMap(nameMap);
        ddmTemplate.setClassPK(classPK);
        ddmTemplate.setDescriptionMap(descriptionMap);
        ddmTemplate.setVersion(String.valueOf(MathUtil.format(Double.valueOf(ddmTemplate.getVersion()) + 0.1, 1, 1)));
        ddmTemplateLocalService.updateDDMTemplate(ddmTemplate);
        LOG.info(String.format("Template %s succesfully updated", name));
    }

    private long getClassPk(String structureName, long groupId, long classNameId){
        long classPk = 0;
        DDMStructure ddmStructure = getStructure(structureName, groupId, classNameId);
        if(Validator.isNotNull(ddmStructure)){
            classPk = ddmStructure.getStructureId();
        }
        else{
            LOG.info(String.format("Structure  %s not found, creating/update template without classPk", structureName));
        }
        return classPk;
    }


    private DDMStructure getStructure(String name, long groupId, long classNameId){
        List<DDMStructure> structures = ddmStructureLocalService.getStructures(groupId, classNameId);
        DDMStructure ddmStructure = null;
        for(DDMStructure structure: structures){
            if(structure.getNameCurrentValue().equalsIgnoreCase(name)){
                ddmStructure = structure;
            }
        }
        return ddmStructure;
    }

    private DDMTemplate getTemplate(Map<Locale, String> nameMap, long groupId, long classNameId) {
        List<DDMTemplate> templates = ddmTemplateLocalService.getTemplates(groupId, classNameId);
        DDMTemplate ddmTemplate = null;
        for (DDMTemplate template : templates) {
            if(nameMap.containsValue(template.getNameCurrentValue())){
                ddmTemplate = template;
            }
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
