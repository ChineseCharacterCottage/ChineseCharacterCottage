package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.TestComponentItem;
import ecnu.chinesecharactercottage.modelsForeground.ComponentDialog;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 这个fragment是主学习板块的第三部分（部件意思测试）的主要逻辑部分。
 */

public class ComponentTestFragment extends BaseFragment {
    //视图控件：
    //字形
    @InjectView(id= R.id.figure)
    private TextView mTvFigure;
    //选项1
    @InjectView(id=R.id.tv_chose_a)
    private TextView mExplanation1;
    //选项2
    @InjectView(id=R.id.tv_chose_b)
    private TextView mExplanation2;
    //选项3
    @InjectView(id=R.id.tv_chose_c)
    private TextView mExplanation3;
    //选项4
    @InjectView(id=R.id.tv_chose_d)
    private TextView mExplanation4;
    //选择的答案
    @InjectView(id=R.id.answer_chose)
    private RadioGroup mChosenAnswer;
    //确定按键
    @InjectView(id=R.id.bt_submit)
    private Button mBtSubmit;
    //下一个按键
    @InjectView(id=R.id.bt_next)
    private  Button mBtNext;
    //错误信息
    @InjectView(id=R.id.layout_error_msg)
    private LinearLayout mLayoutErrorMsg;
    //错误内容
    @InjectView(id=R.id.tv_error_msg)
    private TextView mTvErrorMsg;
    //查看部件按键
    @InjectView(id=R.id.bt_show_character)
    private   Button mBtShowComponent;
    //收藏按键，收藏未实现
    //@InjectView(id=R.id.mark)
    //private Button mMark;

    //相关数据：
    //当前测试部件题目
    private TestComponentItem mNowTest;
    //测试题对应部件
    private ComponentItem mComponentItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_component_test,container,false);//渲染视图
        Injecter.autoInjectAllField(this,view);//绑定控件
        mTvFigure.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf"));//设置部件字形的字体

        initButtons();//设置按钮的监听器

        return view;
    }

    /**
     * 根据传入的部件id，获取相应部件的测试题，并更新到界面上
     * @param componentItem 要测试的部件数据
     */
    public void setTest(ComponentItem componentItem){
        mComponentItem=componentItem;//保存部件数据到成员变量
        mBtSubmit.setEnabled(false);//在数据获取完全前设置提交为不可点击
        mLayoutErrorMsg.setVisibility(View.GONE);//隐藏错误信息
        mBtNext.setVisibility(View.GONE);//隐藏下一个按键
        mBtShowComponent.setVisibility(View.GONE);//隐藏显示显示部件详情按键
        mBtSubmit.setVisibility(View.VISIBLE);//显示提交按键

        //异步获取数据
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(getActivity());
                mNowTest=dataManager.getTestComponentItemByCompId(mComponentItem.getGlobalId());//获取部件测试题
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(mNowTest!=null){
                    //设置题目
                    mTvFigure.setText(mComponentItem.getShape());//设置字形
                    //设置4个选项
                    mExplanation1.setText(mNowTest.getChoiceA());
                    mExplanation2.setText(mNowTest.getChoiceB());
                    mExplanation3.setText(mNowTest.getChoiceC());
                    mExplanation4.setText(mNowTest.getChoiceD());
                    mBtSubmit.setEnabled(true);//设置提交按键为可点击
                    //new Marker(getActivity()).setMark(mMark,mNowTest);//还未实现收藏
                }else
                    finish();//调用基类提供的finish()
            }
        };
        task.execute();
    }

    private void initButtons() {
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correctAnswer=mNowTest.getCorrectAnswer();//获取正确答案
                String chosenAnswer="";//用户选择的答案
                //判断用户选择的答案是什么
                switch (mChosenAnswer.getCheckedRadioButtonId()){
                    case R.id.radio_1:
                        chosenAnswer="a";
                        break;
                    case R.id.radio_2:
                        chosenAnswer="b";
                        break;
                    case R.id.radio_3:
                        chosenAnswer="c";
                        break;
                    case R.id.radio_4:
                        chosenAnswer="d";
                        break;
                }

                //判断用户答案是否正确
                if(chosenAnswer.equals(correctAnswer)){
                    //正确回答时
                    //显示提交按钮
                    mBtSubmit.setVisibility(View.VISIBLE);
                    //隐藏错误信息
                    mLayoutErrorMsg.setVisibility(View.GONE);
                    //隐藏下一个按键
                    mBtNext.setVisibility(View.GONE);
                    //隐藏查看部件按键
                    mBtShowComponent.setVisibility(View.GONE);
                    //回答正确
                    finish();//调用基类提供的finish()

                }else{
                    //回答错误时
                    //隐藏提交按钮
                    mBtSubmit.setVisibility(View.GONE);
                    //显示错误信息
                    mLayoutErrorMsg.setVisibility(View.VISIBLE);
                    //显示下一个按键
                    mBtNext.setVisibility(View.VISIBLE);
                    //显示查看字按键
                    mBtShowComponent.setVisibility(View.VISIBLE);
                    //设置错误信息
                    mTvErrorMsg.setText(correctAnswer.toUpperCase());
                }
            }
        });

        //点击next进入下一个阶段，所以直接结束当前阶段即可
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//调用基类提供的finish()
            }
        });

        //点击显示部件详情对话框
        mBtShowComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动一个对话框
                ComponentDialog.startDialog(getActivity(),mComponentItem);
            }
        });
    }
}
