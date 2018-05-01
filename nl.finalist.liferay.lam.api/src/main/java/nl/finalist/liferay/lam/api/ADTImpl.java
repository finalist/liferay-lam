package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.exception.NoSuchTemplateException;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMTemplateVersion;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateVersionLocalService;
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

@Component(immediate = true, service = ADT.class)
public class ADTImpl implements ADT{
    private static final Log LOG = LogFactoryUtil.getLog(ADTImpl.class);

    @Reference
    private ClassNameLocalService classNameLocalService;
    @Reference
    private DefaultValue defaultValue;
    @Reference
     private ScopeHelper scopeHelper;

    protected DDMTemplateLocalService ddmTemplateLocalService;

    protected DDMTemplateVersionLocalService ddmTemplateVersionLocalService;

    @Override
    public void createOrUpdateADT(String siteKey, String adtKey,String fileUrl, Bundle bundle, String className, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap) {
        long resourceClassNameId = classNameLocalService.getClassNameId("com.liferay.portlet.display.template.PortletDisplayTemplate");
        long classNameId = classNameLocalService.getClassNameId(className);
        long groupId = scopeHelper.getGroupIdByName(siteKey);
        DDMTemplate adt = getADT(adtKey, groupId, classNameId);
        if (Validator.isNull(adt)) {
            LOG.info(String.format("ADT %s does not exist, creating ADT",adtKey));
            createADT(adtKey, fileUrl, bundle, nameMap, descriptionMap, groupId, classNameId, resourceClassNameId);
        } else {
            LOG.info(String.format("ADT %s already exist, updating ADT", adtKey));
            updateADT(fileUrl, bundle,  nameMap, descriptionMap, 0, adt, adtKey);
        }
    }
    protected void createADT(String adtKey,String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long groupId, long classNameId, long resourceClassNameId) {

        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);
        try {
            ddmTemplateLocalService.addTemplate(defaultValue.getDefaultUserId(), groupId, classNameId,0, resourceClassNameId,adtKey, nameMap, descriptionMap,
                            DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, scriptLanguage, script,
                            false,false, null, null, new ServiceContext());
            LOG.info(String.format("ADT %s succesfully created", adtKey));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while creating ADT %s ",adtKey)+ e);
        }
    }

    void updateADT(String fileUrl, Bundle bundle,Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long classPK, DDMTemplate adt,String adtKey) {
        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);
        String newVersion = String.valueOf(MathUtil.format(Double.valueOf(adt.getVersion()) + 0.1, 1, 1));

        adt.setScript(script);
        adt.setLanguage(scriptLanguage);
        adt.setNameMap(nameMap);
        if (classPK > 0) {
            adt.setClassPK(classPK);
        }
        adt.setDescriptionMap(descriptionMap);

        DDMTemplateVersion  adtVersion = null;

        try {
            adtVersion = adt.getLatestTemplateVersion();
            if(Validator.isNotNull(adtVersion)){
                adtVersion.setVersion(newVersion);
                adt.setVersion(newVersion);
                ddmTemplateLocalService.updateDDMTemplate(adt);
                ddmTemplateVersionLocalService.updateDDMTemplateVersion(adtVersion);
            }
            else{
                LOG.error("DDMTemplateVersion is null, template is not updated");
            }

        } catch (PortalException e) {
            LOG.error("PortalException while retrieving ddmtemplateversion, template is not updated" + e);
        }
        LOG.info(String.format("ADT %s succesfully updated", adtKey));
    }


    protected DDMTemplate getADT(String adtKey, long groupId, long classNameId) {
        DDMTemplate adt = null;
        try{
            adt = ddmTemplateLocalService.getTemplate(groupId, classNameId, adtKey);
        } catch (NoSuchTemplateException e) {
            LOG.debug(String.format("DDMTemplate with key %s does not yet exist", adtKey));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving %s ", adtKey), e);
        }
        return adt;
    }

    protected String getContentFromBundle(String fileUrl, Bundle bundle) {
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

