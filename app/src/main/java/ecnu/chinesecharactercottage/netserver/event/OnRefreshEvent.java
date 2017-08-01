package ecnu.chinesecharactercottage.netserver.event;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class OnRefreshEvent extends IMEvent {

    public static final String DESC= "会话已经刷新";
    @Override
    public String message() {
        return DESC;
    }
}
