package nl.finalist.liferay.lam.builder.factory.createOrUpdate

import nl.finalist.liferay.lam.api.model.ColumnModel
import nl.finalist.liferay.lam.api.model.ContentModel

class ContentFactory extends AbstractFactory {

    ContentFactory() {
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new ContentModel(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        if (parent instanceof ColumnModel) {
            ColumnModel column = (ColumnModel)parent;
            column.addContent((ContentModel)node);
        } 
    }
}
