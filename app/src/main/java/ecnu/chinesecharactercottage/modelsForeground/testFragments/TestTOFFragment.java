package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Fragment;
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
import android.widget.TextView;

import ecnu.chinesecharactercottage.activitys.character.ExampleActivity;
import ecnu.chinesecharactercottage.modelsBackground.*;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.ImageGetter;
import ecnu.chinesecharactercottage.modelsForeground.Marker;
import ecnu.chinesecharactercottage.modelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;


/**
 * @author 胡家斌
 * 阅读判断题部分
 */

public class TestTOFFragment extends Fragment {
    //字形
    @InjectView(id=R.id.tv_character)
    TextView mCharacter;
    //图片
    @InjectView(id=R.id.iv_picture)
    ImageView mPicture;
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
    Button mBtNext;
    //下一个(函数方法)
    NextRunnable mNext;
    //错误信息
    @InjectView(id=R.id.layout_error_msg)
    LinearLayout mLayoutErrorMsg;
    //错误内容
    @InjectView(id=R.id.tv_error_msg)
    TextView mTvErrorMsg;
    //查看字按键
    @InjectView(id=R.id.bt_show_character)
    Button mBtShowChar;
    //当前题目
    TestTOFItem mNowTest;

    //收藏按键
    @InjectView(id=R.id.mark)
    private Button mMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test_tof,container,false);
        Injecter.autoInjectAllField(this,view);
        init();

        //设置按钮的监听器
        initButtons();
        return view;
    }

    private void init(){
        mBtTrue.setEnabled(false);
        mBtFalse.setEnabled(false);
        mLayoutErrorMsg.setVisibility(View.GONE);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");
        mCharacter.setTypeface(face);
    }

    private void initButtons() {
        View.OnClickListener listener= new View.OnClickListener() {
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
                mBtTrue.setEnabled(false);
                mBtFalse.setEnabled(false);

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
                    mTvErrorMsg.setText(correctAnswer==1?"True":"False");
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

    public void setTest(TestTOFItem testTOFItem){
        if(testTOFItem==null){
            mNext.next();
            return;
        }
        mBtTrue.setEnabled(true);
        mBtFalse.setEnabled(true);
        mNowTest=testTOFItem;
        mCharacter.setText(mNowTest.getCharacterShape());


        //设置题目图片
        Context c=getActivity();
        new ImageGetter(c,mNowTest.getPicture(),mPicture).setImage();

        new Marker(getActivity()).setMark(mMark,mNowTest);
    }
}
