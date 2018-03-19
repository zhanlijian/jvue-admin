package net.ccfish.jvue.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.ccfish.common.jpa.JpaRestrictions;
import net.ccfish.common.jpa.SearchCriteria;
import net.ccfish.jvue.model.JvueMenu;
import net.ccfish.jvue.model.JvueModule;
import net.ccfish.jvue.repository.JvueMenuRepository;
import net.ccfish.jvue.repository.JvueModuleRepository;
import net.ccfish.jvue.service.JvueMenuService;
import net.ccfish.jvue.vm.ModuleAndMenus;

/**
 * Generated by Spring Data Generator on 31/01/2018
 */
@Service
@Transactional
public class JvueMenuServiceImpl implements JvueMenuService {

    @Autowired
    private JvueMenuRepository jvueMenuRepository;

    @Autowired
    private JvueModuleRepository jvueModuleRepository;

    @Override
    public JpaRepository<JvueMenu, Integer> jpaRepository() {
        return this.jvueMenuRepository;
    }

    public List<JvueMenu> findAllRootMenu() {
        return jvueMenuRepository.findByParentIdIsNull();
    }

    public ModuleAndMenus findModuleAndMenu() {

        ModuleAndMenus moduleAndMenus = new ModuleAndMenus();
        List<JvueMenu> menus = jvueMenuRepository.findByParentIdIsNull();

        List<Integer> moduleIds = menus.stream().map(menu -> menu.getModuleId()).distinct()
                .collect(Collectors.toList());

        List<JvueModule> modules = jvueModuleRepository.findAll(moduleIds);
        
        moduleAndMenus.setMenus(menus);
        moduleAndMenus.setModules(modules);

        return moduleAndMenus;
    }

    @Override
    public List<JvueMenu> findByModule(Integer moduleId) {

        SearchCriteria<JvueMenu> menuSearchCriteria = new SearchCriteria<>();
        menuSearchCriteria.add(JpaRestrictions.eq("moduleId", moduleId, false));
        menuSearchCriteria.add(JpaRestrictions.isNull("parentId"));
        
        return jvueMenuRepository.findAll(menuSearchCriteria);
    }
}
