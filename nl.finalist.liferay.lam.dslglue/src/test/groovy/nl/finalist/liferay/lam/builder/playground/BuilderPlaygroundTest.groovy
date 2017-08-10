package nl.finalist.liferay.lam.builder.playground

import org.junit.Test

/*

This is a playground / PoC to see the nested builder structure in action.
It lets you create Users, and within those, create custom field (instances).
Potentially, these CustomFieldInstances can also be nested into other
entities, like they can in normal Liferay.
 */


class BuilderPlaygroundTest {

    @Test
    void testDSL() {

        // Would be fed to the DslExecutor's binding
        def create = new PlaygroundCreateFactoryBuilder()

        create.user(
                firstName: 'Geert',
                screenName: 'geertpl',
                emailAddress: 'geert.van.der.ploeg@finalist.nl'
        ) {
            customFieldInstance name: 'field1', value: 'theValue1'
            customFieldInstance name: 'field2', value: 'theValue2'
            customFieldInstance name: 'field3', value: 'theValue3'
            customFieldInstance name: 'field4', value: 'theValue4'
        }
    }
}
