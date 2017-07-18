package nl.finalist.liferay.lam

import org.junit.Ignore
import org.junit.Test

import groovy.transform.ToString
/*

This is a playground / PoC to see the nested builder structure in action.
It lets you create Users, and within those, create custom field (instances).
Potentially, these CustomFieldInstances can also be nested into other
entities, like they can in normal Liferay too.

Below, all classes are defined in one go, to have a self contained example. If
we adopt this, it should of course be expanded into proper separate classes/files.
 */


class BuilderPlaygroundTest {

    @ToString
    class CustomFieldInstance {
        String name
        Object value
    }

    @ToString
    class User {
        String screenName
        String firstName
        String emailAddress

        List<CustomFieldInstance> customFields = new ArrayList<>()

        void addCustomFieldInstance(CustomFieldInstance customFieldInstance) {
            customFields += customFieldInstance
        }
    }

    class CreateUserFactory extends AbstractFactory {

        @Override
        Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
            new User(attributes)
        }

        @Override
        void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
            println "About to create user with custom fields: " + node
        }
    }

    class CreateCustomFieldInstanceFactory extends AbstractFactory {

        @Override
        Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
            new CustomFieldInstance(attributes)
        }

        @Override
        boolean isLeaf() {
            return true
        }

        @Override
        void setParent(FactoryBuilderSupport builder, Object parent, Object child) {

            if (parent instanceof User) {
                parent.addCustomFieldInstance(child)
            }
        }
    }


    class PlaygroundCreateFactoryBuilder extends FactoryBuilderSupport {



        void registerFactories() {
            registerFactory("user", new CreateUserFactory());
            registerFactory("customFieldInstance", new CreateCustomFieldInstanceFactory());
        }


    }

    void testDSL() {

        // Would be fed to the DslExecutor's binding
        def create = new PlaygroundCreateFactoryBuilder()

        // The actual resultant DSL:
        create.user (
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
