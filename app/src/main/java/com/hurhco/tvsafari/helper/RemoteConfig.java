package com.hurhco.tvsafari.helper;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hurhco.tvsafari.R;

public class RemoteConfig {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private RemoteConfig() {

    }

    private static class SingletonHelper {
        private static final RemoteConfig INSTANCE = new RemoteConfig();
    }

    public static RemoteConfig getInstance() {
        return RemoteConfig.SingletonHelper.INSTANCE;
    }

    public void init() {
        remoteConfig();
    }

    private void remoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetchAndActivate();
    }

    //region Backendless
    public String backendlessAppId() {
        return mFirebaseRemoteConfig.getString("BACKENDLESS_APP_ID");
    }

    public String backendlessAppKey() {
        return mFirebaseRemoteConfig.getString("BACKENDLESS_APP_KEY");
    }

    public String mainTableName() {
        return mFirebaseRemoteConfig.getString("MAIN_TABLE_NAME");
    }

    public String subTableName() {
        return mFirebaseRemoteConfig.getString("SUB_TABLE_NAME");
    }

    public String subInfoTableName() {
        return mFirebaseRemoteConfig.getString("SUB_INFO_TABLE_NAME");
    }
    //endregion

    //region Ad Rules
    public boolean splashMobRule() {
        return mFirebaseRemoteConfig.getBoolean("CAN_SHOW_SPLASH_AD");
    }

    public boolean mainMobRule() {
        return mFirebaseRemoteConfig.getBoolean("CAN_SHOW_MAIN_AD");
    }

    public boolean subMobRule() {
        return mFirebaseRemoteConfig.getBoolean("CAN_SHOW_SUB_AD");
    }

    public boolean appOpenRule() {
        return mFirebaseRemoteConfig.getBoolean("CAN_SHOW_APP_OPEN_AD");
    }
    //endregion

    //region Ad Units
    public String appOpenUnitId() {
        return mFirebaseRemoteConfig.getString("APP_OPEN_UNIT_ID");
    }

    public String mainInterUnitId() {
        return mFirebaseRemoteConfig.getString("MAIN_INTERSTITIAL_UNIT_ID");
    }

    public String subInterUnitId() {
        return mFirebaseRemoteConfig.getString("SUB_INTERSTITIAL_UNIT_ID");
    }

    public String mainBannerUnitId() {
        return mFirebaseRemoteConfig.getString("MAIN_BANNER_UNIT_ID");
    }

    public String subBannerUnitId() {
        return mFirebaseRemoteConfig.getString("SUB_BANNER_UNIT_ID");
    }
    //endregion
}
