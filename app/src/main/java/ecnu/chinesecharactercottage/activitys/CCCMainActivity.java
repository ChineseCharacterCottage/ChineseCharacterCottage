package ecnu.chinesecharactercottage.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ecnu.chinesecharactercottage.activitys.character.CharacterLearningActivity;
import ecnu.chinesecharactercottage.activitys.character.MainLearningActivity;
import ecnu.chinesecharactercottage.activitys.collection.CollectionChoseActivity;
import ecnu.chinesecharactercottage.activitys.knowledge.TitleListActivity;
import ecnu.chinesecharactercottage.activitys.test.TestChoseActivity;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.ChoseItem;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 主界面类，负责显示各大功能，为各按键设置点击监听器，跳转到对应类。
 * 按键使用的自定义部件ChoseItem
 * @see ChoseItem
 */
public class CCCMainActivity extends Activity {
    //核心功能按键
    //这里用到了相关的注释类，详细请查看modesForeground.inject包中的两个类
    //汉字学习按键
    @InjectView(id=R.id.character_learning)
    private ChoseItem mCharacterLeaning;
    //测试按键
    @InjectView(id=R.id.test)
    private ChoseItem mTest;
    //复习按键
    @InjectView(id=R.id.review)
    private ChoseItem mReview;
    //小知识按键
    @InjectView(id=R.id.knowledge)
    private ChoseItem mKnowledge;
    //主学习按键
    @InjectView(id=R.id.main_learning)
    private ChoseItem mMainLearning;

    /**
     * 静态活动跳转方法
     * @param context 需要跳转的活动上下文
     */
    public static void startActivity(Context context){
        Intent intent=new Intent(context,CCCMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccc_main);//设置布局文件
        Injecter.autoInjectAllField(this);//利用注释类动态绑定布局中的控件,详细请查看modesForeground.inject包中的两个类

        //设置功能按键监听器
        setCharacterLeaning();
        setTest();
        setReview();
        setKnowledge();
        setMainLearning();
    }

    //设置监听器，点击跳转到汉字学习模块
    private void setCharacterLeaning(){
        mCharacterLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharacterLearningActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    //设置监听器，点击跳转到测试模块
    private  void setTest(){
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestChoseActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    //设置监听器，点击跳转到复习模块
    private void setReview(){
        mReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionChoseActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    //设置监听器，点击跳转到小知识模块
    private void setKnowledge(){
        mKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TitleListActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    //设置监听器，点击跳转到主学习模块
    private void setMainLearning(){
        mMainLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainLearningActivity.startActivity(CCCMainActivity.this);
            }
        });
    }
}