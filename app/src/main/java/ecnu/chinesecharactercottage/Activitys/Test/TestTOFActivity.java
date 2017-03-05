package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestTOFActivity extends Activity {

    //字形
    TextView mCharacter;
    //图片
    ImageView mPicture;
    //选择的答案
    RadioGroup mChosedAnswer;
    //确定按键
    Button mSubmit;
    //题目
    TestTOFItem mCorrectAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tof);
        init();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void init(){
        mCharacter=(TextView)findViewById(R.id.tv_character);
        mPicture=(ImageView)findViewById(R.id.iv_picture);
        mChosedAnswer=(RadioGroup)findViewById(R.id.answer_chose);
        mSubmit=(Button)findViewById(R.id.bt_submint);
        //TestTOFItem初始化

    }
}
