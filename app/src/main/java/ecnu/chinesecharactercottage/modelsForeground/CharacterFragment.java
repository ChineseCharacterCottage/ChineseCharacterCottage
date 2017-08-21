package ecnu.chinesecharactercottage.modelsForeground;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.modelsBackground.*;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2016/12/4.
 */

public class CharacterFragment extends Fragment {

    //当前字
    CharItem mNowChar;

    //字形
    @InjectView(id=R.id.figure)
    private TextView mFigure;
    //拼音
    @InjectView(id=R.id.pinyin)
    private TextView mPinyin;
    //字意
    @InjectView(id=R.id.meaning)
    private TextView mMeaning;
    //图片
    @InjectView(id=R.id.char_image)
    private ImageView mCharImg;
    //部首
    @InjectView(id=R.id.radical)
    private TextView mRadical;
    //组词
    @InjectView(id=R.id.words)
    private LinearLayout mWords;
    //例句
    @InjectView(id=R.id.char_sentence)
    private TextView mSentence;
    //例句喇叭
    @InjectView(id=R.id.sentence_pronunciation)
    private Button mSentenceHorn;
    //收藏按键
    @InjectView(id=R.id.mark)
    private Button mMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_character,container,false);
        Injecter.autoInjectAllField(this,view);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");
        mFigure.setTypeface(face);

        return view;
    }
    
    public void setCharacter(CharItem thisChar){
        mNowChar=thisChar;
        String s=thisChar.get(CharItem.CHARACTER);
        mFigure.setText(thisChar.get(CharItem.CHARACTER));
        mPinyin.setText(thisChar.get(CharItem.PINYIN));
        mMeaning.setText(thisChar.get(CharItem.EXPLANATION));
        new ImageGetter(getActivity(),thisChar.getImageId(),mCharImg).setImage();
        mSentence.setText(thisChar.get(CharItem.SENTENCE));
        setWords();
        setRadical();
        new MPGetter(getActivity(),thisChar,mFigure).setMP(true);
        new MPGetter(getActivity(),thisChar.getSentenceReadable(getActivity()),mSentence).setMP();

        new Marker(getActivity()).setMark(mMark,mNowChar);
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
            new MPGetter(getActivity(),words[i],aWord).setMP();
            mWords.addView(aWord);
        }
    }
}
