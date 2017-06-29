package nl.finalist.liferay.lam.dslglue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.finalist.liferay.lam.api.CustomFields;

public class DslExecutorTest {

    @Mock
    private CustomFields customFields;

    @InjectMocks
    private DslExecutor dslExecutor;

    @Before
    public void setUp() {
        dslExecutor = new DslExecutor();
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testCustomFields() {
        dslExecutor.runScripts("customFields.groovy");
    }
}
