package ecnu.chinesecharactercottage;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

/**
 * Created by 10040 on 2016/12/4.
 */

public class CharacterFragment extends Fragment {

    //部首id
    private String mRadicalId;

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
    private LinearLayout mWords;
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
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");
        mFigure.setTypeface(face);
        mPinyin=(TextView)view.findViewById(R.id.pinyin);
        mCharImg=(ImageView)view.findViewById(R.id.char_image);
        mRadical=(TextView)view.findViewById(R.id.radical);
        mMeaning=(TextView)view.findViewById(R.id.meaning);
        mPart=(TextView)view.findViewById(R.id.component);
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
        mMediaPronunciation=thisChar.getMediaPlayer(getActivity());
        mSentence.setText(thisChar.get(CharItem.SENTENCE));

        RadicalItem radical=thisChar.getRadical();
        mRadicalId=radical.getId();
        mRadical.setText(radical.getRadical());
        mRadical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadicalLab radicalLab;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("component_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                try {
                    radicalLab = RadicalLab.getLabWithoutContext();
                } catch (Exception e) {
                    Log.d("getLab IOException", e.toString());
                    getActivity().finish();
                    return;
                }
                if (radicalLab == null) {
                    Log.d("charItemLab", "is null");
                }

                RadicalDialog myRadicalDialog= RadicalDialog.getDialogInstance(radicalLab.getRadical(mRadicalId));
                myRadicalDialog.show(ft,"component_dialog");
            }
        });


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
