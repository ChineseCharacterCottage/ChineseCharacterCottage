package ecnu.chinesecharactercottage.modelsForeground;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/4.
 */

public abstract class TwoChoicesDialog extends DialogFragment {

    private Button mButton1;
    private Button mButton2;
    private Activity mActivity;
    private String mTag;
    private String mText1;
    private String mText2;

    protected TwoChoicesDialog(Activity activity,String tag, String text1, String text2){
        super();
        mActivity=activity;
        mTag=tag;
        mText1=text1;
        mText2=text2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.choseDialog);
    }

    public void show() {
        FragmentManager fm=mActivity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(mTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        super.show(ft, mTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_two_choice,container,false);

        mButton1=(Button)v.findViewById(R.id.button_1);
        mButton2=(Button)v.findViewById(R.id.button_2);
        mButton1.setText(mText1);
        mButton2.setText(mText2);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFirst();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSecond();
            }
        });
        return v;
    }

    abstract protected void clickFirst();
    abstract protected void clickSecond();

}
