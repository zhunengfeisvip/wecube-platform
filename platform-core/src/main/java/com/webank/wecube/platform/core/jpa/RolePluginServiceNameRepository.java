package com.webank.wecube.platform.core.jpa;

import com.webank.wecube.platform.core.domain.RolePluginServiceName;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RolePluginServiceNameRepository extends CrudRepository<RolePluginServiceName, String> {

    Optional<List<RolePluginServiceName>> findAllByRoleNameIn(List<String> roleNameList);

    Boolean existsRolePluginInterfaceByRoleNameAndPluginServiceName(String roleName, String pluginServiceName);

    List<RolePluginServiceName> findByRoleNameAndPluginServiceName(String roleName, String pluginServiceName);

    List<RolePluginServiceName> deleteByPluginServiceNameAndRoleNameIn(String pluginServiceName,
            List<String> roleNameList);

    Optional<List<RolePluginServiceName>> findAllByPluginServiceName(String pluginServiceName);
}
