package ecnu.chinesecharactercottage.modelsForeground;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ecnu.chinesecharactercottage.activitys.character.ComponentListActivity;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;

/**
 * @author 胡家斌
 * 这个类继承自TwoChoicesDialog这个双选对话框，实现两个选项的监听器方法。
 */
public class ChoseComponentDialog extends TwoChoicesDialog {

    public ChoseComponentDialog(Activity activity){
        super(activity,"chose_dialog","semantic","phonetic");
    }

    //跳转到形旁部件列表activity
    @Override
    protected void clickFirst() {
        ComponentListActivity.startActivity(getActivity(), ComponentItem.SHAPE);
    }

    //跳转到声旁部件列表activity
    @Override
    protected void clickSecond() {
        ComponentListActivity.startActivity(getActivity(),ComponentItem.VOICE);
    }
}
