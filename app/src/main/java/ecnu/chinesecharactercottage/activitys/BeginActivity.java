package ecnu.chinesecharactercottage.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.testactivity.TestActivity;

import static java.lang.Thread.sleep;

/**
 * Created by 10040 on 2017/1/23.
 */

public class BeginActivity extends Activity {
    //显示时间
    final private static long SHOWTIME=1500;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(SHOWTIME);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                TestActivity.startActivity(BeginActivity.this);
                finish();
            }
        },SHOWTIME);


    }
}
