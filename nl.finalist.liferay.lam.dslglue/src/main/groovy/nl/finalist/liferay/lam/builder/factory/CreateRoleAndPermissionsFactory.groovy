package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.dslglue.model.RoleAndPermissionsModel;

import java.util.ArrayList;
import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.RoleAndPermissions;
import nl.finalist.liferay.lam.dslglue.model.RoleAndPermissionsModel;

class CreateRoleAndPermissionsFactory extends AbstractFactory {

    RoleAndPermissions roleAndPermissionsService;

    CreateRoleAndPermissionsFactory(RoleAndPermissions roleAndPermissions) {
        this.roleAndPermissionsService = roleAndPermissions;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new RoleAndPermissionsModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        RoleAndPermissionsModel model = (RoleAndPermissionsModel) node;
        
            roleAndPermissionsService.addCustomRoleAndPermission(model.roleName, model.roleType, model.titles,
                model.descriptions, model.permissions);
    }
}
