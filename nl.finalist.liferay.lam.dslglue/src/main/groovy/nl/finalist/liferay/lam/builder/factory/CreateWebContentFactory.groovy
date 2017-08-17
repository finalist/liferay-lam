package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.dslglue.model.WebContentModel;
import nl.finalist.liferay.lam.dslglue.LocaleMapConverter;
class CreateWebContentFactory extends AbstractFactory {
	WebContent webContentService;
	
	CreateWebContentFactory(WebContent webContentService) {
		this.webContentService = webContentService;
	}
	
	 @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new WebContentModel(attributes);
    }
    
     @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        WebContentModel webContent = (WebContentModel) node;
        webContentService.addWebContent(LocaleMapConverter.convert(webContent.titleMap), 
        								LocaleMapConverter.convert(webContent.descriptionMap), webContent.content, webContent.urlTitle);
    }
}