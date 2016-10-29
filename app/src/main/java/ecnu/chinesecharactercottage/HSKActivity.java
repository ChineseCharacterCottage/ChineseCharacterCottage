package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.sql.BatchUpdateException;

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
    private ImageView mFigure;
    //拼音
    private TextView mPinyin;
    //字意
    private TextView mMeaning;
    //部首
    private TextView mRadical;
    //发音
    private MediaPlayer mMediaPronunciation;
    //部件
    private TextView mPart;
    //笔画数目
    private TextView mStrokeCount;
    //组词
    private TextView mWords;

    //确认按键
    private Button mButtonNext;
    //收藏按键
    private Button mMark;
    //发音按键
    private Button mPronunciation;
    //链接到学习板块
    private Button mLinkToLearning;
    //链接到部首板块
    private Button mLinkToRadical;
    //链接到其他媒体
    private Button mLinkToMedia;
    //链接到其他字典
    private Button mLinkToDictionay;

    //进度条
    private ProgressBar mProgressBar;

    //当前汉字条目
    private CharItem mThisCharItem;
    //汉字库
    private CharItemLab mCharItemLab;
    //本次学习目标
    private String[] mThisCharList;
    //当前学习汉字标识
    private String mThisCharTag;

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

        mThisCharList=new String[]{"龙","花","草"};//以后要从文件中读取
        mLearnedNumber=getIntent().getIntExtra("learned_number",0);

        mTotalNumber=mThisCharList.length-1;
        mThisCharTag=mThisCharList[mLearnedNumber];
        mThisCharItem=mCharItemLab.getCharItem(mThisCharTag);


        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mProgressBar.setMax(mTotalNumber+1);

        mFigure=(ImageView) findViewById(R.id.figure);
        mPinyin=(TextView)findViewById(R.id.pinyin);
        mRadical=(TextView)findViewById(R.id.radical);
        mMeaning=(TextView)findViewById(R.id.meaning);
        mPart=(TextView)findViewById(R.id.part);
        mStrokeCount=(TextView)findViewById(R.id.stroke_count);
        mWords=(TextView)findViewById(R.id.words);

        mButtonNext=(Button)findViewById(R.id.button_next);
        mMark=(Button)findViewById(R.id.mark);
        mPronunciation=(Button)findViewById(R.id.media_pronunciation);

        mLinkToLearning=(Button)findViewById(R.id.button_link_learning);
        mLinkToRadical=(Button)findViewById(R.id.button_link_radical);
        mLinkToMedia=(Button)findViewById(R.id.button_link_media);
        mLinkToDictionay=(Button)findViewById(R.id.button_link_dictionary);
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
        mThisCharTag=mThisCharList[mLearnedNumber];
        mThisCharItem=mCharItemLab.getCharItem(mThisCharTag);

        if(mThisCharItem==null)
            return;

        mPinyin.setText(mThisCharItem.getPinyin());
        mFigure.setImageBitmap(mThisCharItem.getImage(this));
        mRadical.setText(mThisCharItem.getRadical());
        mMeaning.setText(mThisCharItem.getNotes());
        mMediaPronunciation=mThisCharItem.getMediaPlayer(this);

        //mPart

        //mStrokeCount

        //mWords

        mThisCharItem.newAppear();;
    }

    private void saveDate(){
        //保存数据
        try {
            mCharItemLab.saveCharItems();
        }
        catch (IOException e){
        }
        catch (JSONException e){
        }
        finally {
            finish();
        }

        Intent intent =new Intent();
        intent.putExtra("learned_number",mLearnedNumber);
        setResult(RESULT_OK,intent);
        finish();
    }
    
    private String getCharTag(int number){
        return mThisCharList[number];
    }


    //临时方法
    boolean isMark=false;
    private void change_mark_status(){
        isMark=!isMark;
    }

}
