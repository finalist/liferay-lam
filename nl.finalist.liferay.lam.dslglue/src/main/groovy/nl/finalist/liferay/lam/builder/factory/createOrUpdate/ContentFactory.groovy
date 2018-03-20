package nl.finalist.liferay.lam.builder.factory.createOrUpdate

import nl.finalist.liferay.lam.api.model.Content
import nl.finalist.liferay.lam.api.model.Column

class ContentFactory extends AbstractFactory {

    ContentFactory() {
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new Content(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        if (parent instanceof Column) {
            Column column = (Column)parent;
            column.addContent((Content)node);
        } 
    }
}
