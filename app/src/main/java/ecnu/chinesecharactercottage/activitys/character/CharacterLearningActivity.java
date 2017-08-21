package ecnu.chinesecharactercottage.activitys.character;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.ChoseComponentDialog;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.ChoseItem;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2017/2/26.
 */

public class CharacterLearningActivity extends Activity {
    //象形字学习按键
    @InjectView(id=R.id.pictogram_learning)
    private ChoseItem mPhonogram;
    //形声字学习按键
    @InjectView(id=R.id.phonogram_learning)
    private ChoseItem mPictogram;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,CharacterLearningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_learning);
        Injecter.autoInjectAllField(this);

        //进入象形字学习
        mPictogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ids=new String[20];
                for (int i=0;i<ids.length;i++)
                    ids[i]=String.valueOf(i+1);
                PictogramActivity.startActivity(CharacterLearningActivity.this,ids,PictogramActivity.LEARNING);
            }
        });

        //进入形声字学习
        mPhonogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChoseComponentDialog(CharacterLearningActivity.this).show();
            }
        });
    }
}
