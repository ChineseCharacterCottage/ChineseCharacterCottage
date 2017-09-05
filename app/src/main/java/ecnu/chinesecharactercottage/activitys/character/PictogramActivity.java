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
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 这个类负责显示象形字，有两种模式，分别是按顺序学习象形字和浏览收藏的象形字
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
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //进度条
    @InjectView(id=R.id.progress_bar)
    private ProgressBar mProgressBar;
    //播放按键
    @InjectView(id=R.id.button_play)
    private Button mBtPlay;
    //下一个按键
    @InjectView(id=R.id.button_next)
    private Button mBtNext;
    //当前象形字
    private ShapeCharItem mNowItem;
    //当前象形字编号
    private int mPosition;
    //象形字总数
    private int mTotalNum;
    //象形字列表
    private String[] mIds;
    //重置顺序标志位
    private boolean mIsEmpty;

    /**
     * 静态活动跳转方法，需要传入上下文、字Id数组、模式（学习Or复习）
     * @param context 上下文
     * @param ids 象形字id数组
     * @param model 学习模式：1，复习模式：2
     */
    static public void startActivity(Context context, String[] ids,int model){
        Intent intent=new Intent(context,PictogramActivity.class);
        //传递数据
        intent.putExtra(KEY_IDS,ids);
        intent.putExtra(KEY_MODEL,model);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram);
        init();//初始化

        //设置下一个按键监听器
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtNext.setEnabled(false);//设置为不可点击，直到数据加载完成
                next();//加载数据
            }
        });
        next();//第一次加载数据
    }

    private void init(){
        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.character_fragment);//绑定字显示fragment
        Injecter.autoInjectAllField(this);//绑定控件
        mPosition =0;//初始化索引为0
        mIsEmpty=true;//初始化标志位为true
        mIds=getIntent().getStringArrayExtra(KEY_IDS);//获取字id数组
        mModel=getIntent().getIntExtra(KEY_MODEL,LEARNING);//获取模式
        mTotalNum=mIds.length;//初始化字数量
        mProgressBar.setMax(mTotalNum);//设置进度条长度
    }

    //根据数据的情况，开线程获取数据，更新页面显示的信息
    private void next(){
        if(mPosition<mTotalNum) {//判断索引是否合法
            //创建一个异步对象从服务器获取象形字相关数据
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    DataManager dataManager = DataManager.getInstance(PictogramActivity.this);//获取DataManager实例
                    mNowItem = dataManager.getShapeCharItem(mIds[mPosition]);//获取对应id的象形字
                    mPosition++;//索引自增
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    if (mNowItem != null) {//保证字数据存在
                        if(mModel==LEARNING){//学习模式要存学习的顺序
                            if(mIsEmpty&&mPosition>1)//判断mPosition的原因是第一个字是上次退出时候的字，所以
                                mIsEmpty=false;//设置标志位为false
                            LearningOrderManager orderManager=LearningOrderManager.getManager(PictogramActivity.this);//获取顺序管理器实例
                            orderManager.saveOrder(LearningOrderManager.PICTOGRAM_LEARNING,mIds[mPosition-1]);//存储顺序
                        }
                        mCharacterFragment.setCharacter(mNowItem);//设置fragment显示的字内容
                        //设置播放视频按键监听器
                        mBtPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //利用intent调用系统播放器播放视频
                                Uri uri = Uri.parse(VIDEO_PATH + mNowItem.getVideo());//构造视频地址
                                Intent intent = new Intent(Intent.ACTION_VIEW);//创建相应intent
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//设置这个标志表示清除先前启动的视频播放活动
                                intent.setDataAndType(uri, "video/*");//设置视频地址和视频格式
                                startActivity(intent);
                            }
                        });
                        //激活下一个按键
                        mBtNext.setEnabled(true);
                        //设置进度条
                        mProgressBar.setProgress(mPosition);
                    } else if (mPosition < mTotalNum) {//如果数据为空且id数组还有剩下的id，则跳到下一个id
                        next();
                    } else {//结束
                        saveData();
                    }
                }
            };
            task.execute();//执行异步对象
        }else//结束
            saveData();
    }

    //结束活动前判断情况来保存数据，结束时提示用户学习结束
    private void saveData(){
        if(LEARNING==mModel&&mIsEmpty){//为true说明本次还没学习就结束了，要重新开始
            //重置顺序
            LearningOrderManager orderManager=LearningOrderManager.getManager(PictogramActivity.this);//获取顺序管理器实例
            orderManager.saveOrder(LearningOrderManager.PICTOGRAM_LEARNING,1);//重置顺序为1
            //重新开始
            //初始化
            mPosition =0;//初始化索引为0
            mIsEmpty=true;//初始化标志位为true
            mIds=new String[3];//重置字id数组
            for (int i=1;i<mIds.length;i++)//构造id数组，由于第一个字必然是有的，所以直接构造后面的id就好
                mIds[i]=String.valueOf(i+1);
            mTotalNum=mIds.length;//初始化字数量
            mProgressBar.setMax(mTotalNum);//设置进度条长度
            next();//更新页面
        }else {
            Toast.makeText(PictogramActivity.this, "Learning finish  ", Toast.LENGTH_SHORT).show();//提示学习结束
            finish();//学习完毕
        }
    }
}
