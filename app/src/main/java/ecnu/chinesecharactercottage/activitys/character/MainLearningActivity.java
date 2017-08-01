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
 * Created by 10040 on 2017/7/26.
 */

public class MainLearningActivity extends Activity {
    //临时数据键值
    static final String ORDER_KEY="main_learning";

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
        FragmentManager fm=getFragmentManager();
        mBeginLearningFragment=(BeginLearningFragment)fm.findFragmentById(R.id.ml_begin_fragment);
        mCharsLearningFragment=(CharsLearningFragment) fm.findFragmentById(R.id.ml_char_learning_fragment);
        mComponentTestFragment=(ComponentTestFragment) fm.findFragmentById(R.id.ml_component_test_fragment);
        mCharsTestFragment=(CharsTestFragment) fm.findFragmentById(R.id.ml_char_test_fragment);
        mBeginLearningFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                charsLearning();
            }
        });
        mCharsLearningFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                componentTest();
            }
        });
        mComponentTestFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                charsTest();
            }
        });
        mCharsTestFragment.setFinishRunnable(new BaseFragment.FinishRunnable() {
            @Override
            public void onFinish() {
                mOrder++;
                mLearningOrderManager.saveOrder(ORDER_KEY,mOrder);
                beginLearning();
            }
        });
        FragmentTransaction ft=fm.beginTransaction();
        ft.hide(mBeginLearningFragment);
        ft.hide(mCharsLearningFragment);
        ft.hide(mComponentTestFragment);
        ft.hide(mCharsTestFragment);
        ft.commit();


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
                    mBeginLearningFragment.setComponent(mComponentItem);
                    FragmentTransaction ft=getFragmentManager().beginTransaction();
                    ft.hide(mCharsTestFragment);
                    ft.show(mBeginLearningFragment);
                    ft.commit();
                }else {
                    mOrder=1;
                    mLearningOrderManager.saveOrder(ORDER_KEY,mOrder);
                    finish();
                }
            }
        };
        task.execute();
    }

    private void charsLearning(){
        String[] characters=mComponentItem.getCharacters();
        if(characters!=null){
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.hide(mBeginLearningFragment);
            ft.show(mCharsLearningFragment);
            ft.commit();
            mCharsLearningFragment.setCharacters(characters);
        }
    }

    private void componentTest(){
        String id=mComponentItem.getGlobalId();
        if(!"".equals(id)){
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.hide(mCharsLearningFragment);
            ft.show(mComponentTestFragment);
            ft.commit();
            mComponentTestFragment.setTest(id);
        }
    }

    private void charsTest(){
        String[] characters=mComponentItem.getCharacters();
        if(characters!=null){
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            ft.hide(mComponentTestFragment);
            ft.show(mCharsTestFragment);
            ft.commit();
            mCharsTestFragment.setTests(mComponentItem.getCharacters());
        }
    }
}
