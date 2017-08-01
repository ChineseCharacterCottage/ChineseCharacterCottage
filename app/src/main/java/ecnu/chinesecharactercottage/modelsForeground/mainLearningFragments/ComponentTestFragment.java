package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.activitys.character.ExampleActivity;
import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.TestHearChoiceItem;
import ecnu.chinesecharactercottage.modelsBackground.TestItem;
import ecnu.chinesecharactercottage.modelsForeground.ComponentDialog;
import ecnu.chinesecharactercottage.modelsForeground.ImageGetter;
import ecnu.chinesecharactercottage.modelsForeground.Marker;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2017/7/26.
 */

public class ComponentTestFragment extends BaseFragment {
    //数据存取键值
    static final private String ID ="id";
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
    //收藏按键
    @InjectView(id=R.id.mark)
    private Button mMark;

    static public ComponentTestFragment getFragment(BaseFragment.FinishRunnable finishRunnable,String id){
        Bundle bundle=new Bundle();
        bundle.putString(ID,id);
        ComponentTestFragment fragment=new ComponentTestFragment();
        fragment.setArguments(bundle);
        fragment.setFinishRunnable(finishRunnable);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_chars_test,container,false);
        Injecter.autoInjectAllField(this,view);
        init();
        //设置按钮的监听器
        initButtons();
        return view;
    }

    private void init(){
        mTvFigure.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf"));
        mBtSubmit.setEnabled(false);
        mLayoutErrorMsg.setVisibility(View.GONE);

        //获取数据
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(getActivity());
                //这里需要一个根据id获取部件选择题的接口,获取数据后直接返回
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(o!=null){
                    //这里设置题目
                    mTvFigure.setText();
                    mExplanation1.setText();
                    mExplanation2.setText();
                    mExplanation3.setText();
                    mExplanation4.setText();
                    mBtSubmit.setEnabled(true);
                    new Marker(getActivity()).setMark(mMark,(TestItem)o);//类型转换要在前面做好
                }else
                    finish();
            }
        };
        task.execute();
    }

    private void initButtons() {
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correctAnswer=mNowTest.getCorrectAnswer();//获取正确答案
                String chosenAnswer="";
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
                    finish();

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

        mBtNext.setVisibility(View.GONE);

        mBtShowComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要在这里获取对应componentItem
                AsyncTask task=new AsyncTask<Object,Object,ComponentItem>() {
                    @Override
                    protected ComponentItem doInBackground(Object[] params) {
                        DataManager myDM=DataManager.getInstance((Context) params[0]);
                        return myDM.getComponentById((String)params[1]);
                    }

                    @Override
                    protected void onPostExecute(ComponentItem componentItem) {
                        ComponentDialog.startDialog(getActivity(),componentItem,componentItem.getModel());
                    }
                };
                task.execute(getActivity(),mNowTest.getRelationCharacterId());
            }
        });
        mBtShowComponent.setVisibility(View.GONE);
    }
}
