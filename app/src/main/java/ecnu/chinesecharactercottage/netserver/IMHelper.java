package ecnu.chinesecharactercottage.netserver;

import android.content.Context;

import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUploadProgressListener;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ecnu.chinesecharactercottage.netserver.event.ForceOfflineEvent;
import ecnu.chinesecharactercottage.netserver.event.OnConnectedEvent;
import ecnu.chinesecharactercottage.netserver.event.OnDisconnectedEvent;
import ecnu.chinesecharactercottage.netserver.event.OnGroupTipsEvent;
import ecnu.chinesecharactercottage.netserver.event.OnRefreshEvent;
import ecnu.chinesecharactercottage.netserver.event.OnWifiNeedAuthEvent;
import ecnu.chinesecharactercottage.netserver.event.UserSigExpiredEvent;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class IMHelper implements TIMUserStatusListener,TIMConnListener,
        TIMGroupEventListener,TIMRefreshListener,TIMUploadProgressListener,TIMMessageListener{

    private static IMHelper sLoginHelper;
    public static IMHelper getInstance(Context context){
        if(sLoginHelper == null){
            sLoginHelper = new IMHelper(context);
        }
        return sLoginHelper;
    }
    private Context mContext;
    private TIMManager mTIMManager;
    private static int APP_ID = 1400036499;
    private IMHelper(Context context){
        mContext = context.getApplicationContext();
        mTIMManager = TIMManager.getInstance();
        initTIM();
    }

    private void initTIM(){
        mTIMManager.init(mContext,new TIMSdkConfig(APP_ID)
        .enableCrashReport(false)
        .enableLogPrint(true)
        .setAccoutType("14266"));
        initUserConfig();
        mTIMManager.addMessageListener(this);
    }

    private void initUserConfig(){
        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(this)
                .setConnectionListener(this)
                .setGroupEventListener(this)
                .setRefreshListener(this)
                .setUploadProgressListener(this);
        mTIMManager.setUserConfig(userConfig);
    }

    public void login(String username,String password){

    }

    @Override
    public void onForceOffline() {
        EventBus.getDefault().post(new ForceOfflineEvent());
    }


    @Override
    public void onUserSigExpired() {
        EventBus.getDefault().post(new UserSigExpiredEvent());
    }

    @Override
    public void onConnected() {
        EventBus.getDefault().post(new OnConnectedEvent());
    }

    @Override
    public void onDisconnected(int i, String s) {
        EventBus.getDefault().post(new OnDisconnectedEvent(i,s));
    }

    @Override
    public void onWifiNeedAuth(String s) {
        EventBus.getDefault().post(new OnWifiNeedAuthEvent(s));
    }

    @Override
    public void onGroupTipsEvent(TIMGroupTipsElem timGroupTipsElem) {
        EventBus.getDefault().post(new OnGroupTipsEvent(timGroupTipsElem));
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(new OnRefreshEvent());
    }

    @Override
    public void onRefreshConversation(List<TIMConversation> list) {

    }

    @Override
    public void onMessagesUpdate(TIMMessage timMessage, int i, int i1, int i2) {

    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {

        return true;
    }
}
