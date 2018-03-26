package nl.finalist.liferay.lam.builder.factory.delete

import nl.finalist.liferay.lam.api.User
import nl.finalist.liferay.lam.dslglue.model.UserModel;

class DeleteUserFactory extends AbstractFactory{
    User userService;

    DeleteUserFactory(User userService){
        this.userService = userService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new UserModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        UserModel model = (UserModel) node;
        userService.deleteUser(model.screenName);
    }
}
