package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 10040 on 2016/11/19.
 */

public class RadicalActivity extends Activity {

    //部首
    private static RadicalItem sRadical;

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
    private LinearLayout mExampleCharacter;

    public static void startActivity(Context context,RadicalItem radicalItem){
        sRadical=radicalItem;
        Intent intent=new Intent(context,RadicalActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radical_item);

        init();//初始化

        setRadical();

    }

    private void init(){
        mFigure=(TextView)findViewById(R.id.radical_figure);
        mImage=(ImageView)findViewById(R.id.radical_image);
        mChineseName=(TextView)findViewById(R.id.radical_chinese_name);
        mEnglishName=(TextView)findViewById(R.id.radical_english_name);
        mMeaning=(TextView)findViewById(R.id.radical_meaning);
        mSimilarity=(TextView)findViewById(R.id.radical_similarity);
        mExampleCharacter=(LinearLayout) findViewById(R.id.radical_example);

    }
    
    private void setRadical(){
        mFigure.setText(sRadical.getRadical());
        //mImage.setImageBitmap(sRadical.getImage());
        mEnglishName.setText(sRadical.getName());
        //mMeaning.setText(sRadical.getMeaning));
        mExampleCharacter=new ExampleCharacter(sRadical.getExamples());
    }

    private void saveDate(){
        //save data

        //return to mainActivity
        finish();

    }
}
