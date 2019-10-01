package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
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

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.util.Constants;

@Component(immediate = true, service = Structure.class)
public class StructureImpl implements Structure {

    private static final Log LOG = LogFactoryUtil.getLog(StructureImpl.class);

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    @Reference
    private ClassNameLocalService classNameLocalService;

    @Reference
    private DDMStructureVersionLocalService ddmStructureVersionLocalService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private GroupLocalService groupService;

    @Reference
    private CompanyLocalService companyService;

    @Reference
    private DDM ddm;

    @Override
    public void createOrUpdateStructure(String[] webIds, String structureKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                                        Map<Locale, String> descriptionMap, String siteKey) {
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createOrUpdateStructureInCompany(webId, structureKey, fileUrl, bundle, nameMap, descriptionMap, siteKey);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            createOrUpdateStructureInCompany(webId, structureKey, fileUrl, bundle, nameMap, descriptionMap, siteKey);
        }

    }

    /**
     * New method to create or update structure in company with given webId
     */
    private void createOrUpdateStructureInCompany(String webId, String structureKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                                                  Map<Locale, String> descriptionMap, String siteKey) {

        Company company = null;
        long groupId = 0;
        long userId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
            userId = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Create/Updated Structure for this company", webId));
            LOG.error(e);
        }
        if (Validator.isNotNull(company) && groupId > 0 && userId > 0) {
            long classNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());

            if (siteKey != null) {
                Group site = groupService.fetchGroup(company.getCompanyId(), siteKey);
                if (site != null) {
                    groupId = site.getGroupId();
                }
            }
            DDMStructure structure = getStructure(structureKey, groupId, classNameId);
            if (Validator.isNull(structure)) {
                LOG.info(String.format("Structure %s does not exist, creating structure in company with webId %s", structureKey, webId));
                createStructure(structureKey, fileUrl, bundle, nameMap, descriptionMap, groupId, userId, classNameId);
            } else {
                LOG.info(String.format("Structure %s already exist, updating structure in company with webId %s", structureKey, webId));
                updateStructure(fileUrl, bundle, nameMap, descriptionMap, structure, structureKey);
            }
        }

    }

    private void createStructure(String structureKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
                                 long groupId, long userId, long classNameId) {
        DDMForm ddmForm = createDDMForm(fileUrl, bundle);
        try {
            ddmStructureLocalService.addStructure(userId, groupId, null, classNameId, structureKey, nameMap, descriptionMap, ddmForm,
                    createDDMFormLayout(ddmForm), "json", 0, new ServiceContext());
            LOG.info(String.format("Structure %s was added", structureKey));
        } catch (PortalException e) {
            LOG.error(String.format("PortalException while saving ddmStructure %s ", structureKey) + e);
        }
    }

    private void updateStructure(String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
                                 DDMStructure ddmStructure, String structureKey) {
        DDMForm ddmForm = createDDMForm(fileUrl, bundle);
        ddmStructure.setNameMap(nameMap);
        ddmStructure.setDescriptionMap(descriptionMap);
        ddmStructure.setDDMForm(ddmForm);
        DDMStructureVersion ddmStructureVersion = null;
        String newVersion = String.valueOf(MathUtil.format(Double.valueOf(ddmStructure.getVersion()) + 0.1, 1, 1));
        try {
            ddmStructureVersion = ddmStructure.getLatestStructureVersion();
            if (Validator.isNotNull(ddmStructureVersion)) {
                ddmStructureVersion.setVersion(newVersion);
                ddmStructure.setVersion(newVersion);
                ddmStructureLocalService.updateDDMStructure(ddmStructure);
                ddmStructureVersionLocalService.updateDDMStructureVersion(ddmStructureVersion);
                LOG.info(String.format("Structure %s was successfully updated", structureKey));
            } else {
                LOG.error("DDMStructureversion for structure is null, structure is not updated");
            }
        } catch (PortalException e) {
            LOG.error("PortalException while retrieving ddmstructureversion, structure not updated", e);
        }

    }

    private DDMFormLayout createDDMFormLayout(DDMForm ddmForm) {
        return ddm.getDefaultDDMFormLayout(ddmForm);
    }

    private DDMStructure getStructure(String structureKey, long groupId, long classNameId) {
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

    private DDMForm createDDMForm(String fileUrl, Bundle bundle) {
        String content = getContentFromBundle(fileUrl, bundle);
        try {
            return ddm.getDDMForm(content);
        } catch (PortalException e) {
            LOG.error("PortalException when creating DDMForm", e);
            return null;
        }
    }

    private String getContentFromBundle(String fileUrl, Bundle bundle) {
        URL url = bundle.getResource(fileUrl);
        String structure = "";
        InputStream input;

        try {
            if (url != null) {
                input = url.openStream();
            } else {
                File script = new File(Constants.TEMP_LAM_SUBDIR + StringPool.SLASH + fileUrl);
                input = new FileInputStream(script);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
                structure = br.lines().collect(Collectors.joining(System.lineSeparator()));
            } finally {
                input.close();
            }
        } catch (IOException e) {
            LOG.error("IOException while reading input for structure " + fileUrl + " " + e);
        }
        return structure;
    }
}
