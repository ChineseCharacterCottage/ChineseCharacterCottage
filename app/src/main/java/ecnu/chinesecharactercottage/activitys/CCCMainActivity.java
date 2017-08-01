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

public class CCCMainActivity extends Activity {
    //四个核心功能
    @InjectView(id=R.id.character_learning)
    private ChoseItem mCharacterLeaning;
    @InjectView(id=R.id.test)
    private ChoseItem mTest;
    @InjectView(id=R.id.review)
    private ChoseItem mReview;
    @InjectView(id=R.id.knowledge)
    private ChoseItem mKnowledge;
    @InjectView(id=R.id.main_learning)
    private ChoseItem mMainLearning;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,CCCMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccc_main);
        Injecter.autoInjectAllField(this);

        //设置四个功能按键
        setCharacterLeaning();
        setTest();
        setReview();
        setKnowledge();
        setMainLearning();
    }

    private void setCharacterLeaning(){
        mCharacterLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharacterLearningActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    private  void setTest(){
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestChoseActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    private void setReview(){
        mReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionChoseActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    private void setKnowledge(){
        mKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TitleListActivity.startActivity(CCCMainActivity.this);
            }
        });
    }

    private void setMainLearning(){
        mMainLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainLearningActivity.startActivity(CCCMainActivity.this);
            }
        });
    }
}