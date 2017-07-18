package nl.finalist.liferay.lam.builder.factory;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Vocabulary;

class UpdateVocabularyFactory extends AbstractFactory {

    Vocabulary vocabularyService;

    UpdateVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        String vocabularyName = (String) attributes.get("name");
        String languageId = (String) attributes.get("forLanguage");
        String translatedName = (String) attributes.get("translation");

        vocabularyService.updateVocabularyTranslation(languageId, translatedName, vocabularyName);

        return null;
    }
}
