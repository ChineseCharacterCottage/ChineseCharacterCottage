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
    //数据存取键值
    static final private String COMPONENT_FIGURE="component_figure";
    static final private String COMPONENT_MEANING="component_meaning";
    //部件数据:
    //部件字形
    private String mComponentFigure;
    //部件意思
    private String mComponentMeaning;

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

    static public BeginLearningFragment getFragment(BaseFragment.FinishRunnable finishRunnable,ComponentItem componentItem){
        Bundle bundle=new Bundle();
        bundle.putString(COMPONENT_FIGURE,componentItem.getShape());
        bundle.putString(COMPONENT_MEANING,componentItem.getExplanation());
        BeginLearningFragment fragment=new BeginLearningFragment();
        fragment.setArguments(bundle);
        fragment.setFinishRunnable(finishRunnable);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            mComponentFigure=savedInstanceState.getString(COMPONENT_FIGURE);
            mComponentMeaning=savedInstanceState.getString(COMPONENT_MEANING);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_begin_leaning,container,false);
        Injecter.autoInjectAllField(this,view);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");
        mTvComponentFigure.setTypeface(face);
        mTvComponentFigure.setText(mComponentFigure);
        mTvComponentMeaning.setText(mComponentMeaning);
        mBtBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        return view;
    }

}
