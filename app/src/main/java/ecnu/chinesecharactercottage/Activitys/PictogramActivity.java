package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/3/21.
 */

public class PictogramActivity extends Activity {
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //视频
    private VideoView mVideoView;
    //下一个按键
    private Button mBtNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram_learning);
        init();


    }

    private void init(){
        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.character_fragment);
        mVideoView=(VideoView)findViewById(R.id.video);
        mBtNext=(Button)findViewById(R.id.button_next);
    }

    private void next(){
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(PictogramActivity.this);
                return null;
            }
        }
    }
}
