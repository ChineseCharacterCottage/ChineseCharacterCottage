package ecnu.chinesecharactercottage.activitys.knowledge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.Knowledge;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/2.
 */

public class KnowledgeDetailsActivity extends Activity {

    //视频地址
    private static String VIDEO_PATH="http://115.159.147.198/HZW_web/video/";
    //文字标题view
    @InjectView(id=R.id.tv_title)
    private TextView mTitleView;
    //视频view
    @InjectView(id=R.id.vv_knowledge)
    private VideoView mVideoView;
    //文章内容view
    @InjectView(id=R.id.tv_knowledge)
    private TextView mTextView;
    //文章id
    private String mId;
    //文章实体
    private Knowledge mKnowledge;


    public static void startActivity(Context context, String id){
        Intent intent=new Intent(context,KnowledgeDetailsActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_knowledge_details);
        Injecter.autoInjectAllField(this);

        mId=getIntent().getStringExtra("id");
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(KnowledgeDetailsActivity.this);
                mKnowledge=dataManager.getKnowledge(mId);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                setKnowledge();
            }
        };
        task.execute();
    }

    private void setKnowledge(){
        mTitleView.setText(mKnowledge.title);
        mTextView.setText(mKnowledge.text);
        mVideoView.setVideoURI(Uri.parse(VIDEO_PATH+mKnowledge.video));
        mVideoView.setMediaController(new MediaController(this));
    }
}
