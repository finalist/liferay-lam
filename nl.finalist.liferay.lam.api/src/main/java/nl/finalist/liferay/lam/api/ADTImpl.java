package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
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

@Component(immediate = true, service = ADT.class)
public class ADTImpl implements ADT{
    private static final Log LOG = LogFactoryUtil.getLog(ADTImpl.class);

    @Reference
    private ClassNameLocalService classNameLocalService;
    @Reference
    private DefaultValue defaultValue;
    @Reference
    private DDMTemplateLocalService ddmTemplateLocalService;

    @Override
    public void createOrUpdateADT(String fileUrl, Bundle bundle, String className, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap) {
        long resourceClassNameId = classNameLocalService.getClassNameId("com.liferay.portlet.display.template.PortletDisplayTemplate");
        long classNameId = classNameLocalService.getClassNameId(className);
        long groupId = defaultValue.getGlobalGroupId();
        DDMTemplate ADT = getADT(nameMap, groupId, classNameId);

        String name = "";
        if (nameMap.entrySet().iterator().hasNext()) {
            name = nameMap.entrySet().iterator().next().getValue();
        }

        if (Validator.isNull(ADT)) {
            LOG.info(String.format("ADT %s does not exist, creating ADT", name));
            createADT(fileUrl, bundle, nameMap, descriptionMap, groupId, classNameId, resourceClassNameId,name);
        } else {
            LOG.info(String.format("ADT %s already exist, updating ADT", name));
            updateADT(fileUrl, bundle,  nameMap, descriptionMap, ADT, name);
        }
    }
    private void createADT(String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long groupId, long classNameId, long resourceClassNameId, String name) {

        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);
        try {
            ddmTemplateLocalService.addTemplate(defaultValue.getDefaultUserId(), groupId, classNameId,
                            0, resourceClassNameId, nameMap, descriptionMap,
                            DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, scriptLanguage, script, new ServiceContext());
            LOG.info(String.format("ADT %s succesfully created", name));
        } catch (PortalException e) {
            LOG.error("PortalException while creating ADT " + name, e);
        }
    }

    private void updateADT(String fileUrl, Bundle bundle,Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, DDMTemplate ADT,String name) {
        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);

        ADT.setScript(script);
        ADT.setLanguage(scriptLanguage);
        ADT.setNameMap(nameMap);
        ADT.setDescriptionMap(descriptionMap);
        ADT.setVersion(String.valueOf(MathUtil.format(Double.valueOf(ADT.getVersion()) + 0.1, 1, 1)));
        ddmTemplateLocalService.updateDDMTemplate(ADT);
        LOG.info(String.format("ADT %s succesfully updated", name));
    }


    private DDMTemplate getADT(Map<Locale, String> nameMap, long groupId, long classNameId) {
        List<DDMTemplate> ADTS = ddmTemplateLocalService.getTemplates(groupId, classNameId);
        DDMTemplate ADT = null;
        for (DDMTemplate template : ADTS) {
            if(nameMap.containsValue(template.getNameCurrentValue())){
                ADT = template;
            }
        }
        return ADT;
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
            LOG.error("IOException while reading input for ADT " + fileUrl + " " + e);
        }
        return template;
    }

}

