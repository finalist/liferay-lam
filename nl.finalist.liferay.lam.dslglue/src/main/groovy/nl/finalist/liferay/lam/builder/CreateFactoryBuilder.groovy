package nl.finalist.liferay.lam.builder

import nl.finalist.liferay.lam.api.*
import nl.finalist.liferay.lam.builder.factory.create.CreateCategoryFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateCustomFieldsFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateRoleAndPermissionsFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateSiteFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateTagFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateUserFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateUserGroupFactory
import nl.finalist.liferay.lam.builder.factory.create.CreateVocabularyFactory

class CreateFactoryBuilder extends FactoryBuilderSupport {

    CreateFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Site siteService, Category
            categoryService, UserGroups userGroupsService, RoleAndPermissions roleAndPermissionsService, Tag tagService, User userService) {

        registerFactory("customField", new CreateCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new CreateVocabularyFactory(vocabularyService));
        registerFactory("site", new CreateSiteFactory(siteService))
        registerFactory("category", new CreateCategoryFactory(categoryService));
        registerFactory("userGroup", new CreateUserGroupFactory(userGroupsService));
        registerFactory("role", new CreateRoleAndPermissionsFactory(roleAndPermissionsService));
        registerFactory("tag", new CreateTagFactory(tagService));
        registerFactory("user", new CreateUserFactory(userService));
    }
   
}
