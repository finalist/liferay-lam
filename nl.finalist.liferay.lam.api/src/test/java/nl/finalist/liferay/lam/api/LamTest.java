package nl.finalist.liferay.lam.api;


import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LamTest {

    @Mock
    private ExpandoValueLocalService evls;

    @InjectMocks
    private Lam lam;

    @Before
    public void setUp() {
        lam = new Lam();
        initMocks(this);
    }

    @Test
    public void test() {
        lam.createCustomField();
        verify(evls).addExpandoValue(null);
    }
}