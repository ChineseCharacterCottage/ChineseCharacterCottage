package ecnu.chinesecharactercottage.modelsForeground;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ecnu.chinesecharactercottage.modelsBackground.SoundGetter;
import ecnu.chinesecharactercottage.activitys.character.ExampleActivity;
import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 这个fragment是个对话框，用来显示部件（偏旁）的详情。分为形旁和声旁两种模式。
 */

public class ComponentDialog extends DialogFragment {
    //通过静态变量来传递数据不是很好，以后应当把这个传递功能封装起来再用，应该在对象销毁后设置为null
    //部件数据
    private static ComponentItem sComponent;

    //模式根据部件来判断，这只是为了方便保存的一个变量
    private int mModel;
    //部首字形
    @InjectView(id=R.id.component_figure)
    private TextView mFigure;
    //部首中文意思
    @InjectView(id=R.id.component_meaning)
    private TextView mMeaning;
    //部首例字
    @InjectView(id=R.id.component_example)
    private LinearLayout mExampleCharacter;

    /**
     * 显示对话框，展示偏旁的详情，并根据模式实现不同的点击效果
     * @param activity 调用的活动
     * @param componentItem 传入的数据
     */
    static public void startDialog(Activity activity, ComponentItem componentItem){
        sComponent=componentItem;
        ComponentDialog myComponentDialog=new ComponentDialog();

        FragmentManager fm=activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("component_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        myComponentDialog.show(ft,"component_dialog");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialog);//设置对话框样式（背景透明）
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_component,container,false);
        Injecter.autoInjectAllField(this,v);//绑定控件
        mModel=sComponent.getModel();//获取模式，只是为了方便才保存这个的 -o-

        seComponent();//设置部首相关的内容
        return v;
    }


    private void seComponent() {
        mFigure.setText(sComponent.getShape());//设置部件字形
        mMeaning.setText(sComponent.getExplanation());//设置部件意思

        String[] examples = sComponent.getCharacters();//获取部件的例字
        int exampleNumber = examples.length;//获取例字的数量

        LinearLayout linearLayout;//创建一个布局，让5个字一行
        linearLayout = new LinearLayout(getActivity());//为了解决报错

        TextView aExample;
        //创建单个字的点击监听器
        View.OnClickListener exampleListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建异步对象，获取相应数据，并根据模式选择查看字详情或者发音
                AsyncTask task=new AsyncTask<Object,Object,CharItem>() {
                    //例字发音
                    private MediaPlayer mMediaPlayer;
                    @Override
                    protected CharItem doInBackground(Object[] params) {
                        DataManager dataManager=DataManager.getInstance(getActivity());//获取DataManager实例
                        CharItem exampleItem=dataManager.getCharItemByShape((String)params[0]);//获取例字
                        if(mModel==ComponentItem.VOICE && exampleItem!=null)
                            mMediaPlayer= new SoundGetter(getActivity()).getMediaPlayer(exampleItem);//获取字发音
                        return exampleItem;
                    }

                    @Override
                    protected void onPostExecute(CharItem exampleItem) {
                        if(exampleItem==null){//如果字不存在则提示
                            Toast.makeText(getActivity(),"This character hasn't been added",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(mModel==ComponentItem.SHAPE)//形旁模式
                            ExampleActivity.startActivity(getActivity(), exampleItem);//跳转到例字显示活动
                        else {//声旁模式
                            MediaManager mm=MediaManager.getInstance();//获取音频管理器实例
                            mm.setMediaPlayer(mMediaPlayer);//设置当前播放音频
                            mm.startMediaPlayer();//播放当前音频
                        }
                    }
                };
                task.execute(((TextView)view).getText().toString());//执行异步对象，并且传入例字字形
            }
        };

        for (int i = 0; i < exampleNumber; i++) {//遍历例字数组
            if (i % 5 == 0) {//5个字一行
                linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);//设置为水平排列
                mExampleCharacter.addView(linearLayout);//将新建的布局加入例字布局中
            }
            aExample = new TextView(getActivity());//新建一个例字的单个例字显示控件
            aExample.setPadding(10,0,10,0);//设置控件的内边距
            aExample.setTextSize(20);//设置例字的字体大小
            aExample.setText(examples[i]);//设置例字字形
            aExample.setTextColor(getResources().getColor(R.color.colorBlack));//设置字体颜色
            aExample.setOnClickListener(exampleListener);//设置点击监听器

            linearLayout.addView(aExample);//把单个例字控件加入例字控件中
        }
    }
}
