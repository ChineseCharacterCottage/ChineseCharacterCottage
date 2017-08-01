package ecnu.chinesecharactercottage.netserver.event;

import com.tencent.imsdk.TIMConversation;

import java.util.List;

/**
 * Created by Shensheng on 2017/7/21.
 */

public class OnRefreshConversationEvent extends IMEvent{
    public final List<TIMConversation> conversations;
    public OnRefreshConversationEvent(List<TIMConversation> conversations){
        super();
        this.conversations = conversations;
    }

    @Override
    public String message() {
        return "刷新"+conversations.size()+"项回话";
    }
}
