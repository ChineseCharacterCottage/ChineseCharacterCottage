package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Activity;

import ecnu.chinesecharactercottage.activitys.test.TestCompleteActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearMatchActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearTOFActivity;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;

/**
 * @author 胡家斌
 * 继承自TwoChoicesDialog的测试题类型选择对话框，左右选项分别进入听力选择和听力判断题两种类型
 * @see TwoChoicesDialog
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
        //这个方法跳转到听力选择题
        //根据模式跳转到不同的学习模式
        if(mModel== TestCompleteActivity.LEARNING)
            TestHearMatchActivity.startActivity(getActivity(),1,10);
        else
            TestHearMatchActivity.startActivity(getActivity(),mModel);
    }

    @Override
    protected void clickSecond() {
        //这个方法跳转到听力选择题
        //根据模式跳转到不同的学习模式
        if(mModel== TestCompleteActivity.LEARNING)
            TestHearTOFActivity.startActivity(getActivity(),1,10);
        else
            TestHearTOFActivity.startActivity(getActivity(),mModel);
    }
}
