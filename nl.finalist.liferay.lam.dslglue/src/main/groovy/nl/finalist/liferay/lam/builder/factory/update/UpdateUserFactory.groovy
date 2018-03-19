package nl.finalist.liferay.lam.builder.factory
import nl.finalist.liferay.lam.api.User;
import nl.finalist.liferay.lam.dslglue.model.UserModel;


class UpdateUserFactory extends AbstractFactory {

    User userService;

    UpdateUserFactory(User userService) {
        this.userService = userService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new UserModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        UserModel model = (UserModel) node;
        userService.updateUser(model.screenName, model.newScreenName, model.emailAddress, model.firstName, model.lastName, model.roles,model.sites,model.userGroups);
    }
}