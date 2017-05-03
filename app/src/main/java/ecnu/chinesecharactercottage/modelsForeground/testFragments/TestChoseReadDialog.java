package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Activity;

import ecnu.chinesecharactercottage.activitys.test.TestCompleteActivity;
import ecnu.chinesecharactercottage.activitys.test.TestTOFActivity;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;

/**
 * Created by 10040 on 2017/5/4.
 */

public class TestChoseReadDialog extends TwoChoicesDialog {

    public TestChoseReadDialog(Activity activity) {
        super(activity,"test_read_dialog", "Complete", "True or False");
    }

    @Override
    protected void clickFirst() {
        TestCompleteActivity.startActivity(getActivity(),1,10);
    }

    @Override
    protected void clickSecond() {
        TestTOFActivity.startActivity(getActivity(),1,10);
    }
}
