package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;

import android.app.Fragment;

/**
 * @author 胡家斌
 * 主学习板块4个模块的逻辑都是用fragment实现的，这个BaseFragment是4个阶段的基类，
 * 实现了一个finish()方法，退出模块的时候会调用一个传入的接口方法
 */

public class BaseFragment extends Fragment {
    public interface FinishRunnable{
        void onFinish();
    }

    //模块结束动作
    FinishRunnable mFinishRunnable;

    public void setFinishRunnable(FinishRunnable finishRunnable){
        mFinishRunnable=finishRunnable;//保存模块结束动作
    }

    protected void finish(){
        if(mFinishRunnable!=null)
            mFinishRunnable.onFinish();//调用结束方法
    }
}
