package ecnu.chinesecharactercottage.ModelsForeground;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

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
        init(view);
        //设置按钮的监听器
        initSubmitButton();
        return view;
    }

    private void init(View view){
        mCharacter=(TextView)view.findViewById(R.id.tv_character);
        mPicture=(ImageView) view.findViewById(R.id.iv_picture);
        mChosenAnswer=(RadioGroup) view.findViewById(R.id.answer_chose);
        mBtSubmit =(Button) view.findViewById(R.id.bt_submint);
        mBtSubmit.setEnabled(false);
        mBtNext=(Button)view.findViewById(R.id.bt_next);
        mLayoutErrorMsg=(LinearLayout)view.findViewById(R.id.layout_error_msg);
        mTvErrorMsg=(TextView)view.findViewById(R.id.tv_error_msg);
        mBtShowChar=(Button)view.findViewById(R.id.bt_show_character);
    }

    private void initSubmitButton() {
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int correctAnswer=mNowTest.getCorrectAnswer()?1:0;//获取正确答案
                int chosenAnswer=-1;
                switch (mChosenAnswer.getCheckedRadioButtonId()){
                    case R.id.radio_t:
                        chosenAnswer=1;
                        break;
                    case R.id.radio_f:
                        chosenAnswer=0;
                        break;
                }
                mBtSubmit.setEnabled(false);

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
        mBtNext.setVisibility(View.GONE);

        mBtShowChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要在这里获取对应charItem
                DataManager myDM=DataManager.getInstance(getActivity());
                ExampleActivity.startActivity(getActivity(),myDM.getCharItemById(Integer.valueOf(mNowTest.getRelationCharacterId())));
            }
        });
        mBtShowChar.setVisibility(View.GONE);
    }

    public void setNext(NextRunnable next){
        mNext=next;
    }

    public void setTest(TestTOFItem testTOFItem){
        if(testTOFItem==null) {
            mNext.next();
            return;
        }
        mBtSubmit.setEnabled(true);
        mNowTest=testTOFItem;
        mCharacter.setText(mNowTest.getCharacterShape());

        AssetManager manager=getActivity().getAssets();
        Bitmap image;
        try {
            InputStream stream = manager.open(mNowTest.getPicture());
            image = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            image = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.imagenotfound);
        }
        mPicture.setImageBitmap(image);
    }
}