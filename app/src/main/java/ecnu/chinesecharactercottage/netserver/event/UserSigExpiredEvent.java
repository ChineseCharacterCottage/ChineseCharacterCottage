package ecnu.chinesecharactercottage.netserver.event;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class UserSigExpiredEvent extends IMEvent{

    public static final String DESC = "用户票据过期，被踢下线";

    @Override
    public String message() {
        return DESC;
    }

}
