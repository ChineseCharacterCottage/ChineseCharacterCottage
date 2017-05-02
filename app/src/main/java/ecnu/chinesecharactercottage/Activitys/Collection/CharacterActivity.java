package ecnu.chinesecharactercottage.activitys.collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/4/30.
 */

public class CharacterActivity extends Activity {
    //进度条
    private ProgressBar mProgressBar;
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //下一个按键
    private Button mBtNext;
    //当前字
    private CharItem mNowItem;
    //当前字编号
    private int mPosition;
    //字总数
    private int mTotalNum;
    //字列表
    private int[] mIds;

    static public void startActivity(Context context, int[] ids){
        Intent intent=new Intent(context,CharacterActivity.class);
        intent.putExtra("ids",ids);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        init();

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtNext.setEnabled(false);
                next();
            }
        });
        next();
    }

    private void init(){
        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.character_fragment);
        mBtNext=(Button)findViewById(R.id.button_next);
        mPosition =0;
        mIds=getIntent().getIntArrayExtra("ids");
        mTotalNum=mIds.length;
        mProgressBar.setMax(mTotalNum);
    }

    private void next(){
        if(mPosition<mTotalNum) {
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    DataManager dataManager = DataManager.getInstance(CharacterActivity.this);
                    mNowItem = dataManager.getCharItemById(mIds[mPosition]);
                    mPosition++;
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    if (mNowItem != null) {
                        mCharacterFragment.setCharacter(mNowItem);
                        mBtNext.setEnabled(true);
                        mProgressBar.setProgress(mPosition);
                    } else if (mPosition < mTotalNum) {
                        next();
                    } else {
                        saveData();
                    }
                }
            };
            task.execute();
        }
        else
            saveData();
    }

    private void saveData(){
        Toast.makeText(CharacterActivity.this, "Collection finish", Toast.LENGTH_SHORT).show();
        finish();
    }
}
