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
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
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

    @Reference
    private GroupLocalService groupService;

    @Override
    public void createOrUpdateTemplate(String[] webIds, String templateKey, String fileUrl, Bundle bundle, String structureKey,
                                       Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String siteKey) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createOrUpdateTemplateInCompany(webId, templateKey, fileUrl, bundle, structureKey, nameMap, descriptionMap, siteKey);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            createOrUpdateTemplateInCompany(webId, templateKey, fileUrl, bundle, structureKey, nameMap, descriptionMap, siteKey);
        }

    }

    /**
     * New method to create or update template in company with given webId
     */
    private void createOrUpdateTemplateInCompany(String webId, String templateKey, String fileUrl, Bundle bundle, String structureKey,
                                                 Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String siteKey) {
        long journalArticleClassNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        long structureClassNameId = classNameLocalService.getClassNameId(DDMStructure.class.getName());
        Company company = null;
        long groupId = 0;
        long defaultUserId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
            defaultUserId = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Create/Update Template for this company", webId));
            LOG.error(e);
        }

        if (Validator.isNotNull(company) && groupId > 0 && defaultUserId > 0) {
            if (siteKey != null) {
                Group site = groupService.fetchGroup(company.getCompanyId(), siteKey);
                if (site != null) {
                    groupId = site.getGroupId();
                }
            }

            long classPK = getClassPk(structureKey, groupId, journalArticleClassNameId);
            DDMTemplate adt = getADT(templateKey, groupId, structureClassNameId);
            if (Validator.isNull(adt)) {
                LOG.info(String.format("Template %s does not exist, creating template in company with webId %s", templateKey, webId));
                super.createTemplate(templateKey, fileUrl, bundle, nameMap, descriptionMap, groupId, defaultUserId, structureClassNameId,
                        journalArticleClassNameId, classPK);
            } else {
                LOG.info(String.format("Template %s already exist, updating template in company with webId %s", templateKey, webId));
                super.updateTemplate(fileUrl, bundle, nameMap, descriptionMap, classPK, adt, templateKey);
            }
        }

    }

    private long getClassPk(String structureKey, long groupId, long classNameId) {
        long classPk = 0;
        DDMStructure ddmStructure = getStructure(structureKey, groupId, classNameId);
        if (ddmStructure != null) {
            classPk = ddmStructure.getStructureId();
        } else {
            LOG.info(String.format("Structure %s not found, creating/update template without classPk", structureKey));
        }
        return classPk;
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

    @Override
    @Reference
    public void setDdmTemplateLocalService(DDMTemplateLocalService ddmTemplateLocalService) {
        this.ddmTemplateLocalService = ddmTemplateLocalService;
    }

    @Override
    @Reference
    public void setDdmTemplateVersionLocalService(DDMTemplateVersionLocalService ddmTemplateVersionLocalService) {
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

    @Override
    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }

}
