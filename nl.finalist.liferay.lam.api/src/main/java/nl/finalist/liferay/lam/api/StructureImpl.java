package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = Structure.class)
public class StructureImpl implements Structure {
    private static final Log LOG = LogFactoryUtil.getLog(StructureImpl.class);
    @Reference
    private DDMStructureLocalService ddmStructureLocalService;
    @Reference
    private ClassNameLocalService classNameLocalService;
    @Reference
    private DefaultValue defaultValue;

    @Override
    public void createStructure(String content, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap) {
        DDMForm ddmForm = createDDMForm(content);
        long classNameId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class.getName());
        try {
            ddmStructureLocalService.addStructure(defaultValue.getDefaultUserId(), defaultValue.getGlobalGroupId(),
                            null, classNameId, null, nameMap, descriptionMap, ddmForm,
                            createDDMFormLayout(ddmForm), "json", 0, new ServiceContext());
        } catch (PortalException e) {
            LOG.error("PortalException while saving ddmStructure",e);
        }
    }

    private DDMFormLayout createDDMFormLayout(DDMForm ddmForm) {
        DDMFormLayout ddmFormLayout = DDMUtil.getDefaultDDMFormLayout(ddmForm);

        return ddmFormLayout;
    }

    private DDMForm createDDMForm(String content) {
        DDMForm ddmForm = null;
        try {
            ddmForm = DDMUtil.getDDMForm(content);
        } catch (PortalException e) {
            LOG.error("PortalException when creating DDMForm", e);
        }
        return ddmForm;
    }

}
