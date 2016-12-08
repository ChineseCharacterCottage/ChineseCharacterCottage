package ecnu.chinesecharactercottage;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 10040 on 2016/12/4.
 */

public class CharacterFragment extends Fragment {

    private CharItem mCharItem;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.character_fragment,container,false);
        
        mFigure=(TextView) view.findViewById(R.id.figure);
        mPinyin=(TextView)view.findViewById(R.id.pinyin);
        mCharImg=(ImageView)view.findViewById(R.id.char_image);
        mRadical=(TextView)view.findViewById(R.id.radical);
        mMeaning=(TextView)view.findViewById(R.id.meaning);
        mPart=(TextView)view.findViewById(R.id.part);
        mWords=(TextView)view.findViewById(R.id.words);
        mSentence=(TextView)view.findViewById(R.id.char_sentence);

        mMark=(Button)view.findViewById(R.id.mark);
        mPronunciation=(Button)view.findViewById(R.id.media_pronunciation);
        return view;
    }
    
    public void setCharacter(CharItem thisChar){
        mFigure.setText(thisChar.get(CharItem.CHARACTER));
        mPinyin.setText(thisChar.get(CharItem.PINYIN));
        mMeaning.setText(thisChar.get(CharItem.EXPLANATION));
        mCharImg.setImageBitmap(thisChar.getImage(getActivity()));
        //mRadical.setText(thisChar.getRadical().getRadical());
        mMediaPronunciation=thisChar.getMediaPlayer(getActivity());
        //mPart.setText(thisChar.get());
        //mWords.setText(thisChar.getWords());
        mSentence.setText(thisChar.get(CharItem.SENTENCE));
        //isMark=thisChar.get;


        try{
            mMediaPronunciation.prepare();
        }
        catch (Exception e){
            Log.d("CharFragmentMedia:",e.toString());
            e.printStackTrace();
        }

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
                    mMediaPronunciation.start();
                }
                catch (Exception e)
                {
                    Log.d("CharFragmentMedia:",e.toString());
                    e.printStackTrace();
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

    //临时方法
    boolean isMark;
    private void change_mark_status(){
        isMark=!isMark;
    }
}
