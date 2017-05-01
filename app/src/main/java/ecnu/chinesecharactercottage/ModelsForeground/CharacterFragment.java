package ecnu.chinesecharactercottage.ModelsForeground;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ecnu.chinesecharactercottage.ModelsBackground.*;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/12/4.
 */

public class CharacterFragment extends Fragment {

    //当前字
    CharItem mNowChar;

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
    //private TextView mComponent;
    //组词
    private LinearLayout mWords;
    //例句
    private TextView mSentence;
    //例句喇叭
    private Button mSentenceHorn;
    //例句发音
    private MediaPlayer mSentenceMP;
    //收藏情况
    private Boolean mIsMark;

    //数据管理器
    private DataManager mDataManager;

    //收藏按键
    private Button mMark;
    //发音按键
    //private Button mPronunciation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_character,container,false);

        mDataManager=DataManager.getInstance(getActivity());

        mFigure=(TextView) view.findViewById(R.id.figure);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");
        mFigure.setTypeface(face);
        mPinyin=(TextView)view.findViewById(R.id.pinyin);
        mCharImg=(ImageView)view.findViewById(R.id.char_image);
        mRadical=(TextView)view.findViewById(R.id.radical);
        mMeaning=(TextView)view.findViewById(R.id.meaning);
        //mComponent=(TextView)view.findViewById(R.id.component);
        mSentence=(TextView)view.findViewById(R.id.char_sentence);
        mWords=(LinearLayout)view.findViewById(R.id.words);
        mSentenceHorn =(Button)view.findViewById(R.id.sentence_pronunciation);

        mMark=(Button)view.findViewById(R.id.mark);
        //mPronunciation=(Button)view.findViewById(R.id.media_pronunciation);
        return view;
    }
    
    public void setCharacter(CharItem thisChar){
        mNowChar=thisChar;
        String s=thisChar.get(CharItem.CHARACTER);
        mFigure.setText(thisChar.get(CharItem.CHARACTER));
        mPinyin.setText(thisChar.get(CharItem.PINYIN));
        mMeaning.setText(thisChar.get(CharItem.EXPLANATION));
        mCharImg.setImageBitmap(thisChar.getImage(getActivity()));
        mSentence.setText(thisChar.get(CharItem.SENTENCE));
        setWords();
        setRadical();
        setPronunciation(thisChar.getMediaPlayer(getActivity()));
        setSentenceMP(thisChar.getSentenceReadable(getActivity()).getMediaPlayer(getActivity()));

        setMark();
        
        
    }

    private void setSentenceMP(MediaPlayer mediaPlayer){
        mSentenceMP=mediaPlayer;
        try{
            mSentenceMP.prepare();
        }
        catch (Exception e){
            Log.d("SentenceMedia:",e.toString());
            e.printStackTrace();
        }
        //发音按键
        View.OnClickListener pronunciationListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mSentenceMP.start();
                }
                catch (Exception e)
                {
                    Log.d("SentenceMedia:",e.toString());
                    e.printStackTrace();
                }
            }
        };

        mSentence.setOnClickListener(pronunciationListener);
        mSentenceHorn.setOnClickListener(pronunciationListener);
    }
    
    private void setPronunciation(MediaPlayer mediaPlayer){
        mMediaPronunciation=mediaPlayer;
        try{
            mMediaPronunciation.prepare();
        }
        catch (Exception e){
            Log.d("PronunciationMedia:",e.toString());
            e.printStackTrace();
        }

        try{
            mMediaPronunciation.start();
        }
        catch (Exception e)
        {
            Log.d("PronunciationMedia:",e.toString());
            e.printStackTrace();
        }
        //发音按键
        mFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mMediaPronunciation.start();
                }
                catch (Exception e)
                {
                    Log.d("PronunciationMedia:",e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    private void setRadical(){

        RadicalItem radicalItem=mNowChar.getRadical();
        mRadical.setText(radicalItem.getRadical());
        mRadical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("component_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                RadicalDialog myRadicalDialog= RadicalDialog.getDialogInstance(mNowChar.getRadical());
                myRadicalDialog.show(ft,"component_dialog");
            }
        });
    }

    private void setWords(){
        WordItem[] words=mNowChar.getWords();
        mWords.removeAllViews();
        int wordNumber = words.length;

        /*LinearLayout linearLayout;
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mWords.addView(linearLayout);*/

        TextView aWord;
        for (int i = 0; i < wordNumber; i++) {
            aWord = new TextView(getActivity());
            aWord.setPadding(10,0,10,0);
            aWord.setTextSize(17);
            aWord.setText(words[i].getChinese()+'/'+words[i].getWord());
            final MediaPlayer wordMP=words[i].getMediaPlayer(getActivity());
            try{
                wordMP.prepare();
            }
            catch (Exception e){
                Log.d("CharFragmentMedia:",e.toString());
                e.printStackTrace();
            }
            aWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wordMP.start();
                }
            });
            mWords.addView(aWord);
        }
    }

    private void setMark(){
        mIsMark=mDataManager.isInCollection(mNowChar);
        if(mIsMark)
            mMark.setBackgroundResource(R.drawable.star_marked);
        else
            mMark.setBackgroundResource(R.drawable.star);


        //收藏按键
        mMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsMark) {
                    mDataManager.removeCollection(mNowChar);
                    mMark.setBackgroundResource(R.drawable.star);
                }
                else {
                    mDataManager.putIntoCollection(mNowChar);
                    mMark.setBackgroundResource(R.drawable.star_marked);
                }
                mIsMark=!mIsMark;
            }
        });
    }
}
