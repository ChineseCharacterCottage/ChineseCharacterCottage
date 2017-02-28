package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/27.
 */

public class TestChoseActivity extends Activity {
    //听力选择
    private Button mListenMatch;
    //听力判断
    private Button mListenTOF;
    //填空
    private Button mComplete;
    //判断
    private Button mTOF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chose);
        init();


    }

    private void init(){
        mListenMatch=(Button)findViewById(R.id.bt_hear_match);
        mListenTOF=(Button)findViewById(R.id.bt_hear_tof);
        mComplete=(Button)findViewById(R.id.bt_complete);
        mTOF=(Button)findViewById(R.id.bt_tof);
    }
}
