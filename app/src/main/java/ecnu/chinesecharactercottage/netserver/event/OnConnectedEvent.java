package ecnu.chinesecharactercottage.netserver.event;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class OnConnectedEvent extends ConnectEvent{
    public static final String DESC = "连接成功";
    @Override
    public String message() {
        return DESC;
    }
}
