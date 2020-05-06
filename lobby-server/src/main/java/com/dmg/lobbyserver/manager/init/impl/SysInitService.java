package com.dmg.lobbyserver.manager.init.impl;

import com.dmg.lobbyserver.manager.VersionManager;
import com.dmg.lobbyserver.manager.init.ServerInitAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 9:46
 * @Version V1.0
 **/
@Component
public class SysInitService implements ServerInitAction {

    @Autowired
    VersionManager versionManager;

    @Override
    public void initServerAction() {
        versionManager.init();
    }
}