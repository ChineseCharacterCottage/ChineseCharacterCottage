package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.activitys.character.ExampleActivity;
import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.TestHearTOFItem;
import ecnu.chinesecharactercottage.modelsForeground.ImageGetter;
import ecnu.chinesecharactercottage.modelsForeground.MPGetter;
import ecnu.chinesecharactercottage.modelsForeground.Marker;
import ecnu.chinesecharactercottage.modelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2017/3/7.
 */

public class TestHearTOFFragment extends Fragment {
    //发音按键
    @InjectView(id=R.id.pronounce)
    private Button mBtPronunciation;
    //图片
    @InjectView(id=R.id.iv_picture)
    private ImageView mPicture;
    //选择答案布局
    @InjectView(id=R.id.layout_submit)
    LinearLayout mLayoutSubmit;
    //选择答案按键
    @InjectView(id=R.id.bt_true)
    Button mBtTrue;
    @InjectView(id=R.id.bt_false)
    Button mBtFalse;
    //下一个按键
    @InjectView(id=R.id.bt_next)
    private  Button mBtNext;
    //下一个(函数方法)
    private NextRunnable mNext;
    //错误信息
    @InjectView(id=R.id.layout_error_msg)
    private  LinearLayout mLayoutErrorMsg;
    //错误内容
    @InjectView(id=R.id.tv_error_msg)
    private   TextView mTvErrorMsg;
    //查看字按键
    @InjectView(id=R.id.bt_show_character)
    private   Button mBtShowChar;
    //当前题目
    private TestHearTOFItem mNowTest;
    //收藏按键
    @InjectView(id=R.id.mark)
    private Button mMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test_hear_tof,container,false);
        Injecter.autoInjectAllField(this,view);
        init();
        //设置按钮的监听器
        initButtons();
        return view;
    }

    private void init(){
        mBtPronunciation.setEnabled(false);
        mBtTrue.setEnabled(false);
        mBtFalse.setEnabled(false);
        mLayoutErrorMsg.setVisibility(View.GONE);
    }

    private void initButtons() {

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int correctAnswer=mNowTest.getCorrectAnswer()?1:0;//获取正确答案
                int chosenAnswer=-1;
                switch (v.getId()){
                    case R.id.bt_true:
                        chosenAnswer=1;
                        break;
                    case R.id.bt_false:
                        chosenAnswer=0;
                        break;
                }

                if(chosenAnswer==correctAnswer){
                    //正确回答时
                    //显示提交按钮
                    mLayoutSubmit.setVisibility(View.VISIBLE);
                    //隐藏错误信息
                    mLayoutErrorMsg.setVisibility(View.GONE);
                    //隐藏下一个按键
                    mBtNext.setVisibility(View.GONE);
                    //隐藏查看字按键
                    mBtShowChar.setVisibility(View.GONE);
                    //调用回答正确函数
                    mNext.next();

                }else{
                    //回答错误时
                    //隐藏提交按钮
                    mLayoutSubmit.setVisibility(View.GONE);
                    //显示错误信息
                    mLayoutErrorMsg.setVisibility(View.VISIBLE);
                    //显示下一个按键
                    mBtNext.setVisibility(View.VISIBLE);
                    //显示查看字按键
                    mBtShowChar.setVisibility(View.VISIBLE);
                    //设置错误信息
                    mTvErrorMsg.setText(correctAnswer==1?"true":"false");
                }
            }
        };
        mBtTrue.setOnClickListener(listener);
        mBtFalse.setOnClickListener(listener);

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示提交按钮
                mLayoutSubmit.setVisibility(View.VISIBLE);
                //隐藏错误信息
                mLayoutErrorMsg.setVisibility(View.GONE);
                //隐藏下一个按键
                mBtNext.setVisibility(View.GONE);
                //隐藏查看字按键
                mBtShowChar.setVisibility(View.GONE);
                //调用回答正确函数
                mNext.next();
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
                        return myDM.getCharItemById(Integer.valueOf((String)params[1]));
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

    public void setNext(NextRunnable next){
        mNext=next;
    }

    public void setTest(TestHearTOFItem testHearTOFItem){
        if(testHearTOFItem==null) {
            mNext.next();
            return;
        }
        mBtTrue.setEnabled(true);
        mBtFalse.setEnabled(true);
        mNowTest=testHearTOFItem;

        //设置播放读音按键
        Context c=getActivity();
        new MPGetter(c,mNowTest,mBtPronunciation).setMP(true);

        //设置题目图片
        new ImageGetter(c,mNowTest.getPicture(),mPicture).setImage();

        new Marker(getActivity()).setMark(mMark,mNowTest);
    }

}
