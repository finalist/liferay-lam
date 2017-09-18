package nl.finalist.liferay.lam.builder.factory
import nl.finalist.liferay.lam.api.Template;
import nl.finalist.liferay.lam.dslglue.LocaleMapConverter;
import nl.finalist.liferay.lam.dslglue.model.TemplateModel;
import org.osgi.framework.Bundle;


class CreateOrUpdateTemplateFactory extends AbstractFactory {

    Template templateService;
    Bundle bundle;

    CreateOrUpdateTemplateFactory(Template templateService, Bundle bundle) {
        this.templateService = templateService;
        this.bundle = bundle;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new TemplateModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        TemplateModel model = (TemplateModel) node;
        
      
        templateService.createOrUpdateTemplate(model.file, bundle ,model.forStructure, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap));
    }
}