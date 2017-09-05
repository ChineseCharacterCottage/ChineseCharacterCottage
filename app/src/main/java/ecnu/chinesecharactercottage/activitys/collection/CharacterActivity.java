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

import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

/**
 * @author 胡家斌
 * 这个类负责显示汉字，目前只在收藏部分用到，按顺序浏览收藏的所有字
 */

public class CharacterActivity extends Activity {
    //数据键值
    private final static String IDS="ids";
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //进度条
    private ProgressBar mProgressBar;
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

    /**
     * 静态活动跳转方法
     * @param context 上下文
     * @param ids 字id数组
     */
    static public void startActivity(Context context, int[] ids){
        Intent intent=new Intent(context,CharacterActivity.class);
        intent.putExtra(IDS,ids);//传递数据
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);//设置布局

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
        //绑定控件
        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mBtNext=(Button)findViewById(R.id.button_next);

        mPosition =0;//初始化索引为0
        mIds=getIntent().getIntArrayExtra(IDS);//获取字id数组
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
                    DataManager dataManager = DataManager.getInstance(CharacterActivity.this);//获取DataManager实例
                    mNowItem = dataManager.getCharItemById(mIds[mPosition]);//获取对应id的字
                    mPosition++;//索引自增
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    if (mNowItem != null) {//保证字数据存在
                        mCharacterFragment.setCharacter(mNowItem);//设置fragment显示的字内容
                        mBtNext.setEnabled(true);//激活下一个按键
                        mProgressBar.setProgress(mPosition);//更新进度条
                    } else if (mPosition < mTotalNum) {//如果数据为空且id数组还有剩下的id，则跳到下一个id
                        next();
                    } else {//结束
                        saveData();
                    }
                }
            };
            task.execute();//执行异步对象
        }
        else//结束
            saveData();
    }

    private void saveData(){
        Toast.makeText(CharacterActivity.this, "Collection finish", Toast.LENGTH_SHORT).show();//提示学习结束
        finish();//学习完毕
    }
}
