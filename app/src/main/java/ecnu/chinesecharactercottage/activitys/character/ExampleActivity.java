package ecnu.chinesecharactercottage.activitys.character;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.R;

/**
 * @author 胡家斌
 * 这个活动是用来显示例字的
 */

public class ExampleActivity extends Activity {
    //要显示的字的数据
    private static CharItem sCharItem;
    //显示字的Fragment
    private CharacterFragment mCharacterFragment;
    //返回按键
    private Button mButton;

    /**
     * 静态活动跳转方法，需要传入上下文和字数据
     * @param context 上下文
     * @param charItem 字数据
     */
    public static void startActivity(Context context,CharItem charItem){
        sCharItem=charItem;//将字数据存到静态成员中
        Intent intent=new Intent(context,ExampleActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_char);//设置布局

        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.example_fragment);//绑定碎片
        mButton=(Button)findViewById(R.id.button_return);//绑定按键
        mCharacterFragment.setCharacter(sCharItem);//设置Fragment要显示的字的数据
        //设置按键监听器
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
