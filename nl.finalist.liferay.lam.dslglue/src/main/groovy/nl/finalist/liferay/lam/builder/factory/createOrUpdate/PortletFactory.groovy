package nl.finalist.liferay.lam.builder.factory.createOrUpdate

import nl.finalist.liferay.lam.api.model.Column
import nl.finalist.liferay.lam.api.model.Portlet

class PortletFactory extends AbstractFactory {

    PortletFactory() {
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new Portlet(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        if (parent instanceof Column) {
            Column column = (Column)parent;
            column.addPortlet((Portlet)node);
        } 
    }
}
