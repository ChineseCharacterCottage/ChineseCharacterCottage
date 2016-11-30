package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 10040 on 2016/11/29.
 */

public class ShowCharacterLayout extends LinearLayout {
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


    //收藏按键
    private Button mMark;
    //发音按键
    private Button mPronunciation;


    public ShowCharacterLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.show_character,this);
        init();
    }

    public ShowCharacterLayout(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.show_character,this);
        init();
    }
    
    public void setCharacter(CharItem thisChar){
        mFigure.setText(thisChar.get(CharItem.CHARACTER));
        mPinyin.setText(thisChar.get(CharItem.PINYIN));
        mMeaning.setText(thisChar.get(CharItem.EXPLANATION));
        //mCharImg.setImageBitmap(thisChar.getImage(this));
        //mRadical.setText(thisChar.get());
        //mMediaPronunciation=thisChar.getMediaPlayer(this);
        //mPart.setText(thisChar.get());
        //mWords.setText(thisChar.get(CharItem.WORDS));
        mSentence.setText(thisChar.get(CharItem.SENTENCE));
        //isMark=thisChar.get;

        //设置收藏状态
        isMark=false;
        if(isMark)
            mMark.setBackgroundResource(R.drawable.star_marked);
        else
            mMark.setBackgroundResource(R.drawable.star);

        //发音按键
        mPronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mMediaPronunciation.prepare();
                    mMediaPronunciation.start();
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),"Audio file error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //收藏按键
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
        mFigure=(TextView) findViewById(R.id.figure);
        mPinyin=(TextView)findViewById(R.id.pinyin);
        mCharImg=(ImageView)findViewById(R.id.char_image);
        mRadical=(TextView)findViewById(R.id.radical);
        mMeaning=(TextView)findViewById(R.id.meaning);
        mPart=(TextView)findViewById(R.id.part);
        mWords=(TextView)findViewById(R.id.words);
        mSentence=(TextView)findViewById(R.id.char_sentence);

        mMark=(Button)findViewById(R.id.mark);
        mPronunciation=(Button)findViewById(R.id.media_pronunciation);
    }

    //临时方法
    boolean isMark;
    private void change_mark_status(){
        isMark=!isMark;
    }
}
