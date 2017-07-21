package ecnu.chinesecharactercottage.activitys.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.ChoseItem;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.modelsForeground.testFragments.*;

/**
 * Created by 10040 on 2017/2/27.
 */

public class TestChoseActivity extends Activity {
    //听力
    @InjectView(id=R.id.test_listen)
    private ChoseItem mListen;
    //阅读
    @InjectView(id=R.id.test_read)
    private ChoseItem mRead;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,TestChoseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chose);
        Injecter.autoInjectAllField(this);

        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestChoseListenDialog(TestChoseActivity.this).show();
            }
        });

        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestChoseReadDialog(TestChoseActivity.this).show();

            }
        });

    }
}
