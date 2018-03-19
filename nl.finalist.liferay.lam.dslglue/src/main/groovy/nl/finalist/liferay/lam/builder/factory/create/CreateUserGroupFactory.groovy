package nl.finalist.liferay.lam.builder.factory.create

import nl.finalist.liferay.lam.api.UserGroups
import nl.finalist.liferay.lam.dslglue.model.UserGroupModel;

class CreateUserGroupFactory extends AbstractFactory  {
    UserGroups userGroupsService;

    CreateUserGroupFactory(UserGroups userGroupsService) {
        this.userGroupsService = userGroupsService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new UserGroupModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        UserGroupModel model = (UserGroupModel) node;
        userGroupsService.addUserGroup(model.name, model.description, model.customFields);
    }
}
