package com.webank.wecube.platform.core.service.plugin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webank.wecube.platform.core.domain.RolePluginServiceName;

@Service
public class PluginServiceAuthorityService {



    public void addPluginServiceAuthorityToRoles(String pluginServiceName, List<String> roleList) {
        
    }

    public void removePluginServiceAuthorityByRoles(String procId, List<String> roleList) {
        
    }
    
    public List<RolePluginServiceName> getPluginServiceAuthoritiesByPluginServiceName(String pluginServiceName) {
        
        return null;
    }
    
    public List<RolePluginServiceName> getAllPluginServiceAuthoritiesByRoleList(List<String> roleIdList){
        
        return null;
    }

    public List<RolePluginServiceName> getPluginServiceAuthoritiesByRoleListAndPermission(List<String> roleIdList, String permissionStr){
        return null;
    }
}