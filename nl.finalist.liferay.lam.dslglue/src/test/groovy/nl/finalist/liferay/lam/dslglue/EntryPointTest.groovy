package nl.finalist.liferay.lam.dslglue

import org.junit.Test

class EntryPointTest {

    @Test
    void testWith() {

        Entrypoint.with(groupId: 123) {
            //println owner.groupId

//            println "groupId tijdens uitvoeren van dsl: ${owner.groupId}"

        }
    }
}
