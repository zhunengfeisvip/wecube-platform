package com.webank.wecube.platform.core.service.plugin;

import com.google.common.collect.Lists;
import com.webank.wecube.platform.core.DatabaseBasedTest;
import com.webank.wecube.platform.core.commons.WecubeCoreException;
import com.webank.wecube.platform.core.domain.RolePluginServiceName;
import com.webank.wecube.platform.core.domain.plugin.PluginConfig;
import com.webank.wecube.platform.core.domain.plugin.PluginConfigInterface;
import com.webank.wecube.platform.core.domain.plugin.PluginPackage;
import com.webank.wecube.platform.core.jpa.PluginPackageRepository;
import com.webank.wecube.platform.core.jpa.RolePluginServiceNameRepository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static com.webank.wecube.platform.core.domain.plugin.PluginConfig.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class PluginServiceAuthorityServiceTest extends DatabaseBasedTest {
    @Autowired
    private PluginServiceAuthorityService pluginServiceAuthorityService;

    @Autowired
    private PluginPackageRepository pluginPackageRepository;
    @Autowired
    private RolePluginServiceNameRepository rolePluginServiceNameRepository;

    private static final String SERVICENAME1 = "test-package/vm/create";
    private static final String SERVICENAME2 = "test-package-2/vm/create";

    private void mockPluginSeviceData1() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PluginPackage pkg = new PluginPackage(null, "test-package", "v1.0.0", PluginPackage.Status.REGISTERED, now,
                false);
        PluginConfig cfg = new PluginConfig(null, pkg, "vm", null, null, ENABLED, null);
        PluginConfigInterface inf = new PluginConfigInterface(null, cfg, "create", SERVICENAME1,
                "test-package/vm/create", "/test-package/vm/create", "POST", null, null);
        cfg.setInterfaces(newHashSet(inf));
        pkg.setPluginConfigs(newHashSet(cfg));
        pluginPackageRepository.save(pkg);
    }

    private void mockPluginSeviceData2() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        PluginPackage pkg = new PluginPackage(null, "test-package-2", "v1.0.0", PluginPackage.Status.REGISTERED, now,
                false);
        PluginConfig cfg = new PluginConfig(null, pkg, "vm", null, null, ENABLED, null);
        PluginConfigInterface inf = new PluginConfigInterface(null, cfg, "create", SERVICENAME2,
                "test-package-2/vm/create", "/test-package-2/vm/create", "POST", null, null);
        cfg.setInterfaces(newHashSet(inf));
        pkg.setPluginConfigs(newHashSet(cfg));
        pluginPackageRepository.save(pkg);
    }

    private void mockPluginServiceAuthorityData1() {
        mockPluginSeviceData1();
        RolePluginServiceName rolePluginServiceName3 = new RolePluginServiceName("role3", SERVICENAME1,"USE");
        RolePluginServiceName rolePluginServiceName4 = new RolePluginServiceName("role4", SERVICENAME1,"USE");
        RolePluginServiceName rolePluginServiceName5 = new RolePluginServiceName("role5", SERVICENAME1,"USE");
        rolePluginServiceNameRepository
                .saveAll(Lists.newArrayList(rolePluginServiceName3, rolePluginServiceName4, rolePluginServiceName5));
    }

    private void mockPluginServiceAuthorityData2() {
        mockPluginSeviceData2();
        RolePluginServiceName rolePluginServiceName6 = new RolePluginServiceName("role6", SERVICENAME2,"MGMT");
        RolePluginServiceName rolePluginServiceName7 = new RolePluginServiceName("role7", SERVICENAME2,"USE");
        rolePluginServiceNameRepository.saveAll(Lists.newArrayList(rolePluginServiceName6, rolePluginServiceName7));
    }

    @Test
    public void addPluginServiceAuthorityToRolesSucceedTest() {
        mockPluginSeviceData1();
        List<String> roleList = Lists.newArrayList("role1", "role2");
        pluginServiceAuthorityService.addPluginServiceAuthorityToRoles(SERVICENAME1, roleList);

        Optional<List<RolePluginServiceName>> result = rolePluginServiceNameRepository
                .findAllByPluginServiceName(SERVICENAME1);

        assertThat(result.isPresent());
        assertThat(result.get().size()).isEqualTo(2);
        assertThat(roleList).contains(result.get().get(0).getRoleName());
        assertThat(roleList).contains(result.get().get(1).getRoleName());
    }

    @Test
    public void addPluginServiceAuthorityToRolesFailedBySeviceNameNotFoundTest() {
        mockPluginSeviceData1();
        List<String> roleList = Lists.newArrayList("role1", "role2");
        try {
            pluginServiceAuthorityService.addPluginServiceAuthorityToRoles("NotExistServiceName", roleList);
            fail("Test should fail as SeviceName Not Found error");
        } catch (WecubeCoreException ex) {
            assertThat(ex.getMessage()).contains("SeviceName Not Found");
        }
    }

    @Test
    public void removePluginServiceAuthorityByRolesSucceedTest() {
        mockPluginServiceAuthorityData1();
        List<String> roleList = Lists.newArrayList("role4", "role5");
        pluginServiceAuthorityService.removePluginServiceAuthorityByRoles(SERVICENAME1, roleList);

        Optional<List<RolePluginServiceName>> result = rolePluginServiceNameRepository
                .findAllByPluginServiceName(SERVICENAME1);

        assertThat(result.isPresent());
        assertThat(result.get().size()).isEqualTo(1);
        assertThat(result.get().get(0).getRoleName()).isNotIn(roleList);
    }

    @Test
    public void removePluginServiceAuthorityByRolesFailedBySeviceNameNotFoundTest() {
        mockPluginServiceAuthorityData1();
        List<String> roleList = Lists.newArrayList("role4", "role5");
        try {
            pluginServiceAuthorityService.removePluginServiceAuthorityByRoles("NotExistServiceName", roleList);
            fail("Test should fail as SeviceName Not Found error");
        } catch (WecubeCoreException ex) {
            assertThat(ex.getMessage()).contains("SeviceName Not Found");
        }
    }

    @Test
    public void getRoleListByPluginServiceNameSucceedTest() {
        mockPluginServiceAuthorityData1();
        List<String> roleList = Lists.newArrayList("role3", "role4", "role5");

        List<RolePluginServiceName> rolePluginServiceNames = pluginServiceAuthorityService
                .getPluginServiceAuthoritiesByPluginServiceName(SERVICENAME1);

        assertThat(rolePluginServiceNames).hasSize(3);
        assertThat(rolePluginServiceNames.get(0).getRoleName()).isIn(roleList);
        assertThat(rolePluginServiceNames.get(1).getRoleName()).isIn(roleList);
        assertThat(rolePluginServiceNames.get(2).getRoleName()).isIn(roleList);
    }

    @Test
    public void getAllPluginServiceNamesByRoleSucceedListTest() {
        mockPluginServiceAuthorityData1();
        mockPluginServiceAuthorityData2();
        List<String> roleList = Lists.newArrayList("role3", "role6");
        List<String> serviceNameList = Lists.newArrayList(SERVICENAME1, SERVICENAME2);
        List<RolePluginServiceName> rolePluginServiceNames = pluginServiceAuthorityService
                .getAllPluginServiceAuthoritiesByRoleList(roleList);

        assertThat(rolePluginServiceNames).hasSize(5);
        assertThat(rolePluginServiceNames.get(0).getPluginServiceName()).isIn(serviceNameList);
        assertThat(rolePluginServiceNames.get(1).getPluginServiceName()).isIn(serviceNameList);
        assertThat(rolePluginServiceNames.get(2).getPluginServiceName()).isIn(serviceNameList);
        assertThat(rolePluginServiceNames.get(3).getPluginServiceName()).isIn(serviceNameList);
        assertThat(rolePluginServiceNames.get(4).getPluginServiceName()).isIn(serviceNameList);
    }

    @Test
    public void getPluginServiceNameByRoleListAndPermissionSucceedTest() {
        mockPluginServiceAuthorityData1();

        List<String> roleList = Lists.newArrayList("role5", "role6");

        List<RolePluginServiceName> rolePluginServiceNames = pluginServiceAuthorityService
                .getPluginServiceAuthoritiesByRoleListAndPermission(roleList, "USE");

        assertThat(rolePluginServiceNames).hasSize(4);
    }

}