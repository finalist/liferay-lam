package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields
import nl.finalist.liferay.lam.api.RoleAndPermissions;
import nl.finalist.liferay.lam.api.UserGroups;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.builder.factory.CreateRoleAndPermissionsFactory;
import nl.finalist.liferay.lam.builder.factory.CreateUserGroupFactory;
import nl.finalist.liferay.lam.api.Category;
import nl.finalist.liferay.lam.builder.factory.CreateVocabularyFactory;
import nl.finalist.liferay.lam.builder.factory.CreateCustomFieldsFactory;
import nl.finalist.liferay.lam.builder.factory.CreateCategoryFactory;
import nl.finalist.liferay.lam.builder.factory.CreateWebContentFactory;

class CreateFactoryBuilder extends FactoryBuilderSupport {

    CreateFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Category categoryService, UserGroups userGroupsService, RoleAndPermissions roleAndPermissionsService, WebContent webContentService) {
        registerFactory("customField", new CreateCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new CreateVocabularyFactory(vocabularyService));
        registerFactory("category", new CreateCategoryFactory(categoryService));
        registerFactory("userGroup", new CreateUserGroupFactory(userGroupsService));
        registerFactory("role", new CreateRoleAndPermissionsFactory(roleAndPermissionsService));
        registerFactory("webcontent", new CreateWebContentFactory(webContentService));
    }
}
