package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 10040 on 2017/7/26.
 */

public class ComponentTestFragment extends BaseFragment {
    static public ComponentTestFragment getFragment(BaseFragment.FinishRunnable finishRunnable){
        return new ComponentTestFragment(finishRunnable);
    }

    public ComponentTestFragment(BaseFragment.FinishRunnable finishRunnable){
        super(finishRunnable);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

    }
}
