package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Activity;

import ecnu.chinesecharactercottage.activitys.test.TestCompleteActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearMatchActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearTOFActivity;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;

/**
 * Created by 10040 on 2017/5/4.
 */

public class TestChoseListenDialog extends TwoChoicesDialog {
    private int mModel;

    public TestChoseListenDialog(Activity activity,int model) {
        super(activity,"test_listen_dialog", "Match", "True or False");
        mModel=model;
    }

    public TestChoseListenDialog(Activity activity) {
        this(activity,TestCompleteActivity.LEARNING);
    }

    @Override
    protected void clickFirst() {
        if(mModel== TestCompleteActivity.LEARNING)
            TestHearMatchActivity.startActivity(getActivity(),1,10);
        else
            TestHearMatchActivity.startActivity(getActivity(),mModel);
    }

    @Override
    protected void clickSecond() {
        if(mModel== TestCompleteActivity.LEARNING)
            TestHearTOFActivity.startActivity(getActivity(),1,10);
        else
            TestHearTOFActivity.startActivity(getActivity(),mModel);
    }
}
