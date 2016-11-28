package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by huge on 2016/9/17.
 * It is the Learning-Modular of CCC.
 */
public class HSKActivity extends Activity {

    //当前学习数量
    int mLearnedNumber=0;
    //学习总量
    int mTotalNumber=0;

    //字形
    private TextView mFigure;
    //拼音
    private TextView mPinyin;
    //字意
    private TextView mMeaning;
    //图片
    private ImageView mCharImg;
    //部首
    private TextView mRadical;
    //发音
    private MediaPlayer mMediaPronunciation;
    //部件
    private TextView mPart;
    //组词
    private TextView mWords;
    //例句
    private TextView mSentence;

    //确认按键
    private Button mButtonNext;
    //收藏按键
    private Button mMark;
    //发音按键
    private Button mPronunciation;

    //进度条
    private ProgressBar mProgressBar;

    //汉字库
    private CharItemLab mCharItemLab;
    //本次学习目标列表
    private CharItem[] mThisCharList;
    //当前汉字
    private CharItem mThisCharItem;

    /*static public void startHSKLeaning(Context context){
        Intent intent=new Intent(context,HSKActivity.class);
        context.startActivity(intent);
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsk);
        //初始化
        init();

        //显示首个汉字
        setCharacter();

        //设置监听器
        mButtonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //为了效果临时加的，要记得删除
                isMark=false;
                //统计当前学习量
                mLearnedNumber++;

                if(mLearnedNumber>mTotalNumber) {
                    saveDate();
                }
                else{
                    setCharacter();
                }

                if(mLearnedNumber==mTotalNumber) {
                    mButtonNext.setText("finish");
                }
            }

        });

        mPronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mMediaPronunciation.prepare();
                    mMediaPronunciation.start();
                }
                catch (Exception e)
                {
                    Toast.makeText(HSKActivity.this,"Audio file error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_mark_status();
                if(isMark)
                    mMark.setBackgroundResource(R.drawable.star_marked);
                else
                    mMark.setBackgroundResource(R.drawable.star);
            }
        });
    }

    private void init(){

        try{
            mCharItemLab=CharItemLab.getLab(HSKActivity.this);
        }
        catch (IOException exp){
            finish();
            return;
        }
        catch(JSONException exp){
            finish();
            return;
        }
        if(mCharItemLab==null){
        }

        String charId[]=new String[]{"1","2","3"};//以后要从文件中读取
        mLearnedNumber=getIntent().getIntExtra("learned_number",0);

        mTotalNumber=mThisCharList.length-1;
        try {
            mThisCharList = mCharItemLab.getCharItems(charId);
        }
        catch (Exception e)
        {
            finish();
            return;
        }



        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mProgressBar.setMax(mTotalNumber+1);

        mFigure=(TextView) findViewById(R.id.figure);
        mPinyin=(TextView)findViewById(R.id.pinyin);
        mCharImg=(ImageView)findViewById(R.id.char_image);
        mRadical=(TextView)findViewById(R.id.radical);
        mMeaning=(TextView)findViewById(R.id.meaning);
        mPart=(TextView)findViewById(R.id.part);
        mWords=(TextView)findViewById(R.id.words);
        mSentence=(TextView)findViewById(R.id.char_sentence);

        mButtonNext=(Button)findViewById(R.id.button_next);
        mMark=(Button)findViewById(R.id.mark);
        mPronunciation=(Button)findViewById(R.id.media_pronunciation);
    }

    private void setCharacter() {
        //设置进度条
        mProgressBar.setProgress(mLearnedNumber+1);

        //设置收藏状态
        if(isMark)
            mMark.setBackgroundResource(R.drawable.star_marked);
        else
            mMark.setBackgroundResource(R.drawable.star);

        //更新汉字
        if(mLearnedNumber>mTotalNumber)
            return;

        mThisCharItem=mThisCharList[mLearnedNumber];

        mFigure.setText(mThisCharItem.get("character"));
        mPinyin.setText(mThisCharItem.get("pinyin"));
        mMeaning.setText(mThisCharItem.get("explanation"));
        mCharImg.setImageBitmap(mThisCharItem.getImage(this));
        mRadical.setText(mThisCharItem.get());
        mMediaPronunciation=mThisCharItem.getMediaPlayer(this);
        mPart.setText(mThisCharItem.get());
        mWords.setText(mThisCharItem.get("words"));
        mSentence.setText(mThisCharItem.get("sentence"));


    }

    private void saveDate(){
        //保存数据

        Intent intent =new Intent();
        intent.putExtra("learned_number",mLearnedNumber);
        setResult(RESULT_OK,intent);
        finish();
    }


    //临时方法
    boolean isMark=false;
    private void change_mark_status(){
        isMark=!isMark;
    }

}
