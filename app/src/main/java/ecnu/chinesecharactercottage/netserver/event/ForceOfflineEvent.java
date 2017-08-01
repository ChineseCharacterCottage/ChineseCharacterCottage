package ecnu.chinesecharactercottage.netserver.event;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class ForceOfflineEvent extends IMEvent {
    public static final String DESC = "由于账号在其他地方登陆，被踢下线";
    @Override
    public String message(){
        return DESC;
    }
}
