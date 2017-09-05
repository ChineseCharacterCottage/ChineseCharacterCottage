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
 * @author 胡家斌
 * 这个Fragment负责显示字的详细信息，在多个活动中都有被调用
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
        View view=inflater.inflate(R.layout.fragment_character,container,false);//渲染布局
        Injecter.autoInjectAllField(this,view);//绑定控件

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"font/1.ttf");//获取字体
        mFigure.setTypeface(face);//设置田字格上字形的字体

        return view;
    }

    /**
     * 设置需要显示的字
     * @param thisChar 需要显示详情的字的数据
     */
    public void setCharacter(CharItem thisChar){
        mNowChar=thisChar;//保存传入的需要显示的
        mFigure.setText(thisChar.get(CharItem.CHARACTER));//设置字形
        mPinyin.setText(thisChar.get(CharItem.PINYIN));//设置拼音
        mMeaning.setText(thisChar.get(CharItem.EXPLANATION));//设置意思
        new ImageGetter(getActivity(),thisChar.getImageId(),mCharImg).setImage();//设置图片
        mSentence.setText(thisChar.get(CharItem.SENTENCE));//设置例句
        setWords();//设置例词
        setRadical();//设置部首
        new MPGetter(getActivity(),thisChar,mFigure).setMP(true);//设置点击字形发音
        new MPGetter(getActivity(),thisChar,mPinyin).setMP(true);//设置点击拼音发音
        new MPGetter(getActivity(),thisChar.getSentenceReadable(getActivity()),mSentence).setMP();//设置句子点击发音
        new MPGetter(getActivity(),thisChar.getSentenceReadable(getActivity()),mSentenceHorn).setMP();//设置句子喇叭点击发音

        new Marker(getActivity()).setMark(mMark,mNowChar);//设置收藏
    }

    //设置部首相关的内容，包括点击部首的监听器
    private void setRadical(){
        RadicalItem radicalItem=mNowChar.getRadical();//获取字的部首
        mRadical.setText(radicalItem.getRadical());//设置部首字形
        //设置部首点击监听器，会弹出一个部首详情对话框
        mRadical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示部首详情对话框
                RadicalDialog.getDialogInstance(mNowChar.getRadical()).show();
            }
        });
    }

    //设置例词的相关内容，包括每个字的点击监听器
    private void setWords(){
        WordItem[] words=mNowChar.getWords();//获取例词数组
        mWords.removeAllViews();//先移除例词控件中的内容

        /*如果需要分行显示，可以再插入新的线性布局
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mWords.addView(linearLayout);*/

        TextView aWord;
        for (WordItem wordItem : words) {
            aWord = new TextView(getActivity());//新建一个显示单个例词的控件
            aWord.setPadding(10,0,10,0);//设置控件的内边距
            aWord.setTextSize(17);//设置控件的字体大小
            aWord.setText(wordItem.getChinese()+'/'+wordItem.getWord());//设置控件的内容，显示为该词的中文/英文
            new MPGetter(getActivity(),wordItem,aWord).setMP();//设置该词点击后发音
            mWords.addView(aWord);//将该词添加到例词布局中
        }
    }
}
