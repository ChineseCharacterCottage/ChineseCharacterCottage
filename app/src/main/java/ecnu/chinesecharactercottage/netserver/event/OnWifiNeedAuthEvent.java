package ecnu.chinesecharactercottage.netserver.event;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class OnWifiNeedAuthEvent extends ConnectEvent {
    public final String wifiName;
    public OnWifiNeedAuthEvent(String wifi){
        super();
        wifiName = wifi;
    }
    @Override
    public String message() {
        return wifiName+"需要验证";
    }
}
