package com.webank.wecube.platform.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webank.wecube.platform.core.utils.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role_plugin_service_name")
public class RolePluginServiceName {

    @Id
    @JsonIgnore
    private String id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "plugin_service_name")
    private String pluginServiceName;

    public RolePluginServiceName(String roleName, String pluginServiceName) {
        this.roleName = roleName;
        this.pluginServiceName = pluginServiceName;
    }

    public RolePluginServiceName() {
    }

    @PrePersist
    public void initGuid() {
        if (this.id == null || "".equals(this.id)) {
            this.id = Objects.requireNonNull(this.roleName, "The [roleName] cannot be NULL while persisting [role_plugin_service_name]")
                    + Constants.KEY_COLUMN_DELIMITER
                    + Objects.requireNonNull(this.pluginServiceName, "The [pluginServiceName] cannot be NULL while persisting [role_plugin_service_name]");
        }
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
}
