package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 10040 on 2016/11/19.
 */

public class RadicalActivity extends Activity {

    //部首列表
    Object[] mRadicalArray;
    //部首个数
    int mRadicalNumber;
    //当前部首序号
    int mRadicalNow;

    //部首字形
    private TextView mFigure;
    //部首图片
    private ImageView mImage;
    //部首中文名
    private TextView mChineseName;
    //部首英文名
    private TextView mEnglishName;
    //部首中文意思
    private TextView mMeaning;
    //形近部首
    private TextView mSimilarity;
    //部首例字
    private ExampleCharacter mExampleCharacter;
    //下一个
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radical);

        init();//初始化

        setRadical();
        
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRadicalNow++;
                if(mRadicalNow<=mRadicalNumber){
                    if(mRadicalNow==mRadicalNumber)
                        mButton.setText("finish");
                    setRadical();
                }
                else {
                    saveDate();
                }
            }
        });

    }

    private void init(){
        mFigure=(TextView)findViewById(R.id.radical_figure);
        mImage=(ImageView)findViewById(R.id.radical_image);
        mChineseName=(TextView)findViewById(R.id.radical_chinese_name);
        mEnglishName=(TextView)findViewById(R.id.radical_english_name);
        mMeaning=(TextView)findViewById(R.id.radical_meaning);
        mSimilarity=(TextView)findViewById(R.id.radical_similarity);
        mExampleCharacter=(ExampleCharacter)findViewById(R.id.radical_example);
        mButton=(Button)findViewById(R.id.next_radical);

        mRadicalArray=getmRadicalArray();
        mRadicalNumber=mRadicalArray.length;
        mRadicalNow=0;
    }
    
    private void setRadical(){
        //Character = mRadicalArray(mRadicalNow);
        //mFigure.setText(Character.figure);
    }

    private void saveDate(){
        //save data

        //return to mainActivity
        finish();

    }

    //临时方法
    private Object[] getmRadicalArray(){
        return new Object[4];
    }
}
