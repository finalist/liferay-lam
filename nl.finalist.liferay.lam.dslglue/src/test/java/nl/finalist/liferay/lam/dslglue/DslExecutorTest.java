package nl.finalist.liferay.lam.dslglue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.finalist.liferay.lam.api.CustomFields;

import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
    public void testRun() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        FileReader file = new FileReader(classLoader.getResource("test.groovy").getFile());

        dslExecutor.runScripts(file);
//        verify(customFields).deleteCustomField(0, "", "");
    }
}
