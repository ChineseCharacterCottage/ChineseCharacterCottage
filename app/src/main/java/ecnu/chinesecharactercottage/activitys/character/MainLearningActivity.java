package ecnu.chinesecharactercottage.activitys.character;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments.*;

/**
 * Created by 10040 on 2017/7/26.
 */

public class MainLearningActivity extends Activity {
    Fragment mLearningFragment;
    FragmentTransaction mFT;

    static public void startActivity(Context context){
        Intent intent=new Intent(context,MainLearningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_learning);

        mLearningFragment=getFragmentManager().findFragmentById(R.id.learning_fragment);
        mFT=mLearningFragment.getFragmentManager().beginTransaction();
        beginLearning();
    }

    private void beginLearning(){
        BeginLearningFragment beginLearningFragment=
    }


}
