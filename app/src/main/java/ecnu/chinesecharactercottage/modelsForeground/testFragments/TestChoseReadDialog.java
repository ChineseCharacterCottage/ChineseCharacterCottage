package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Activity;

import ecnu.chinesecharactercottage.activitys.test.TestCompleteActivity;
import ecnu.chinesecharactercottage.activitys.test.TestTOFActivity;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;

/**
 * @author 胡家斌
 * 继承自TwoChoicesDialog的测试题类型选择对话框，左右选项分别进入阅读匹配题和阅读判断题两种类型
 * @see TwoChoicesDialog
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
        //这个方法跳转到阅读匹配题
        //根据模式跳转到不同的学习模式
        if(mModel==TestCompleteActivity.LEARNING)
            TestCompleteActivity.startActivity(getActivity(),1,10);
        else
            TestCompleteActivity.startActivity(getActivity(),mModel);

    }

    @Override
    protected void clickSecond() {
        //这个方法跳转到阅读选择题
        //根据模式跳转到不同的学习模式
        if(mModel==TestCompleteActivity.LEARNING)
            TestTOFActivity.startActivity(getActivity(),1,10);
        else
            TestTOFActivity.startActivity(getActivity(),mModel);

    }
}
