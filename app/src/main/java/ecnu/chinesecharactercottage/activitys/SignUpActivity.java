package ecnu.chinesecharactercottage.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/1/25.
 */

public class SignUpActivity extends Activity {

    private EditText mAccount;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private RadioGroup mGenderChose;
    private RadioGroup mIdentityChose;
    private Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signUp());
                else
                    Toast.makeText(SignUpActivity.this,"注册失败",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void init(){
        mAccount=(EditText)findViewById(R.id.account_email);
        mPassword=(EditText)findViewById(R.id.password);
        mConfirmPassword=(EditText)findViewById(R.id.confirm_password);
        mGenderChose=(RadioGroup)findViewById(R.id.gender_chose);
        mIdentityChose=(RadioGroup)findViewById(R.id.identity_chose);
        mSignUp=(Button)findViewById(R.id.button_sign_up);

        Intent intent=getIntent();
        mAccount.setText(intent.getStringExtra("account"));
    }

    private boolean signUp(){
        return false;
    }
}
