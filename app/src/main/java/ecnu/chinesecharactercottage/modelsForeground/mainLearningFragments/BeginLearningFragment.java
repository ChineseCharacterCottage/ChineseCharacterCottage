package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2017/7/26.
 */

public class BeginLearningFragment extends BaseFragment {

    //显示控件：
    //部件字形
    @InjectView(id=R.id.component_figure)
    private TextView mTvComponentFigure;
    //部件意思
    @InjectView(id=R.id.component_meaning)
    private TextView mTvComponentMeaning;
    //开始学习按键
    @InjectView(id=R.id.bt_begin)
    private Button mBtBegin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_begin_leaning,container,false);
        Injecter.autoInjectAllField(this,view);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");
        mTvComponentFigure.setTypeface(face);
        mBtBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        return view;
    }

    public void setComponent(ComponentItem componentItem){
        mTvComponentFigure.setText(componentItem.getShape());
        mTvComponentMeaning.setText(componentItem.getExplanation());
    }

}
