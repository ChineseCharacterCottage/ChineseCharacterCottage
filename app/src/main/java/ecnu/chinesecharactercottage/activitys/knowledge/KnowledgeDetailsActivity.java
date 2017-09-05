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
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.Knowledge;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.R;

/**
 * @author 胡家斌
 * 这个活动负责显示小知识的详情
 */

public class KnowledgeDetailsActivity extends Activity {

    //数据键值
    private static String ID="id";
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

    /**
     * 活动静态跳转方法
     * @param context 上下文
     * @param id 需要显示的小知识的id
     */
    public static void startActivity(Context context, String id){
        Intent intent=new Intent(context,KnowledgeDetailsActivity.class);
        intent.putExtra(ID,id);//传递数据
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_knowledge_details);//设置布局
        Injecter.autoInjectAllField(this);//绑定控件

        mId=getIntent().getStringExtra(ID);//获取id
        //创建异步对象，从服务器获取数据并更新界面内容
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(KnowledgeDetailsActivity.this);//获取DataManager实例
                mKnowledge=dataManager.getKnowledge(mId);//利用DataManager实例获取小知识的数据
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                setKnowledge();//设置页面的内容
            }
        };
        task.execute();
    }

    private void setKnowledge(){
        mTitleView.setText(mKnowledge.title);//更新标题
        mTextView.setText(mKnowledge.text);//更新说明
        mVideoView.setVideoURI(Uri.parse(VIDEO_PATH+mKnowledge.video));//设置视频地址
        mVideoView.setMediaController(new MediaController(this));//添加视频控制器（现在还是非常丑的直接从手机底端弹出的样子-_-）
    }
}
