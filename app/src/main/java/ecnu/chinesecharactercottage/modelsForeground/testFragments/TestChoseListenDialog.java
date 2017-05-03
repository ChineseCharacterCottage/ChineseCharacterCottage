package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Activity;

import ecnu.chinesecharactercottage.activitys.test.TestHearMatchActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearTOFActivity;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;

/**
 * Created by 10040 on 2017/5/4.
 */

public class TestChoseListenDialog extends TwoChoicesDialog {

    public TestChoseListenDialog(Activity activity) {
        super(activity,"test_listen_dialog", "Match", "True or False");
    }

    @Override
    protected void clickFirst() {
        TestHearMatchActivity.startActivity(getActivity(),1,10);
    }

    @Override
    protected void clickSecond() {
        TestHearTOFActivity.startActivity(getActivity(),1,10);
    }
}
