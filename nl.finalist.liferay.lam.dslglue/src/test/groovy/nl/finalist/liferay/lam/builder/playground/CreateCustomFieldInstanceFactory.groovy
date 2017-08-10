package nl.finalist.liferay.lam.builder.playground


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
