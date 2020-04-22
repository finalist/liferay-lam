package nl.finalist.liferay.lam.builder.factory.create
import nl.finalist.liferay.lam.api.User;
import nl.finalist.liferay.lam.dslglue.model.UserModel;


class CreateUserFactory extends AbstractFactory {

    User userService;

    CreateUserFactory(User userService) {
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
        // Method call updated to use webIds available in groovy model
        userService.createUser(model.webIds, model.screenName, model.emailAddress, model.firstName, model.lastName, model.roles, model.sites, model.userGroups, model.customFields);
    }
}