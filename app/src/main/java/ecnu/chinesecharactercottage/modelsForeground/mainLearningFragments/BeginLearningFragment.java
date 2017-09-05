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
 * @author 胡家斌
 * 这个fragment是主学习板块的第一部分（部件意思学习）的主要逻辑部分。
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
        View view=inflater.inflate(R.layout.fragment_ml_begin_leaning,container,false);//渲染视图
        Injecter.autoInjectAllField(this,view);//绑定控件

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");//获取字体
        mTvComponentFigure.setTypeface(face);//设置部件字形的字体
        //点击开始学习就进入下一个阶段
        mBtBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        return view;
    }

    public void setComponent(ComponentItem componentItem){
        //设置字形
        mTvComponentFigure.setText(componentItem.getShape());
        //设置部件意思
        mTvComponentMeaning.setText(componentItem.getExplanation());
    }

}
