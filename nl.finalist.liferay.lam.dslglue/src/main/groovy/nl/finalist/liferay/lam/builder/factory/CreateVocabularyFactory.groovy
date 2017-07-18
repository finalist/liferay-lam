package nl.finalist.liferay.lam.builder.factory;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Vocabulary;

class CreateVocabularyFactory extends AbstractFactory  {
    Vocabulary vocabularyService;

     CreateVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        String vocabularyName = (String) attributes.get("name");
        vocabularyService.addVocabulary(vocabularyName);
        return attributes;
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
    }
}
