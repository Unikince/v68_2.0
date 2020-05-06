package com.dmg.lobbyserver.manager;

import com.dmg.lobbyserver.dao.VersionControlDao;
import com.dmg.lobbyserver.dao.bean.VersionControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 版本控制管理等配置信息
 *
 * @author charlesLee
 */
@Component
public class VersionManager {
    @Autowired
    private VersionControlDao versionControlDao;

    private static final VersionManager version = new VersionManager();

    private Map<String, Object> androidInfo = new HashMap<>();

    private Map<String, Object> iosInfo = new HashMap<>();

    private Map<String, Object> allInfo = new HashMap<>();

    private VersionManager() {
    }

    public static VersionManager instance() {
        return version;
    }

    public void init() {
        VersionControlBean versionControlBean = versionControlDao.selectById(1);
        version.iosInfo.put("heartBeatTime", versionControlBean.getHeartBeatTime());
        version.iosInfo.put("version", versionControlBean.getVersion());
        version.iosInfo.put("shareIos", versionControlBean.getShareIos());
        version.iosInfo.put("downLoadURL", versionControlBean.getDownLoadUrl());
        version.iosInfo.put("iosPayType", versionControlBean.getIosPayType());
        version.iosInfo.put("iosDownload", versionControlBean.getIosDownload());

        version.androidInfo.put("heartBeatTime", versionControlBean.getHeartBeatTime());
        version.androidInfo.put("version", versionControlBean.getVersion());
        version.androidInfo.put("shareAndroid", versionControlBean.getShareAndroid());
        version.androidInfo.put("downLoadURL", versionControlBean.getDownLoadUrl());
        version.androidInfo.put("androidPayType", versionControlBean.getAndroidPayType());
        version.androidInfo.put("AndroidDownload", versionControlBean.getAndroidDownload());

        version.allInfo.put("heartBeatTime", versionControlBean.getHeartBeatTime());
        version.allInfo.put("version", versionControlBean.getVersion());
        version.allInfo.put("shareIos", versionControlBean.getShareIos());
        version.allInfo.put("downLoadURL", versionControlBean.getDownLoadUrl());
        version.allInfo.put("iosPayType", versionControlBean.getIosPayType());
        version.allInfo.put("iosDownload", versionControlBean.getIosDownload());
        version.allInfo.put("shareAndroid", versionControlBean.getShareAndroid());
        version.allInfo.put("androidPayType", versionControlBean.getAndroidPayType());
        version.allInfo.put("AndroidDownload", versionControlBean.getAndroidDownload());
    }

    public Map<String, Object> getVersionInfo(int deviceType) {
        // 如果是安卓
        if (deviceType == 1) {
            return androidInfo;
            // 如果是ios
        } else if (deviceType == 2) {
            return iosInfo;
        } else {
            return allInfo;
        }
    }

	public Map<String, Object> getAndroidInfo() {
		return androidInfo;
	}

	public Map<String, Object> getIosInfo() {
		return iosInfo;
	}

	public Map<String, Object> getAllInfo() {
		return allInfo;
	}

    
}
