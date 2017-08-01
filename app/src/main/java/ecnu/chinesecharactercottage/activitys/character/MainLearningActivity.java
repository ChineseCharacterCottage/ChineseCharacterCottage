package ecnu.chinesecharactercottage.activitys.character;

import android.app.Activity;
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
 * Created by 10040 on 2017/7/26.
 */

public class MainLearningActivity extends Activity {
    //临时数据键值
    static final String ORDER_KEY="main_learning";

    //顺序存储管理器
    LearningOrderManager mLearningOrderManager;
    //数据管理器
    DataManager mDataManager;

    //当前学习编号
    int mOrder;
    //当前学习部件
    ComponentItem mComponentItem;


    static public void startActivity(Context context){
        Intent intent=new Intent(context,MainLearningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_learning);

        mLearningOrderManager=LearningOrderManager.getManager(this);
        mDataManager=DataManager.getInstance(this);


        //获取上次的学习顺序
        mOrder=mLearningOrderManager.getOrder(ORDER_KEY);
        //开始学习
        beginLearning();
    }

    private void beginLearning(){
        AsyncTask task=new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                mComponentItem=mDataManager.getComponentByOrder(mOrder);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(mComponentItem!=null){
                    BeginLearningFragment beginLearningFragment=BeginLearningFragment.getFragment(new BaseFragment.FinishRunnable() {
                        @Override
                        public void onFinish() {
                            charsLearning();
                        }
                    }, mComponentItem);
                    FragmentTransaction ft=getFragmentManager().beginTransaction();
                    ft.replace(R.id.learning_fragment,beginLearningFragment);
                    ft.commit();
                }else {
                    mOrder=1;
                }
                mLearningOrderManager.saveOrder(ORDER_KEY,mOrder);
            }
        };
        task.execute();
    }

    private void charsLearning(){
        String[] characters=mComponentItem.getCharacters();
        if(characters!=null){
            CharsLearningFragment charsLearningFragment=CharsLearningFragment.getFragment(new BaseFragment.FinishRunnable() {
                @Override
                public void onFinish() {
                    componentTest();
                }
            },characters);
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.replace(R.id.learning_fragment,charsLearningFragment);
            ft.commit();
        }
    }

    private void componentTest(){
        Toast.makeText(this,"部件测试",Toast.LENGTH_SHORT).show();

        String id=mComponentItem.getGlobalId();
        if(!"".equals(id)){
            ComponentTestFragment componentTestFragment=ComponentTestFragment.getFragment(new BaseFragment.FinishRunnable() {
                @Override
                public void onFinish() {
                    mOrder++;
                    charsTest();
                }
            },id);
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.replace(R.id.learning_fragment,componentTestFragment);
            ft.commit();
        }
    }

    private void charsTest(){
        Toast.makeText(this,"例字测试",Toast.LENGTH_SHORT).show();

        String[] characters=mComponentItem.getCharacters();
        if(characters!=null){
            CharsTestFragment charsTestFragment=CharsTestFragment.getFragment(new BaseFragment.FinishRunnable() {
                @Override
                public void onFinish() {
                    mOrder++;
                    beginLearning();
                }
            },characters);
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.replace(R.id.learning_fragment,charsTestFragment);
            ft.commit();
        }

    }


}
