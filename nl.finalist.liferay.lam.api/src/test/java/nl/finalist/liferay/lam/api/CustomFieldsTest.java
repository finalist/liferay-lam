package nl.finalist.liferay.lam.api;


import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CustomFieldsTest {

    @Mock
    private ExpandoValueLocalService evls;

    @InjectMocks
    private CustomFields customFields;

    @Before
    public void setUp() {
        customFields = new CustomFields();
        initMocks(this);
    }

    @Test
    public void test() {
        customFields.createCustomField();
        verify(evls).addExpandoValue(null);
    }
}