package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by huge on 2016/9/17.
 * It is the Learning-Modular of CCC.
 */
public class HSKActivity extends Activity {

    //当前学习数量
    int mLearnedNumber=0;
    //学习总量
    int mTotalNumber=0;

    //汉字详情
    private CharacterFragment mCharacterFragment;

    //确认按键
    private Button mButtonNext;

    //进度条
    private ProgressBar mProgressBar;

    //汉字库
    private CharItemLab mCharItemLab;
    //本次学习目标列表
    private CharItem[] mThisCharList;
    //当前汉字
    private CharItem mThisCharItem;

    /*static public void startHSKLeaning(Context context){
        Intent intent=new Intent(context,HSKActivity.class);
        context.startActivity(intent);
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsk);
        //初始化
        init();

        //显示首个汉字
        setCharacter();

        //设置监听器
        mButtonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //统计当前学习量
                mLearnedNumber++;

                if(mLearnedNumber>mTotalNumber) {
                    saveDate();
                }
                else{
                    setCharacter();
                }

                if(mLearnedNumber==mTotalNumber) {
                    mButtonNext.setText(R.string.button_finish);
                }
            }

        });


    }

    private void init(){
        try{
            mCharItemLab=CharItemLab.getLab(HSKActivity.this);
        }
        catch (IOException exp){
            finish();
            return;
        }
        catch(JSONException exp){
            finish();
            return;
        }
        if(mCharItemLab==null){
        }

        String charId[]=new String[]{"1","2","3"};//以后要从文件中读取
        mLearnedNumber=getIntent().getIntExtra("learned_number",0);

        try {
            mThisCharList = mCharItemLab.getCharItems(charId);
        }
        catch (Exception e)
        {
            finish();
            return;
        }
        mTotalNumber=mThisCharList.length-1;

        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mProgressBar.setMax(mTotalNumber+1);

        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.character_fragment);

        mButtonNext=(Button)findViewById(R.id.button_next);
    }

    private void setCharacter() {
        //设置进度条
        mProgressBar.setProgress(mLearnedNumber+1);

        //更新汉字
        if(mLearnedNumber>mTotalNumber)
            return;
        mThisCharItem=mThisCharList[mLearnedNumber];
        mCharacterFragment.setCharacter(mThisCharItem);

    }

    private void saveDate(){
        //保存数据

        Intent intent =new Intent();
        intent.putExtra("learned_number",mLearnedNumber);
        setResult(RESULT_OK,intent);
        finish();
    }

}
