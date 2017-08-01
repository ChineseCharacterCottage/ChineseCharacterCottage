package ecnu.chinesecharactercottage.netserver.event;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class OnDisconnectedEvent extends ConnectEvent {

    //连接失败时发送
    public final int errorCode;
    public final String desc;
    public OnDisconnectedEvent(int code,String desc){
        super();
        this.errorCode = code;
        this.desc = desc;
    }
    @Override
    public String message() {
        return "错误"+errorCode+": "+desc;
    }
}
