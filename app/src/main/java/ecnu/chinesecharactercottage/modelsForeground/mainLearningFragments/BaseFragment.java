package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;

import android.app.Fragment;

/**
 * Created by 10040 on 2017/7/26.
 */

public class BaseFragment extends Fragment {
    public interface FinishRunnable{
        void onFinish();
    }

    //模块结束动作
    FinishRunnable mFinishRunnable;

    public void setFinishRunnable(FinishRunnable finishRunnable){
        mFinishRunnable=finishRunnable;
    }

    protected void finish(){
        if(mFinishRunnable!=null)
            mFinishRunnable.onFinish();
    }
}
