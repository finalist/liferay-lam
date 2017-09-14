package nl.finalist.liferay.lam.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.component.annotations.Reference;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

public class StructureImpl implements Structure {
	private static final Log LOG = LogFactoryUtil.getLog(StructureImpl.class);
	@Reference
	private DDMStructureLocalService ddmStructureLocalService;
	@Reference
	private DefaultValue defaultValue;

	@Override
	public void createStructure(File structureJson) throws FileNotFoundException, IOException, ParseException {
		Map<Locale, String> nameMap = new HashMap<>();
		Map<Locale, String> descriptionMap = new HashMap<>();
		long classNameId = 0L;
		try {
			ddmStructureLocalService.addStructure(defaultValue.getDefaultUserId(), defaultValue.getGlobalGroupId(),
					null, classNameId, null, nameMap, descriptionMap, createDDMForm(structureJson),
					createDDMFormLayout(structureJson), "json", 0, new ServiceContext());
		} catch (PortalException e) {
			e.printStackTrace();
		}

	}

	private DDMFormLayout createDDMFormLayout(File structureJson) {
		DDMFormLayout ddmFormLayout = DDMUtil.getDefaultDDMFormLayout(createDDMForm(structureJson));

		return ddmFormLayout;
	}

	private DDMForm createDDMForm(File structureJson) {
		DDMForm ddmForm = null;
		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray;
			
				jsonArray = (JSONArray) parser.parse(new FileReader(structureJson));
				ddmForm = DDMUtil.getDDMForm("");
			} catch (IOException | ParseException |PortalException e) {
				LOG.error("Exception when parsing structure JSON", e);
			}
		return ddmForm;
	}

}
