package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.RoleAndPermissions;
import nl.finalist.liferay.lam.dslglue.model.RoleModel;

class CreateRoleAndPermissionsFactory extends AbstractFactory {

    RoleAndPermissions roleAndPermissionsService;

    CreateRoleAndPermissionsFactory(RoleAndPermissions roleAndPermissions) {
        this.roleAndPermissionsService = roleAndPermissions;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new RoleModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        RoleModel model = (RoleModel) node;
        
            roleAndPermissionsService.addCustomRoleAndPermission(model.name, model.type, model.titles,
                model.descriptions, model.permissions);
    }
}
