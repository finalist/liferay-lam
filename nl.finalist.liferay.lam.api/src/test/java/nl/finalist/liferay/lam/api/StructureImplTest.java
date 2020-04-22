package nl.finalist.liferay.lam.api;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;

public class StructureImplTest {

    private static final String SITE_KEY = "TestSite";

    private static final String TEST_URL = "/TestURL.html";

    private static final String STRUCTURE_KEY = "Test";

    private static final long CLASSNAME_ID = 1L;

    private static final long GLOBAL_GROUP_ID = 1L;

    private static final long GROUP_ID = 2L;

    private static final long COMPANY_ID = 1L;

    private static final long USER_ID = 1L;

    @Mock
    private DDMStructureLocalService ddmStructureLocalService;

    @Mock
    private ClassNameLocalService classNameLocalService;

    @Mock
    private DDMStructureVersionLocalService ddmStructureVersionLocalService;

    @Mock
    private DefaultValue defaultValue;

    @Mock
    private GroupLocalService groupService;

    @Mock
    private DDM mockDDM;

    @InjectMocks
    private StructureImpl structure;

    @Mock
    private Bundle mockBundle;

    @Mock
    private Group mockGroup;

    @Mock
    private Company mockCompany;

    @Mock
    private DDMForm mockDDMForm;

    @Mock
    private DDMStructure mockDDMStructure;

    @Mock
    private DDMStructureVersion mockStructureVersion;

    private Map<Locale, String> nameMap = new HashMap<>();

    private Map<Locale, String> descriptionMap = new HashMap<>();

    @Before
    public void setup() throws PortalException {
        nameMap.put(Locale.US, "testName");
        descriptionMap.put(Locale.US, "testDescription");

        MockitoAnnotations.initMocks(this);

        when(classNameLocalService.getClassNameId(JournalArticle.class.getName())).thenReturn(CLASSNAME_ID);
        when(defaultValue.getGlobalGroupId()).thenReturn(GLOBAL_GROUP_ID);
        when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);
        when(defaultValue.getDefaultUserId()).thenReturn(USER_ID);
        when(mockCompany.getCompanyId()).thenReturn(COMPANY_ID);
        when(groupService.fetchGroup(COMPANY_ID, SITE_KEY)).thenReturn(mockGroup);
        when(mockGroup.getGroupId()).thenReturn(GROUP_ID);
        when(mockBundle.getResource(any())).thenReturn(TemplateImplTest.class.getResource("/TestURL.html"));

        when(mockDDM.getDDMForm(anyString())).thenReturn(mockDDMForm);
    }

    @Test
    public void shouldAddStructure_whenItDoesntExist() throws PortalException {
        when(ddmStructureLocalService.getStructure(GROUP_ID, CLASSNAME_ID, STRUCTURE_KEY)).thenThrow(new NoSuchStructureException());
        // updated method call to pass mocked company's web ID
        structure.createOrUpdateStructure(new String[] {mockCompany.getWebId()}, STRUCTURE_KEY, TEST_URL, mockBundle, nameMap, descriptionMap, null);

        verify(ddmStructureLocalService, times(1)).addStructure(eq(USER_ID), eq(GLOBAL_GROUP_ID), any(), eq(CLASSNAME_ID), eq(STRUCTURE_KEY),
                eq(nameMap), eq(descriptionMap), any(), any(), eq("json"), eq(0), any());
    }

    @Test
    public void shouldUpdateStructure_whenItExists() throws PortalException {
        when(ddmStructureLocalService.getStructure(anyLong(), anyLong(), anyString())).thenReturn(mockDDMStructure);
        when(mockDDMStructure.getLatestStructureVersion()).thenReturn(mockStructureVersion);
        when(mockDDMStructure.getVersion()).thenReturn("1.0");
        // updated method call to pass mocked company's web ID
        structure.createOrUpdateStructure(new String[] {mockCompany.getWebId()}, STRUCTURE_KEY, TEST_URL, mockBundle, nameMap, descriptionMap, null);

        verify(ddmStructureLocalService, times(1)).updateDDMStructure(any());
        verify(ddmStructureVersionLocalService, times(1)).updateDDMStructureVersion(any());
    }

    @Test
    public void shouldAddToGivenSite_whenSiteKeyIsSpecified() throws PortalException {
        when(ddmStructureLocalService.getStructure(GROUP_ID, CLASSNAME_ID, STRUCTURE_KEY)).thenThrow(new NoSuchStructureException());
        // updated method call to pass mocked company's web ID
        structure.createOrUpdateStructure(new String[] {mockCompany.getWebId()}, STRUCTURE_KEY, TEST_URL, mockBundle, nameMap, descriptionMap,
                SITE_KEY);

        verify(ddmStructureLocalService, times(1)).addStructure(eq(USER_ID), eq(GROUP_ID), any(), eq(CLASSNAME_ID), eq(STRUCTURE_KEY), eq(nameMap),
                eq(descriptionMap), any(), any(), eq("json"), eq(0), any());
    }
}
