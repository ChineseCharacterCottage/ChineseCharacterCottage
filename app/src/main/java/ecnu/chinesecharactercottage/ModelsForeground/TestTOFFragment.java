package ecnu.chinesecharactercottage.ModelsForeground;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import ecnu.chinesecharactercottage.Activitys.ExampleActivity;
import ecnu.chinesecharactercottage.ModelsBackground.*;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/3/4.
 */

public class TestTOFFragment extends Fragment {
    //字形
    TextView mCharacter;
    //图片
    ImageView mPicture;
    //选择的答案
    RadioGroup mChosenAnswer;
    //确定按键
    Button mBtSubmit;
    //下一个按键
    Button mBtNext;
    //下一个(函数方法)
    NextRunnable mNext;
    //错误信息
    LinearLayout mLayoutErrorMsg;
    //错误内容
    TextView mTvErrorMsg;
    //查看字按键
    Button mBtShowChar;
    //当前题目
    TestTOFItem mNowTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test_tof,container,false);

        mCharacter=(TextView)view.findViewById(R.id.tv_character);
        mPicture=(ImageView) view.findViewById(R.id.iv_picture);
        mChosenAnswer=(RadioGroup) view.findViewById(R.id.answer_chose);
        mBtSubmit =(Button) view.findViewById(R.id.bt_submint);
        mBtNext=(Button)view.findViewById(R.id.bt_next);
        mLayoutErrorMsg=(LinearLayout)view.findViewById(R.id.layout_error_msg);
        mTvErrorMsg=(TextView)view.findViewById(R.id.tv_error_msg);
        mBtShowChar=(Button)view.findViewById(R.id.bt_show_character);

        initSubmitButton();
        return view;
    }

    private void initSubmitButton() {
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int correctAnswer=mNowTest.getCorrectAnswer();//获取正确答案
                int chosenAnswer=-1;
                switch (mChosenAnswer.getCheckedRadioButtonId()){
                    case R.id.radio_t:
                        chosenAnswer=1;
                        break;
                    case R.id.radio_f:
                        chosenAnswer=0;
                        break;
                }

                if(chosenAnswer==correctAnswer){
                    //正确回答时
                    //显示提交按钮
                    mBtSubmit.setVisibility(View.VISIBLE);
                    //隐藏错误信息
                    mLayoutErrorMsg.setVisibility(View.GONE);
                    //隐藏下一个按键
                    mBtNext.setVisibility(View.GONE);
                    //调用回答正确函数
                    mNext.next();

                }else{
                    //回答错误时
                    //隐藏提交按钮
                    mBtSubmit.setVisibility(View.GONE);
                    //显示错误信息
                    mLayoutErrorMsg.setVisibility(View.VISIBLE);
                    //显示下一个按键
                    mBtNext.setVisibility(View.VISIBLE);
                    //设置错误信息
                    mTvErrorMsg.setText(correctAnswer==1?"true":"false");
                }
            }
        });

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNext.next();
            }
        });

        mBtShowChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要在这里获取对应charItem
                DataManager myDM=DataManager.getInstance(getActivity());
                ExampleActivity.startActivity(getActivity(),myDM.getCharItemById(mNowTest.getCharId()));
            }
        });
    }

    public void setNextListener(NextRunnable next){
        mNext=next;
    }

    public void setTest(TestTOFItem testTOFItem){
        mTestTOFItem=testTOFItem;
        //mCharacter.setText();
        //mPicture.setImageBitmap();
    }
}
