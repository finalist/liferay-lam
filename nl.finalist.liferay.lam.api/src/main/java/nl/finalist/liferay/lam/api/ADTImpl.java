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
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

import nl.finalist.liferay.lam.util.Constants;

@Component(immediate = true, service = ADT.class)
public class ADTImpl implements ADT {

    private static final Log LOG = LogFactoryUtil.getLog(ADTImpl.class);

    protected ClassNameLocalService classNameLocalService;

    protected DefaultValue defaultValue;

    protected DDMTemplateLocalService ddmTemplateLocalService;

    protected DDMTemplateVersionLocalService ddmTemplateVersionLocalService;

    protected CompanyLocalService companyService;

    @Override
    public void createOrUpdateADT(String[] webIds, String adtKey, String fileUrl, Bundle bundle, String className, Map<Locale, String> nameMap,
                                  Map<Locale, String> descriptionMap) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createOrUpdateADTInCompany(webId, adtKey, fileUrl, className, nameMap, descriptionMap, bundle);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            createOrUpdateADTInCompany(webId, adtKey, fileUrl, className, nameMap, descriptionMap, bundle);
        }

    }

    /**
     * New method to create or update ADT in company with given webId
     */
    private void createOrUpdateADTInCompany(String webId, String adtKey, String fileUrl, String className, Map<Locale, String> nameMap,
                                            Map<Locale, String> descriptionMap, Bundle bundle) {

        Company company = null;
        long groupId = 0;
        long defaultUserId = 0;

        try {
            company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
            defaultUserId = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Create/Updated ADT for this company", webId));
            LOG.error(e);
        }
        if (Validator.isNotNull(company) && groupId > 0 && defaultUserId > 0) {
            long resourceClassNameId = classNameLocalService.getClassNameId("com.liferay.portlet.display.template.PortletDisplayTemplate");
            long classNameId = classNameLocalService.getClassNameId(className);
            DDMTemplate adt = getADT(adtKey, groupId, classNameId);
            if (Validator.isNull(adt)) {
                LOG.info(String.format("ADT %s does not exist in company with web id %s, creating ADT", adtKey, webId));
                createTemplate(adtKey, fileUrl, bundle, nameMap, descriptionMap, groupId, defaultUserId, classNameId, resourceClassNameId, 0L);
            } else {
                LOG.info(String.format("ADT %s already exists in company with web id %s, updating ADT", adtKey, webId));
                updateTemplate(fileUrl, bundle, nameMap, descriptionMap, 0, adt, adtKey);
            }
        }
    }

    void createTemplate(String adtKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, long groupId,
                        long defaultUserId, long classNameId, long resourceClassNameId, long classPk) {

        String scriptLanguage = FilenameUtils.getExtension(fileUrl);
        String script = getContentFromBundle(fileUrl, bundle);
        try {
            ddmTemplateLocalService.addTemplate(defaultUserId, groupId, classNameId, classPk, resourceClassNameId, adtKey, nameMap, descriptionMap,
                    DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, DDMTemplateConstants.TEMPLATE_MODE_CREATE, scriptLanguage, script, false, false, null,
                    null, new ServiceContext());
            LOG.info(String.format("ADT %s succesfully created", adtKey));
        } catch (PortalException e) {
            LOG.error("PortalException while creating ADT " + adtKey, e);
        }
    }

    void updateTemplate(String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, long classPK, DDMTemplate adt,
                        String adtKey) {
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

        DDMTemplateVersion adtVersion = null;

        try {
            adtVersion = adt.getLatestTemplateVersion();
            if (Validator.isNotNull(adtVersion)) {
                adtVersion.setVersion(newVersion);
                adt.setVersion(newVersion);
                ddmTemplateLocalService.updateDDMTemplate(adt);
                ddmTemplateVersionLocalService.updateDDMTemplateVersion(adtVersion);
            } else {
                LOG.error("DDMTemplateVersion is null, template is not updated");
            }

        } catch (PortalException e) {
            LOG.error(String.format("PortalException while retrieving ddmtemplateversion," + " template %s is not updated", adtKey), e);
        }
        LOG.info(String.format("ADT %s succesfully updated", adtKey));
    }

    protected DDMTemplate getADT(String adtKey, long groupId, long classNameId) {
        DDMTemplate adt = null;
        try {
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
            if (url != null) {
                input = url.openStream();
            } else {
                File script = new File(Constants.TEMP_LAM_SUBDIR + StringPool.SLASH + fileUrl);
                input = new FileInputStream(script);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
                template = br.lines().collect(Collectors.joining(System.lineSeparator()));
            } finally {
                input.close();
            }
        } catch (IOException e) {
            LOG.error("IOException while reading input for ADT " + fileUrl, e);
        }
        return template;
    }

    @Reference
    public void setDdmTemplateLocalService(DDMTemplateLocalService ddmTemplateLocalService) {
        this.ddmTemplateLocalService = ddmTemplateLocalService;
    }

    @Reference
    public void setDdmTemplateVersionLocalService(DDMTemplateVersionLocalService ddmTemplateVersionLocalService) {
        this.ddmTemplateVersionLocalService = ddmTemplateVersionLocalService;
    }

    @Reference
    public void setClassNameLocalService(ClassNameLocalService classNameLocalService) {
        this.classNameLocalService = classNameLocalService;
    }

    @Reference
    public void setDefaultValue(DefaultValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
