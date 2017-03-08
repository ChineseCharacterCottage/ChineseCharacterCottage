package ecnu.chinesecharactercottage.ModelsForeground.TestFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.TestHearTOFItem;
import ecnu.chinesecharactercottage.ModelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/3/7.
 */

public class TestHearTOFFragment extends Fragment {
    //发音按键
    private Button mBtPronunciation;
    //读音播放器
    private MediaPlayer mMPPronunciation;
    //图片
    private ImageView mPicture;
    //选择的答案
    private RadioGroup mChosenAnswer;
    //确定按键
    private Button mBtSubmit;
    //下一个按键
    private  Button mBtNext;
    //下一个(函数方法)
    private NextRunnable mNext;
    //错误信息
    private  LinearLayout mLayoutErrorMsg;
    //错误内容
    private   TextView mTvErrorMsg;
    //查看字按键
    private   Button mBtShowChar;
    //当前题目
    private TestHearTOFItem mNowTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test_hear_tof,container,false);
        init(view);
        //设置按钮的监听器
        initButtons();
        return view;
    }

    private void init(View view){
        mBtPronunciation=(Button) view.findViewById(R.id.pronounce);
        mBtPronunciation.setEnabled(false);
        mPicture=(ImageView) view.findViewById(R.id.iv_picture);
        mChosenAnswer=(RadioGroup) view.findViewById(R.id.answer_chose);
        mBtSubmit =(Button) view.findViewById(R.id.bt_submit);
        mBtSubmit.setEnabled(false);
        mBtNext=(Button)view.findViewById(R.id.bt_next);
        mLayoutErrorMsg=(LinearLayout)view.findViewById(R.id.layout_error_msg);
        mLayoutErrorMsg.setVisibility(View.GONE);
        mTvErrorMsg=(TextView)view.findViewById(R.id.tv_error_msg);
        mBtShowChar=(Button)view.findViewById(R.id.bt_show_character);
    }

    private void initButtons() {
        mBtPronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMPPronunciation.start();
            }
        });

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

                if(chosenAnswer==correctAnswer){
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
                    mNext.next();

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
        mBtSubmit.setEnabled(true);
        mNowTest=testHearTOFItem;

        //设置播放读音按键
        Context c=getActivity();
        mMPPronunciation=new MediaPlayer();
        try {
            AssetFileDescriptor fd=c.getAssets().openFd(mNowTest.getPronunciation());
            if(Build.VERSION.SDK_INT<24) {
                mMPPronunciation.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            }else {
                mMPPronunciation.setDataSource(c.getAssets().openFd(mNowTest.getPronunciation()+".mp3"));
            }
            mMPPronunciation.prepare();
            mBtPronunciation.setEnabled(true);
        }catch (IOException e){
            Log.d("CharItem","Media file not found :"+e.toString());
        }

        //设置题目图片
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
