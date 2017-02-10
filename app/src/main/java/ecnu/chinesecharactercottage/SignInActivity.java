package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 10040 on 2017/1/23.
 */

public class SignInActivity extends Activity {

    private EditText mAccount;
    private EditText mPassword;
    private Button mSignIn;
    private Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signIn(mAccount.getText().toString(),mPassword.getText().toString())){

                }else
                    Toast.makeText(SignInActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                intent.putExtra("account",mAccount.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void init(){
        mAccount=(EditText)findViewById(R.id.account_email);
        mPassword=(EditText)findViewById(R.id.password);
        mSignIn=(Button)findViewById(R.id.button_sign_in);
        mSignUp=(Button)findViewById(R.id.button_sign_up);
    }

    private boolean signIn(String account,String password){
        return false;
    }
}
