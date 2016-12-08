package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

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
    
    private void setRadical() {
        try {
            mFigure.setText(sRadical.getRadical());
            //mImage.setImageBitmap(sRadical.getImage());
            mEnglishName.setText(sRadical.getName());
            //mMeaning.setText(sRadical.getMeaning));

            String[] examples = sRadical.getExamples();
            int exampleNumber = examples.length;

            LinearLayout linearLayout;
            linearLayout = new LinearLayout(this);//为了解决报错

            TextView aExample;
            for (int i = 0; i < exampleNumber; i++) {
                if (i % 5 == 0) {
                    linearLayout = new LinearLayout(this);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    mExampleCharacter.addView(linearLayout);
                }
                aExample = new TextView(this);
                aExample.setText(examples[i]);
                aExample.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharItem exampleItem;
                        CharItemLab charItemLab;
                        //获取对应例字
                        try {
                            charItemLab = CharItemLab.getLab(RadicalActivity.this);
                        } catch (IOException exp) {
                            Log.d("getLab IOException", exp.toString());
                            finish();
                            return;
                        } catch (JSONException exp) {
                            Log.d("getLab JsonException", exp.toString());
                            finish();
                            return;
                        }
                        if (charItemLab == null) {
                            Log.d("charItemLab", "is null");
                        }
                        try {
                            exampleItem = charItemLab.findCharItemsByShape(((TextView) view).getText().toString())[0];
                            ExampleActivity.startActivity(RadicalActivity.this,exampleItem);
                        } catch (Exception e) {
                            Log.d("findCharItemsByShape", e.toString());
                            e.printStackTrace();
                        }
                    }
                });
                linearLayout.addView(aExample);
            }
        } catch (Exception e) {
            Log.d("example", e.toString());
        }
    }

    private void saveDate(){
        //save data

        //return to mainActivity
        finish();

    }
}
