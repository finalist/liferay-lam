package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.api.CustomFields
import nl.finalist.liferay.lam.api.RoleAndPermissions;
import nl.finalist.liferay.lam.api.UserGroups;
import nl.finalist.liferay.lam.api.Vocabulary
import nl.finalist.liferay.lam.builder.factory.CreateCustomFieldsFactory;
import nl.finalist.liferay.lam.builder.factory.CreateRoleAndPermissionsFactory;
import nl.finalist.liferay.lam.builder.factory.CreateSiteFactory;
import nl.finalist.liferay.lam.builder.factory.CreateUserGroupFactory;
import nl.finalist.liferay.lam.builder.factory.CreateVocabularyFactory;

class CreateFactoryBuilder extends FactoryBuilderSupport {

    CreateFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Site siteService, UserGroups userGroupsService, RoleAndPermissions roleAndPermissionsService) {
        registerFactory("customField", new CreateCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new CreateVocabularyFactory(vocabularyService));
        registerFactory("site", new CreateSiteFactory(siteService))
        registerFactory("userGroup", new CreateUserGroupFactory(userGroupsService));
        registerFactory("role", new CreateRoleAndPermissionsFactory(roleAndPermissionsService));
    }
}
