package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ecnu.chinesecharactercottage.R;

import static java.lang.Thread.sleep;

/**
 * Created by 10040 on 2017/1/23.
 */

public class BeginActivity extends Activity {
    //显示时间
    private long mShowTime=100;

    @Override
    protected void onCreate(Bundle savadInstanceState){
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_begin);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(mShowTime);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Intent intent=new Intent(BeginActivity.this,CCCMainActivity.class);
                startActivity(intent);
                finish();
            }
        },mShowTime);


    }
}
