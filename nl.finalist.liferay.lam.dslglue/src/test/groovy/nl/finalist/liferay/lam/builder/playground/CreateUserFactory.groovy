package nl.finalist.liferay.lam.builder.playground

class CreateUserFactory extends AbstractFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        new User(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        println "Here we would create user with custom fields: " + node
    }
}
