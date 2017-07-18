package nl.finalist.liferay.lam.builder.factory;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Vocabulary;

public class DeleteVocabularyFactory extends AbstractFactory{

    Vocabulary vocabularyService;

    public DeleteVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        String vocabularyName = (String) attributes.get("name");
        vocabularyService.deleteVocabulary(vocabularyName);
        String.format("Vocabulary %s deletion completed", vocabularyName);
        return null;
    }

}
