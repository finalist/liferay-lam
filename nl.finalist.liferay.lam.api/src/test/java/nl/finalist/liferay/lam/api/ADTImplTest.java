package nl.finalist.liferay.lam.api;

import com.liferay.dynamic.data.mapping.exception.NoSuchTemplateException;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.exception.PortalException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ADTImplTest {

    @Mock
     private DDMTemplateLocalService ddmTemplateLocalService;

    @InjectMocks
    private ADTImpl adtImpl;

    @Before
    public void setUp() {
        adtImpl = new ADTImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getADT_happy() throws PortalException {
        DDMTemplate ddmTemplate = mock(DDMTemplate.class);
        when(ddmTemplateLocalService.getTemplate(anyLong(), anyLong(), anyString())).thenReturn(ddmTemplate);
        DDMTemplate result = adtImpl.getADT("", 0L, 0L);
        assertEquals(ddmTemplate, result);
    }

    @Test
    public void getADT_NoSuchTemplateException() throws PortalException {
        when(ddmTemplateLocalService.getTemplate(anyLong(), anyLong(), anyString())).thenThrow(NoSuchTemplateException.class);
        DDMTemplate adt = adtImpl.getADT("", 0L, 0L);
        assertNull(adt);
    }

    @Test
    public void getADT_PortalException() throws PortalException {
        when(ddmTemplateLocalService.getTemplate(anyLong(), anyLong(), anyString())).thenThrow(PortalException.class);
        DDMTemplate adt = adtImpl.getADT("", 0L, 0L);
        assertNull(adt);
    }
}