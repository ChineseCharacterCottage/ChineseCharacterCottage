package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by 10040 on 2016/12/8.
 */

public class ExampleActivity extends Activity {

    private static CharItem sCharItem;
    private CharacterFragment mCharacterFragment;
    private Button mButton;

    public static void startActivity(Context context,CharItem charItem){
        sCharItem=charItem;
        Intent intent=new Intent(context,ExampleActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_char);

        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.example_fragment);
        mButton=(Button)findViewById(R.id.button_return);
        setCharacter();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setCharacter(){
        mCharacterFragment.setCharacter(sCharItem);
    }
}
