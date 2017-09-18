package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
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

import org.osgi.framework.Bundle;
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
    public void createOrUpdateStructure(String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap){
        long classNameId = classNameLocalService.getClassNameId(JournalArticle.class.getName());
        long groupId = defaultValue.getGlobalGroupId();
        DDMStructure structure = getStructure(nameMap, groupId, classNameId);
        String name = "";
        if(nameMap.entrySet().iterator().hasNext()){
            name = nameMap.entrySet().iterator().next().getValue();
        }
        if(Validator.isNull(structure)){
            LOG.info(String.format("Structure %s does not exist, creating structure", name));
            createStructure(fileUrl, bundle, nameMap, descriptionMap, groupId, classNameId, name);
        }
        else{
            LOG.info(String.format("Structure %s already exist, updating structure", name));
            updateStructure(fileUrl, bundle, nameMap, descriptionMap, structure, name);
        }
    }

    private void createStructure(String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, long groupId, long classNameId, String name) {
        DDMForm ddmForm = createDDMForm(fileUrl, bundle);
        try {
            ddmStructureLocalService.addStructure(defaultValue.getDefaultUserId(), groupId ,
                            null, classNameId, null, nameMap, descriptionMap, ddmForm,
                            createDDMFormLayout(ddmForm), "json", 0, new ServiceContext());
            LOG.info(String.format("Structure %s was added", name));
        } catch (PortalException e) {
            LOG.error("PortalException while saving ddmStructure",e);
        }
    }

    private void updateStructure(String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                    Map<Locale, String> descriptionMap, DDMStructure ddmStructure, String name){
        DDMForm ddmForm = createDDMForm(fileUrl, bundle);
        ddmStructure.setNameMap(nameMap);
        ddmStructure.setDescriptionMap(descriptionMap);
        ddmStructure.setDDMForm(ddmForm);
        ddmStructure.setVersion(String.valueOf(MathUtil.format(Double.valueOf(ddmStructure.getVersion()) + 0.1, 1, 1)));
        ddmStructureLocalService.updateDDMStructure(ddmStructure);
        LOG.info(String.format("Structure %s was updated", name));
    }

    private DDMFormLayout createDDMFormLayout(DDMForm ddmForm) {
        DDMFormLayout ddmFormLayout = DDMUtil.getDefaultDDMFormLayout(ddmForm);
        return ddmFormLayout;
    }

    private DDMStructure getStructure(Map<Locale, String> nameMap, long groupId,long classNameId){
        List<DDMStructure> structures = ddmStructureLocalService.getStructures(groupId, classNameId);
        DDMStructure ddmStructure = null;
        for(DDMStructure structure: structures){
            for(Map.Entry<Locale, String> name: nameMap.entrySet()){
                if(structure.getNameCurrentValue().equalsIgnoreCase(name.getValue())){
                    ddmStructure = structure;
                }
            }
        }
        return ddmStructure;
    }

    private DDMForm createDDMForm(String fileUrl, Bundle bundle) {
        DDMForm ddmForm = null;
        String content = getContentFromBundle(fileUrl, bundle);
        try {
            ddmForm = DDMUtil.getDDMForm(content);
        } catch (PortalException e) {
            LOG.error("PortalException when creating DDMForm", e);
        }
        return ddmForm;
    }

    private String getContentFromBundle(String fileUrl, Bundle bundle){
        URL url = bundle.getResource(fileUrl);
        String structure = "";
        InputStream input;

        try {
            input = url.openStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.defaultCharset()))) {
                structure = br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            finally {
                input.close();
            }
        }

        catch (IOException e) {
            LOG.error("IOException while reading input for structure "+ fileUrl + " " +e);
        }
        return structure;
    }
}
