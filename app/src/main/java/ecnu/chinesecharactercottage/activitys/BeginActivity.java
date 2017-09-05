package ecnu.chinesecharactercottage.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.testactivity.TestActivity;

import static java.lang.Thread.sleep;

/**
 * @author 胡家斌
 *这个类是启动类，唯一的任务就是显示启动页面，一定时间延迟后跳转到TestActivity类。
 * @see TestActivity
 */
public class BeginActivity extends Activity {
    //显示时间
    final private static long SHOWTIME=1500;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);//设置布局

        //下面调用异步计时器方法，等待指定时间后跳转到TestActivity类
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TestActivity.startActivity(BeginActivity.this);//调用TestActivity类提供的跳转静态方法
                finish();//显示完成后本活动没有任何作用，直接结束掉
            }
        },SHOWTIME);

    }
}
