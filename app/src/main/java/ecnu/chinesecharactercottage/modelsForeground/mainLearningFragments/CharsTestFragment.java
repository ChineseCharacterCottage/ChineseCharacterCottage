package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.TestHearChoiceItem;
import ecnu.chinesecharactercottage.modelsForeground.ImageGetter;
import ecnu.chinesecharactercottage.modelsForeground.MPGetter;
import ecnu.chinesecharactercottage.modelsForeground.Marker;
import ecnu.chinesecharactercottage.modelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2017/7/26.
 */

public class CharsTestFragment extends BaseFragment {
    //数据存取键值
    static final private String CHARACTERS="characters";
    //视图控件：
    //字形
    @InjectView(id= R.id.figure)
    private TextView mTvFigure;
    //图片1
    @InjectView(id=R.id.iv_picture_1)
    private ImageView mPicture1;
    //图片2
    @InjectView(id=R.id.iv_picture_2)
    private ImageView mPicture2;
    //图片3
    @InjectView(id=R.id.iv_picture_3)
    private ImageView mPicture3;
    //图片4
    @InjectView(id=R.id.iv_picture_4)
    private ImageView mPicture4;
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
    //查看字按键
    @InjectView(id=R.id.bt_show_character)
    private   Button mBtShowChar;
    //收藏按键
    @InjectView(id=R.id.mark)
    private Button mMark;

    //数据内容：
    //当前题目
    private TestHearChoiceItem mNowTest;
    //当前题目索引
    int mNowIndex;
    //测试例字题目列表
    TestHearChoiceItem[] mTestCharItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_chars_test,container,false);
        Injecter.autoInjectAllField(this,view);

        mTvFigure.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf"));
        //设置按钮的监听器
        initButtons();
        return view;
    }

    public void setTests(final String[] characters){
        mBtSubmit.setEnabled(false);
        mLayoutErrorMsg.setVisibility(View.GONE);
        mNowIndex=0;

        //获取数据
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(getActivity());
                //这里需要一个根据字形获取选择题的接口
                mTestCharItems=new TestHearChoiceItem[characters.length];
                for(int i=0;i<characters.length;i++){
                    mTestCharItems[i]=dataManager.getTestByCharShape(characters[i]);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(mTestCharItems.length>0) {
                    next();
                }else
                    finish();
            }
        };
        task.execute();
    }

    private void next(){
        if (mNowIndex < mTestCharItems.length) {
            if (mTestCharItems[mNowIndex] != null) {
                setTest(mTestCharItems[mNowIndex]);
                mNowIndex++;
            } else {
                mNowIndex++;
                next();
            }
        } else
            finish();
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
                    //隐藏查看字按键
                    mBtShowChar.setVisibility(View.GONE);
                    //调用回答正确函数
                    next();
                    //清空选项
                    mChosenAnswer.clearCheck();

                }else{
                    //回答错误时
                    //隐藏提交按钮
                    mBtSubmit.setVisibility(View.GONE);
                    //显示错误信息
                    mLayoutErrorMsg.setVisibility(View.VISIBLE);
                    //显示下一个按键
                    mBtNext.setVisibility(View.VISIBLE);
                    //显示查看字按键
                    mBtShowChar.setVisibility(View.VISIBLE);
                    //设置错误信息
                    mTvErrorMsg.setText(correctAnswer.toUpperCase());
                }
            }
        });

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示提交按钮
                mBtSubmit.setVisibility(View.VISIBLE);
                //隐藏错误信息
                mLayoutErrorMsg.setVisibility(View.GONE);
                //隐藏下一个按键
                mBtNext.setVisibility(View.GONE);
                //隐藏查看字按键
                mBtShowChar.setVisibility(View.GONE);
                //调用回答正确函数
                next();
                //清空选项
                mChosenAnswer.clearCheck();
            }
        });
        mBtNext.setVisibility(View.GONE);

        mBtShowChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要在这里获取对应charItem
                AsyncTask task=new AsyncTask<Object,Object,CharItem>() {
                    @Override
                    protected CharItem doInBackground(Object[] params) {
                        DataManager myDM=DataManager.getInstance((Context) params[0]);
                        CharItem charItem=myDM.getCharItemById(Integer.valueOf((String)params[1]));
                        return charItem;
                    }

                    @Override
                    protected void onPostExecute(CharItem charItem) {
                        ExampleActivity.startActivity(getActivity(),charItem);
                    }
                };
                task.execute(getActivity(),mNowTest.getRelationCharacterId());
            }
        });
        mBtShowChar.setVisibility(View.GONE);
    }

    private void setTest(TestHearChoiceItem testHearChoiceItem){
        mNowTest=mTestCharItems[mNowIndex];
        //设置字形
        mTvFigure.setText(mNowTest.getRelationCharacterShape());
        mPicture1.setImageResource(R.drawable.imagenotfound);
        mPicture2.setImageResource(R.drawable.imagenotfound);
        mPicture3.setImageResource(R.drawable.imagenotfound);
        mPicture4.setImageResource(R.drawable.imagenotfound);

        if(testHearChoiceItem==null) {
            next();
            return;
        }
        mBtSubmit.setEnabled(true);
        mNowTest=testHearChoiceItem;


        //设置题目图片
        Context c=getActivity();
        new ImageGetter(c,mNowTest.getPictureA(),mPicture1).setImage();
        new ImageGetter(c,mNowTest.getPictureB(),mPicture2).setImage();
        new ImageGetter(c,mNowTest.getPictureC(),mPicture3).setImage();
        new ImageGetter(c,mNowTest.getPictureD(),mPicture4).setImage();

        new Marker(getActivity()).setMark(mMark,mNowTest);
    }
}
