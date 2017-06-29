package nl.finalist.liferay.lam.dslglue

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.junit.Test
import static org.junit.Assert.*

class EntryPointTest {

    @Test
    void testWith() {

        def theGroupIdWithinTheClosure;

        new Entrypoint().with(groupId: 321, siteName: "some site") {

            theGroupIdWithinTheClosure = groupId
            println groupId
            println siteName
        }


        assertEquals(321, theGroupIdWithinTheClosure)
    }

    @Test
    void testDslWith() {

        Binding sharedData = new Binding()

        CompilerConfiguration conf = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();
        conf.addCompilationCustomizers(imports);

        imports.addStaticImport("nl.finalist.liferay.lam.dslglue.Entrypoint", "with");
        GroovyShell shell = new GroovyShell(sharedData, conf);
        shell.evaluate("""

                with groupId: 123, siteName: "some site", {
                    println "groupId should be 123: \${groupId}"
                    println "site name should be 'some site': '\${siteName}'"
                }

        """)

    }
}
