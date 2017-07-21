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

/**
 * Created by 10040 on 2016/12/11.
 */

public class ChoseComponentDialog extends TwoChoicesDialog {

    public ChoseComponentDialog(Activity activity){
        super(activity,"chose_dialog","semantic","phonetic");
    }

    @Override
    protected void clickFirst() {
        ComponentListActivity.startActivity(getActivity(),0);
    }

    @Override
    protected void clickSecond() {
        ComponentListActivity.startActivity(getActivity(),1);
    }
}
