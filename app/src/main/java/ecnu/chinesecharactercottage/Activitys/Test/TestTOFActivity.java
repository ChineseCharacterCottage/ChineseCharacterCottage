package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.TestTOFItem;
import ecnu.chinesecharactercottage.ModelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.ModelsForeground.TestTOFFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestTOFActivity extends Activity {

    //题目页面
    private TestTOFFragment mTestFragment;
    //id列表
    private String[] mIds;
    //当前题目序号
    private int mNowIndex;
    //题目列表
    private TestTOFItem[] mTestTOFItems;

    static public void startActivity(Context context,int startId,int len){
        if(len<=0)
            return;
        String[] ids=new String[len];
        for(int i=0;i<len;i++)
            ids[i]=String.valueOf(startId+i);

        Intent intent=new Intent(context,TestTOFActivity.class);
        intent.putExtra("ids",ids);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tof);
        init();

        //后台读取testTOFItem列表，完成后设置next()函数
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(TestTOFActivity.this);
                mTestTOFItems=new TestTOFItem[mIds.length];
                for(int i=0;i<mIds.length;i++){
                    mTestTOFItems[i]=(TestTOFItem)dataManager.getTestItemById(mIds[i],DataManager.TOF);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mTestFragment.setNext(new NextRunnable() {
                    @Override
                    public void next() {
                        if(mNowIndex<=mTestTOFItems.length) {
                            mTestFragment.setTest(mTestTOFItems[mNowIndex]);
                            mNowIndex++;
                        }else
                            finishTest();
                    }
                });
            }
        };
        task.execute();
    }

    private void init(){
        mTestFragment=(TestTOFFragment)getFragmentManager().findFragmentById(R.id.test_tof_fragment);
        mIds=getIntent().getStringArrayExtra("ids");
        mNowIndex=0;
    }

    private void finishTest(){
        finish();
    }

}
