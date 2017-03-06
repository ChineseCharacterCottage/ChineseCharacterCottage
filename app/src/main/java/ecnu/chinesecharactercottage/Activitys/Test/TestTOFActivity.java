package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import ecnu.chinesecharactercottage.ModelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.ModelsForeground.TestTOFFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestTOFActivity extends Activity {

    //题目页面
    private TestTOFFragment mTestFragment;
    //题目列表
    private TestTOFItem[] mTestTOFItems;

    static public void startActivity(Context context,int startId,int len){
        if(len<=0)
            return;
        String[] mIds=new String[len];
        for(int i=0;i<len;i++)
            mIds[i]=String.valueOf(startId+i);

        Intent intent=new Intent(context,TestTOFActivity.class);
        intent.putExtra()
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tof);
        init();


    }

    private void init(){
        mTestFragment=(TestTOFFragment)getFragmentManager().findFragmentById(R.id.test_tof_fragment);
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        };

        NextRunnable nextRunnable=new NextRunnable() {

            @Override
            public void next() {

            }
        }
        //TestTOFItem初始化

    }
}
