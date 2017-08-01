package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/8/1.
 */

public class DefaultFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ml_default,container,false);
    }
}
