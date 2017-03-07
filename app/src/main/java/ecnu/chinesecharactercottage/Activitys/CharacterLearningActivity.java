package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ecnu.chinesecharactercottage.ModelsForeground.ChoseComponentDialog;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/26.
 */

public class CharacterLearningActivity extends Activity {
    //象形字学习按键
    private Button mPhonogram;
    //形声字学习按键
    private Button mPictogram;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,CharacterLearningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_learning);
        init();

        //进入象形字学习
        mPhonogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂时用hsk学习界面充当.
                Toast.makeText(CharacterLearningActivity.this,"Phonogram Learning Model is being desiged",Toast.LENGTH_SHORT).show();
            }
        });

        //进入形声字学习
        mPictogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("chose_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                ChoseComponentDialog myChoseComponentDialog= ChoseComponentDialog.getDialogInstance();
                myChoseComponentDialog.show(ft,"chose_dialog");
            }
        });
    }

    private void init(){
        mPhonogram=(Button)findViewById(R.id.phonogram_learning);
        mPictogram=(Button)findViewById(R.id.pictogram_learning);
    }
}
