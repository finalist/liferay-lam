package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.model.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ClassNameLocalService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.Bundle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TemplateImplTest {

    @Mock
    private ClassNameLocalService classNameLocalService;

    @Mock
    private DefaultValue defaultValue;

    @Mock
    private DDMStructureLocalService ddmStructureLocalService;

    @Mock
    private DDMTemplateLocalService ddmTemplateLocalService;

    @InjectMocks
    private TemplateImpl template;

    @Before
    public void setUp() {
        template = new TemplateImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createADT() throws PortalException {

        Bundle bundle = mock(Bundle.class);
        when(bundle.getResource(any()))
            .thenReturn(TemplateImplTest.class.getResource("/TemplateImplTestCreateADT_template.html"));

        template.createOrUpdateTemplate("myADTKey", "/TemplateImplTestCreateADT_template.html", bundle, null, null,
            null);
        verify(ddmTemplateLocalService).addTemplate(anyLong(), anyLong(), anyLong(), anyLong(),
            anyLong(), eq("myADTKey"), any(), any(),  eq(DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY),
            eq(DDMTemplateConstants.TEMPLATE_MODE_CREATE), anyString(),
            anyString(), eq(false), eq(false), anyString(), any(), any());
    }

}