package ecnu.chinesecharactercottage.activitys.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/27.
 */

public class TestChoseActivity extends Activity {
    //用于设置按键到屏幕中心
    private LinearLayout mButtons;
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
        setButtons();

        mTOF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestTOFActivity.startActivity(TestChoseActivity.this,1,10);
            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestCompleteActivity.startActivity(TestChoseActivity.this,1,10);
            }
        });

        mListenTOF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestHearTOFActivity.startActivity(TestChoseActivity.this,1,10);
            }
        });

        mListenMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestHearMatchActivity.startActivity(TestChoseActivity.this,1,10);
            }
        });

    }

    private void init(){
        mListenMatch=(Button)findViewById(R.id.bt_hear_match);
        mListenTOF=(Button)findViewById(R.id.bt_hear_tof);
        mComplete=(Button)findViewById(R.id.bt_complete);
        mTOF=(Button)findViewById(R.id.bt_tof);
        mButtons=(LinearLayout)findViewById(R.id.homepageButtons);
    }

    private void setButtons(){
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth=wm.getDefaultDisplay().getWidth();
        int screenHeight=wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) mButtons.getLayoutParams();
        int left=(int)(screenWidth/4.9);
        int top=(int)(screenHeight/3.05);
        linearParams.setMargins(left,top,0,0);
        linearParams.height=(int)(screenHeight/2.15);
        linearParams.width=(int)(screenWidth/1.648);
        mButtons.setLayoutParams(linearParams);
    }
}
