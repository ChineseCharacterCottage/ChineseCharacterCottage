package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;

import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.ModelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/12/11.
 */

public class ReviewActivity extends Activity {

    //每次读取数目
    private final int NUMBER=20;

    //前置标记数目
    private int mBaseNumber;
    //当前复习位置
    private int mPosition =0;
    //收藏夹总量
    private int mTotalNumber=0;
    //当前列表长度
    private int mNumber=0;

    //汉字详情
    private CharacterFragment mCharacterFragment;

    //确认按键
    private Button mButtonNext;

    //汉字库
    private CharItemLab mCharItemLab;
    //收藏夹字id
    private String[] mCharIDs;
    //当前学习字列表
    private CharItem[] mThisCharList;
    //当前汉字
    private CharItem mThisCharItem;

    public static void starActivity(Context context,String[] charId){
        Intent intent=new Intent(context,ReviewActivity.class);
        intent.putExtra("charId",charId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //初始化
        init();

        //显示首个汉字
        setCharacter();

        if(mPosition+mBaseNumber == mTotalNumber) {
            mButtonNext.setText(R.string.button_finish);
        }
        //设置监听器
        mButtonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //统计当前学习量
                mPosition++;

                if(mPosition+mBaseNumber>mTotalNumber) {
                    saveDate();
                }
                else{
                    setCharacter();
                }

                if(mPosition+mBaseNumber == mTotalNumber) {
                    mButtonNext.setText(R.string.button_finish);
                }
            }

        });
    }

    private void init(){
        try{
            mCharItemLab=CharItemLab.getLab(ReviewActivity.this);
        }
        catch (IOException exp){
            finish();
            return;
        }
        catch(JSONException exp){
            finish();
            return;
        }

        Intent intent=getIntent();
        mCharIDs=intent.getStringArrayExtra("charId");
        mPosition =intent.getIntExtra("learned_number",0);
        mTotalNumber=mCharIDs.length-1;
        mBaseNumber=intent.getIntExtra("baseNumber",0);
        mNumber=0;

        buildList();
        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.character_fragment);
        mButtonNext=(Button)findViewById(R.id.button_next);
    }

    private void setCharacter(){
        //更新汉字
        if(mPosition>mNumber)
            buildList();
        mThisCharItem=mThisCharList[mPosition];
        mCharacterFragment.setCharacter(mThisCharItem);
    }

    private void buildList(){
        mBaseNumber+=mNumber;
        if(mBaseNumber+NUMBER<mTotalNumber){
            mNumber=NUMBER;
        }else{
            mNumber=mTotalNumber-mBaseNumber+1;
        }
        String[] char_id=new String[mNumber];

        for(int i=0;i<mNumber;i++)
            char_id[i]=mCharIDs[i+mBaseNumber];
        try {
            mThisCharList = mCharItemLab.getCharItems(char_id);
        }
        catch (Exception e)
        {
            Log.d("mThisCharList:",e.toString());
            finish();
            return;
        }
        mPosition=0;
    }

    private void saveDate(){
        /*Intent intent =new Intent();
        intent.putExtra("baseNumber",mPosition+mBaseNumber);
        setResult(RESULT_OK,intent);*/
        finish();
    }
}
