package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.dslglue.model.VocabularyModel;
import nl.finalist.liferay.lam.dslglue.LocaleMapConverter;

class CreateVocabularyFactory extends AbstractFactory  {
    Vocabulary vocabularyService;

     CreateVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new VocabularyModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        VocabularyModel vocabulary = (VocabularyModel) node;
        vocabularyService.addVocabulary(LocaleMapConverter.convert(vocabulary.name));
    }
}
