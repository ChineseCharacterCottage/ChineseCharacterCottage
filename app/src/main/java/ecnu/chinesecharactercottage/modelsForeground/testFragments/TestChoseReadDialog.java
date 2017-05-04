package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Activity;

import ecnu.chinesecharactercottage.activitys.test.TestCompleteActivity;
import ecnu.chinesecharactercottage.activitys.test.TestTOFActivity;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;

/**
 * Created by 10040 on 2017/5/4.
 */

public class TestChoseReadDialog extends TwoChoicesDialog {
    private int mModel;

    public TestChoseReadDialog(Activity activity, int model) {
        super(activity,"test_read_dialog", "Complete", "True or False");
        mModel=model;
    }

    public TestChoseReadDialog(Activity activity) {
        this(activity,TestCompleteActivity.LEARNING);
    }

    @Override
    protected void clickFirst() {
        if(mModel==TestCompleteActivity.LEARNING)
            TestCompleteActivity.startActivity(getActivity(),1,10);
        else
            TestCompleteActivity.startActivity(getActivity(),mModel);

    }

    @Override
    protected void clickSecond() {
        if(mModel==TestCompleteActivity.LEARNING)
            TestTOFActivity.startActivity(getActivity(),1,10);
        else
            TestTOFActivity.startActivity(getActivity(),mModel);

    }
}
