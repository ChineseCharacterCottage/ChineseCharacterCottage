package ecnu.chinesecharactercottage.activitys.character;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.LearningOrderManager;
import ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments.*;

/**
 * @author 胡家斌
 * 主学习活动，分为4个阶段，每个阶段都是一个fragment，
 * 在活动中只负责各个fragment间的转换，主要是显示相应Fragment，隐藏其他fragment
 */

public class MainLearningActivity extends Activity {

    //顺序存储管理器
    LearningOrderManager mLearningOrderManager;
    //数据管理器
    DataManager mDataManager;
    //4个fragment
    BeginLearningFragment mBeginLearningFragment;
    CharsLearningFragment mCharsLearningFragment;
    ComponentTestFragment mComponentTestFragment;
    CharsTestFragment mCharsTestFragment;

    //当前学习编号
    int mOrder;
    //当前学习部件
    ComponentItem mComponentItem;


    /**
     * 静态活动跳转方法
     * @param context 需要跳转的活动上下文
     */
    static public void startActivity(Context context){
        Intent intent=new Intent(context,MainLearningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_learning);//设置布局

        mLearningOrderManager=LearningOrderManager.getManager(this);//获取顺序管理器实例
        mDataManager=DataManager.getInstance(this);//获取数据管理器实例

        FragmentManager fm=getFragmentManager();//获取碎片管理器实例
        //通过碎片管理器绑定布局中的4个fragment
        mBeginLearningFragment=(BeginLearningFragment)fm.findFragmentById(R.id.ml_begin_fragment);
        mCharsLearningFragment=(CharsLearningFragment) fm.findFragmentById(R.id.ml_char_learning_fragment);
        mComponentTestFragment=(ComponentTestFragment) fm.findFragmentById(R.id.ml_component_test_fragment);
        mCharsTestFragment=(CharsTestFragment) fm.findFragmentById(R.id.ml_char_test_fragment);

        //4个阶段的fragment都派生自同一个基类，有一个setFinishRunnable()方法，接收一个实现FinishRunnable接口的对象，
        //当fragment结束后会调用这个对象里的方法，通过实现这个方法可以在本activity中转换fragment
        mBeginLearningFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                charsLearning();//转到第二阶段
            }
        });
        mCharsLearningFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                componentTest();//转到第三阶段
            }
        });
        mComponentTestFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                charsTest();//转到第四阶段
            }
        });
        mCharsTestFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                mOrder++;//完成一轮4个阶段以后顺序要+1
                mLearningOrderManager.saveOrder(LearningOrderManager.MAIN_LEARNING,mOrder);//将顺序用顺序管理器保存下来
                beginLearning();//回到第一阶段继续
            }
        });
        //初始化4个fragment
        FragmentTransaction ft=fm.beginTransaction();//获取一个碎片事务
        ft.hide(mBeginLearningFragment);//这个第一阶段可以不隐藏
        ft.hide(mCharsLearningFragment);
        ft.hide(mComponentTestFragment);
        ft.hide(mCharsTestFragment);
        ft.commit();


        //获取上次的学习顺序
        mOrder=mLearningOrderManager.getOrder(LearningOrderManager.MAIN_LEARNING);
        //开始学习
        beginLearning();
    }

    //第一阶段
    private void beginLearning(){
        //创建一个异步对象，从服务器获取指定顺序的部件
        AsyncTask task=new AsyncTask(){
            //doInBackground方法会在子线程中运行
            @Override
            protected Object doInBackground(Object[] objects) {
                mComponentItem=mDataManager.getComponentByOrder(mOrder);//从DataManager实例获取指定顺序的部件对象
                return null;
            }

            //onPostExecute方法在主线程中运行，它的参数是doInBackground的返回值
            @Override
            protected void onPostExecute(Object o) {
                if(mComponentItem!=null){
                    mBeginLearningFragment.setComponent(mComponentItem);//设置第一阶段显示部件的内容
                    FragmentTransaction ft=getFragmentManager().beginTransaction();//开启一个fragment事务
                    ft.hide(mCharsTestFragment);//隐藏第四阶段的fragment
                    ft.show(mBeginLearningFragment);//显示当前阶段的fragment
                    ft.commit();
                }else {
                    //这里将返回的指定顺序的部件不存在判定为到了整张表的尾部，则重置顺序，结束本次学习
                    mOrder=1;//重置顺序为1
                    mLearningOrderManager.saveOrder(LearningOrderManager.MAIN_LEARNING,mOrder);//记录重置后的顺序
                    finish();//结束本次学习
                }
            }
        };
        task.execute();//运行异步对象
    }

    //第二阶段
    private void charsLearning(){
        //由于第一阶段判定了mComponentItem不为NULL，所以这里可以直接使用
        String[] characters=mComponentItem.getCharacters();//获取当前学习部件的例字字形数组
        if(characters!=null){//保证例字数组不为空
            FragmentTransaction ft=getFragmentManager().beginTransaction();//开启一个fragment事务
            ft.hide(mBeginLearningFragment);//隐藏第一阶段的fragment
            ft.show(mCharsLearningFragment);//显示当前阶段的fragment
            ft.commit();
            mCharsLearningFragment.setCharacters(characters);//设置第二阶段显示部件例字的详情
        }
    }

    //第三阶段
    private void componentTest(){
        String id=mComponentItem.getGlobalId();//获取部件的id
        if(!"".equals(id)){//保证部件id存在
            FragmentTransaction ft=getFragmentManager().beginTransaction();//开启一个fragment事务
            ft.hide(mCharsLearningFragment);//隐藏第二阶段的fragment
            ft.show(mComponentTestFragment);//显示当前阶段的fragment
            ft.commit();
            mComponentTestFragment.setTest(mComponentItem);//设置第三阶段测试部件的意思
        }
    }

    //第四阶段
    private void charsTest(){
        String[] characters=mComponentItem.getCharacters();//获取当前学习部件的例字字形数组
        if(characters!=null){//保证例字数组不为空
            FragmentTransaction ft=getFragmentManager().beginTransaction();//开启一个fragment事务
            ft.hide(mComponentTestFragment);//隐藏第三阶段的fragment
            ft.show(mCharsTestFragment);//显示当前阶段的fragment
            ft.commit();
            mCharsTestFragment.setTests(mComponentItem.getCharacters());//设置第四阶段测试部件的例字
        }
    }
}
