package ecnu.chinesecharactercottage.activitys.character;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.ShapeCharItem;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.LearningOrderManager;

/**
 * Created by 10040 on 2017/3/21.
 */

public class PictogramActivity extends Activity {
    //数据键值
    private static String KEY_IDS="ids";
    private static String KEY_MODEL="model";
    //视频地址
    private static String VIDEO_PATH="http://115.159.147.198/HZW_web/video/";
    //模式
    private int mModel;
    public static int LEARNING=1;
    public static int COLLECTION=2;
    //进度条
    private ProgressBar mProgressBar;
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //播放按键
    private Button mBtPlay;
    //下一个按键
    private Button mBtNext;
    //当前象形字
    private ShapeCharItem mNowItem;
    //当前象形字编号
    private int mPosition;
    //象形字总数
    private int mTotalNum;
    //象形字列表
    private String[] mIds;

    static public void startActivity(Context context, String[] ids,int model){
        Intent intent=new Intent(context,PictogramActivity.class);
        intent.putExtra(KEY_IDS,ids);
        intent.putExtra(KEY_MODEL,model);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram);
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
        mBtPlay=(Button)findViewById(R.id.button_play);
        mBtNext=(Button)findViewById(R.id.button_next);
        mPosition =0;
        mIds=getIntent().getStringArrayExtra(KEY_IDS);
        mModel=getIntent().getIntExtra(KEY_MODEL,LEARNING);
        mTotalNum=mIds.length;
        mProgressBar.setMax(mTotalNum);
    }

    private void next(){
        if(mPosition<mTotalNum) {
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    DataManager dataManager = DataManager.getInstance(PictogramActivity.this);
                    mNowItem = dataManager.getShapeCharItem(mIds[mPosition]);
                    mPosition++;
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    if (mNowItem != null) {
                        if(mModel==LEARNING){
                            LearningOrderManager orderManager=LearningOrderManager.getManager(PictogramActivity.this);
                            orderManager.saveOrder(LearningOrderManager.PICTOGRAM_LEARNING,mPosition-1);
                        }
                        mCharacterFragment.setCharacter(mNowItem);
                        mBtPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse(VIDEO_PATH + mNowItem.getVideo());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setType("video/*");
                                intent.setDataAndType(uri, "video/*");
                                startActivity(intent);
                            }
                        });
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
        }else
            saveData();
    }

    private void saveData(){
        Toast.makeText(PictogramActivity.this, "Learning finish", Toast.LENGTH_SHORT).show();
        finish();
    }
}
