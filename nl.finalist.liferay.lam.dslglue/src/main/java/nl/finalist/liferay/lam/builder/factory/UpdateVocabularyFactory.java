package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Vocabulary;

public class UpdateVocabularyFactory extends AbstractFactory {

    private static final Log LOG = LogFactoryUtil.getLog(UpdateVocabularyFactory.class);
    Vocabulary vocabularyService;

    public UpdateVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        String vocabularyName = (String) attributes.get("name");
        String languageId = (String) attributes.get("forLanguage");
        String translatedName = (String) attributes.get("translation");

        vocabularyService.updateVocabularyTranslation(languageId, translatedName, vocabularyName);

        LOG.info(String.format("Vocabulary %s updated with translation %s for language %s", vocabularyName, translatedName,languageId));
        return null;
    }

}
