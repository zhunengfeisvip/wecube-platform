package com.webank.wecube.platform.core.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role_plugin_service_name")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class RolePluginServiceName {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id", length = 32)
    @JsonIgnore
    private String id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "plugin_service_name")
    private String pluginServiceName;

    @Column(name = "permission")
    private String permission;

    public RolePluginServiceName(String roleName, String pluginServiceName, String permission) {
        this.roleName = roleName;
        this.pluginServiceName = pluginServiceName;
        this.permission = permission;
    }

    public RolePluginServiceName() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPluginServiceName() {
        return pluginServiceName;
    }

    public void setPluginServiceName(String pluginServiceName) {
        this.pluginServiceName = pluginServiceName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
