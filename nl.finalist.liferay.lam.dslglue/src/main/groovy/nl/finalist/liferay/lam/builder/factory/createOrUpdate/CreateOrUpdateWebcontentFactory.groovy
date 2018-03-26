package nl.finalist.liferay.lam.builder.factory.createOrUpdate
import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.util.LocaleMapConverter;
import nl.finalist.liferay.lam.dslglue.model.WebContentModel;

import org.osgi.framework.Bundle;


class CreateOrUpdateWebcontentFactory extends AbstractFactory {

    WebContent webContentService;
    Bundle bundle;

    CreateOrUpdateWebcontentFactory(WebContent webContentService, Bundle bundle) {
        this.webContentService = webContentService;
        this.bundle = bundle;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new WebContentModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        WebContentModel model = (WebContentModel) node;
        webContentService.createOrUpdateWebcontent(model.id, model.forSite, LocaleMapConverter.convert(model
                .titleMap), model.file, bundle , model.urlTitle, model.structure, model.template);
    }
}