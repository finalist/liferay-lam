package nl.finalist.liferay.lam.builder.playground

class PlaygroundCreateFactoryBuilder extends FactoryBuilderSupport {

    // Need to specify constructor (with default 'true' for init) otherwise
    // register-method will not be triggered
    PlaygroundCreateFactoryBuilder(boolean init=true) {
        super(init)
    }

    def registerFactories() {
        println "registering factories"
        registerFactory("user", new CreateUserFactory());
        registerFactory("customFieldInstance", new CreateCustomFieldInstanceFactory());
    }


}
