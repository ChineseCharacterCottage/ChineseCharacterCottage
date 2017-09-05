package ecnu.chinesecharactercottage.activitys.collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import ecnu.chinesecharactercottage.activitys.character.PictogramActivity;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.ChoseItem;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.modelsForeground.testFragments.TestChoseListenDialog;
import ecnu.chinesecharactercottage.modelsForeground.testFragments.TestChoseReadDialog;

/**
 * @author 胡家斌
 * 这个活动是收藏模块的主界面，在这里选择进入哪个收藏模块。
 */

public class CollectionChoseActivity extends Activity{
    //字收藏按键
    @InjectView(id=R.id.bt_character)
    private ChoseItem mBtCharacter;
    //象形字收藏按键
    @InjectView(id=R.id.bt_pictogram)
    private ChoseItem mBtPictogram;
    //测试题收藏按键
    //听力按键
    @InjectView(id=R.id.bt_listen)
    private ChoseItem mListen;
    //阅读按键
    @InjectView(id=R.id.bt_read)
    private ChoseItem mRead;

    /**
     * 静态活动跳转方法
     * @param context 需要跳转的活动上下文
     */
    public static void startActivity(Context context){
        Intent intent=new Intent(context,CollectionChoseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_chose);//设置布局文件
        Injecter.autoInjectAllField(this);//利用注释类动态绑定布局中的控件,详细请查看modesForeground.inject包中的两个类

        setListener();//设置这些按键的监听器
        
    }
    
    private void setListener(){
        //下面获取收藏列表用的全是异步方法，这其实是为了将来扩展方便而这么做的。
        //实际情况是，我们的收藏表全部都是放在本地的，所以根本不需要特意开线程来获取数据

        //设置字收藏学习按键监听器
        mBtCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构造一个异步对象，从DataManager类实例中获取收藏字id列表，然后跳转到汉字详情活动
                AsyncTask task= new AsyncTask(){
                    //doInBackground方法会在子线程中运行
                    @Override
                    protected Object doInBackground(Object[] params) {
                        DataManager dataManager=DataManager.getInstance(CollectionChoseActivity.this);//获取DaaManager实例
                        String[] ids=dataManager.getCollectionCharsId(false);//从DataManager实例获取收藏的汉字id列表
                        //获取到的id列表是string的数组（不要问我为什么一会int一会string），转换成了int数组
                        int[] ids2=new int[ids.length];
                        for (int i=0;i<ids.length;i++)
                            ids2[i]=Integer.parseInt(ids[i]);
                        return ids2;//返回获取到的id列表
                    }

                    //onPostExecute方法在主线程中运行，它的参数是doInBackground的返回值
                    @Override
                    protected void onPostExecute(Object o) {
                        int[] ids=(int[])o;//将参数转化回int数组表示的id列表
                        //跳转到汉字详情活动
                        CharacterActivity.startActivity(CollectionChoseActivity.this,ids);

                    }
                };
                task.execute();//执行异步对象
            }
        });

        //设置象形字收藏学习按键监听器
        mBtPictogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构造一个异步对象，从DataManager类实例中获取收藏象形字id列表，然后跳转到象形字详情活动
                AsyncTask task= new AsyncTask(){
                    //doInBackground方法会在子线程中运行
                    @Override
                    protected Object doInBackground(Object[] params) {
                        DataManager dataManager=DataManager.getInstance(CollectionChoseActivity.this);//获取DaaManager实例
                        String[] ids=dataManager.getCollectionCharsId(true);//从DataManager实例获取收藏的汉字id列表
                        return ids;//返回获取到的id列表
                    }

                    //onPostExecute方法在主线程中运行，它的参数是doInBackground的返回值
                    @Override
                    protected void onPostExecute(Object o) {
                        String[] ids=(String[])o;//将参数转化回int数组表示的id列表
                        //跳转到象形字详情活动
                        PictogramActivity.startActivity(CollectionChoseActivity.this,ids,PictogramActivity.COLLECTION);

                    }
                };
                task.execute();//执行异步对象
            }
        });


        //设置听力测试题收藏学习按键监听器
        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建听力测试类型选择对话框
                new TestChoseListenDialog(CollectionChoseActivity.this,1).show();
            }
        });

        //设置阅读测试题收藏学习按键监听器
        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建阅读测试类型选择对话框
                new TestChoseReadDialog(CollectionChoseActivity.this,1).show();

            }
        });
    }
}
