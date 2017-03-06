package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    public static void startActivity(Context context){
        Intent intent=new Intent(context,TestChoseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chose);
        init();
        mTOF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestTOFActivity.startActivity(TestChoseActivity.this,1,10);
            }
        });

    }

    private void init(){
        mListenMatch=(Button)findViewById(R.id.bt_hear_match);
        mListenTOF=(Button)findViewById(R.id.bt_hear_tof);
        mComplete=(Button)findViewById(R.id.bt_complete);
        mTOF=(Button)findViewById(R.id.bt_tof);
    }
}
