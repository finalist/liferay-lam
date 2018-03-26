package nl.finalist.liferay.lam.builder.factory.createOrUpdate

import nl.finalist.liferay.lam.api.model.PageModel
import nl.finalist.liferay.lam.api.model.ColumnModel

class ColumnFactory extends AbstractFactory {

    ColumnFactory() {
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new ColumnModel(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        if (parent instanceof PageModel) {
            PageModel page = (PageModel)parent;
            page.addColumn((ColumnModel)node);
        } 
    }
}
