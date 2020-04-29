package com.webank.wecube.platform.core.dto;

public class queryAvailableInterfacesForProcessDefinitionDto {
    private String targetEntityFilterRule;
    private String processOwnerRoleList;

    public String getTargetEntityFilterRule() {
        return targetEntityFilterRule;
    }

    public void setTargetEntityFilterRule(String targetEntityFilterRule) {
        this.targetEntityFilterRule = targetEntityFilterRule;
    }
}
