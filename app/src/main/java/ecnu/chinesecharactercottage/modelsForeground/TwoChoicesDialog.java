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
 * 一个抽象类，实现了一个双选对话框，样式是已经定义好的。可以通过继承定义两个选项的文字内容，并实现两个选项的点击效果
 */

public abstract class TwoChoicesDialog extends DialogFragment {

    //选项1
    private Button mButton1;
    //选项2
    private Button mButton2;
    //启动对话框的活动
    private Activity mActivity;
    //对话框的标识
    private String mTag;
    //选项文字1
    private String mText1;
    //选项文字2
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
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.choseDialog);//设置样式
    }

    /**
     * 显示对话框
     */
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
        View v=inflater.inflate(R.layout.dialog_two_choice,container,false);//渲染视图

        //绑定控件
        mButton1=(Button)v.findViewById(R.id.button_1);
        mButton2=(Button)v.findViewById(R.id.button_2);
        //设置选项文字内容
        mButton1.setText(mText1);
        mButton2.setText(mText2);
        //设置选项点击效果
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

    //点击第一个按键的效果
    abstract protected void clickFirst();
    //点击第二个按键的效果
    abstract protected void clickSecond();

}
