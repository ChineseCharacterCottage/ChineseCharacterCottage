package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.ShapeCharItem;
import ecnu.chinesecharactercottage.ModelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/3/21.
 */

public class PictogramActivity extends Activity {
    //视频地址
    private static String VIDEO_PATH="http://115.159.147.198/HZW_web/video/";
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //视频
    //private VideoView mVideoView;
    //等待进度条
    //private Dialog mDialog;
    //播放按键
    private Button mBtPlay;
    //下一个按键
    private Button mBtNext;
    //当前象形字
    private ShapeCharItem mNowItem;
    //当前象形字编号
    private int mItemId;

    static public void startActivity(Context context){
        Intent intent=new Intent(context,PictogramActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram_learning);
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
        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.character_fragment);
        //mVideoView=(VideoView)findViewById(R.id.video);
        //mVideoView.setMediaController(new MediaController(this));
        mBtPlay=(Button)findViewById(R.id.button_play);
        mBtNext=(Button)findViewById(R.id.button_next);
        mItemId=1;
    }

    private void next(){
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(PictogramActivity.this);
                mNowItem=dataManager.getShapeCharItem(String.valueOf(mItemId));
                mItemId++;
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(mNowItem!=null){
                    mCharacterFragment.setCharacter(mNowItem);
                    mBtPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(VIDEO_PATH+mNowItem.getVideo());
                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setType("video/*");
                            intent.setDataAndType(uri , "video/*");
                            startActivity(intent);
                        }
                    });
                    mBtNext.setEnabled(true);
                    /*
                    mDialog= ProgressDialog.show(PictogramActivity.this,"loading...","wait please");
                    mVideoView.setVideoURI(Uri.parse(VIDEO_PATH+mNowItem.getVideo()));
                    mVideoView.requestFocus();
                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            Log.d("prepared","prepared");
                            mDialog.dismiss();
                        }
                    });
                    mVideoView.start();*/
                }else
                    saveData();
            }
        };
        task.execute();
    }

    private void saveData(){
        finish();
    }
}
