package ecnu.chinesecharactercottage.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

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

    //本次学习目标列表
    private CharItem[] mThisCharList;
    //当前汉字
    private CharItem mThisCharItem;

    static public void startHSKLeaning(Context context,int[] charIds){
        Intent intent=new Intent(context,HSKActivity.class);
        intent.putExtra("charIds",charIds);
        context.startActivity(intent);
    }


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

        int[] charIds=getIntent().getIntArrayExtra("charids");
        mLearnedNumber=getIntent().getIntExtra("learned_number",0);

        AsyncTask task=new AsyncTask<int[],Object,CharItem[]>() {
            @Override
            protected CharItem[] doInBackground(int[][] params) {
                DataManager dataManager=DataManager.getInstance(HSKActivity.this);
                return dataManager.getCharItemByIds(params[0]);
            }

            @Override
            protected void onPostExecute(CharItem[] charItems) {
                mThisCharList=charItems;
            }
        };
        task.execute(charIds);

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

    @Override
    public void onBackPressed(){
        saveDate();
    }

}
