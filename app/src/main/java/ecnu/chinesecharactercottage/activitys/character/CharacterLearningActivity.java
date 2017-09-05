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
import ecnu.chinesecharactercottage.modelsForeground.LearningOrderManager;
import ecnu.chinesecharactercottage.modelsForeground.TwoChoicesDialog;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 这个类是汉字学习模块的主界面，在这里选择进入象形字学习还是进入形声字学习
 */
public class CharacterLearningActivity extends Activity {
    //象形字学习按键
    @InjectView(id=R.id.pictogram_learning)
    private ChoseItem mPhonogram;
    //形声字学习按键
    @InjectView(id=R.id.phonogram_learning)
    private ChoseItem mPictogram;

    /**
     * 静态活动跳转方法
     * @param context 需要跳转的活动上下文
     */
    public static void startActivity(Context context){
        Intent intent=new Intent(context,CharacterLearningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_learning);//设置布局
        Injecter.autoInjectAllField(this);//利用注释类动态绑定布局中的控件,详细请查看modesForeground.inject包中的两个类

        //进入象形字学习
        mPictogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ids=new String[3];//需要学习的id列表

                LearningOrderManager orderManager=LearningOrderManager.getManager(CharacterLearningActivity.this);//获取顺序管理器实例
                int record=orderManager.getOrder(LearningOrderManager.PICTOGRAM_LEARNING);//获取顺序
                //构造id列表
                for (int i=0;i<ids.length;i++)
                    ids[i]=String.valueOf(i+record);
                //跳转到象形字学习，并传入id列表
                PictogramActivity.startActivity(CharacterLearningActivity.this,ids,PictogramActivity.LEARNING);
            }
        });

        //进入形声字学习
        mPhonogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到形声字学习
                new ChoseComponentDialog(CharacterLearningActivity.this).show();
            }
        });
    }
}
