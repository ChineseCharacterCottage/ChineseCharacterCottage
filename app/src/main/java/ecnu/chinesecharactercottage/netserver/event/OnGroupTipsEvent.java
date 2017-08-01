package ecnu.chinesecharactercottage.netserver.event;

import com.tencent.imsdk.TIMGroupTipsElem;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class OnGroupTipsEvent extends IMEvent {

    public final TIMGroupTipsElem elem;
    public OnGroupTipsEvent(TIMGroupTipsElem elem){
        super();
        this.elem = elem;
    }
    @Override
    public String message() {
        return "群信息";
    }
}
