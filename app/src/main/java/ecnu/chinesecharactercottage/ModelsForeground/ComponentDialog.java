package ecnu.chinesecharactercottage.ModelsForeground;

import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.Activitys.ExampleActivity;
import ecnu.chinesecharactercottage.ModelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/12/10.
 */

public class ComponentDialog extends DialogFragment {

    //模式
    private static int sModel;
    //部首
    private static ComponentItem sComponent;

    //部首字形
    private TextView mFigure;
    //部首中文意思
    private TextView mMeaning;
    //部首例字
    private LinearLayout mExampleCharacter;

    static public ComponentDialog getDialogInstance(ComponentItem componentItem,int model){
        sComponent=componentItem;
        sModel=model;
        ComponentDialog myComponentDialog=new ComponentDialog();
        return myComponentDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_component,container,false);
        mFigure=(TextView)v.findViewById(R.id.component_figure);
        mMeaning=(TextView)v.findViewById(R.id.component_meaning);
        mExampleCharacter=(LinearLayout) v.findViewById(R.id.component_example);

        setRadical();
        
        return v;
    }

    private void setRadical() {
        mFigure.setText(sComponent.getShape());
        mMeaning.setText(sComponent.getExplanation());

        String[] examples = sComponent.getCharacters();
        int exampleNumber = examples.length;

        LinearLayout linearLayout;
        linearLayout = new LinearLayout(getActivity());//为了解决报错

        TextView aExample;
        for (int i = 0; i < exampleNumber; i++) {
            if (i % 5 == 0) {
                linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                mExampleCharacter.addView(linearLayout);
            }
            aExample = new TextView(getActivity());
            aExample.setPadding(10,0,10,0);
            aExample.setTextSize(20);
            aExample.setText(examples[i]);
            aExample.setTextColor(getResources().getColor(R.color.colorBlack));

            //final CharItem exampleItem;
            //获取对应例字
            //接口未完成，这里先这样
            //exampleItem = charItemLab.findCharItemsByShape(examples[i])[0];
            View.OnClickListener exampleListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AsyncTask task=new AsyncTask<String,Object,CharItem>() {
                        @Override
                        protected CharItem doInBackground(String[] params) {
                            DataManager dataManager=DataManager.getInstance(getActivity());
                            return dataManager.getCharItemByShape(params[0]);
                        }

                        @Override
                        protected void onPostExecute(CharItem exampleItem) {
                            if(sModel==0)
                                ExampleActivity.startActivity(getActivity(), exampleItem);
                            else {
                                MediaPlayer mediaPlayer=exampleItem.getMediaPlayer(getActivity());
                                try{
                                    mediaPlayer.prepare();
                                }
                                catch (Exception e){
                                    Log.d("PronunciationMedia:",e.toString());
                                    e.printStackTrace();
                                }
                                mediaPlayer.start();
                            }
                        }
                    };
                    task.execute(((TextView)view).getText().toString());
                }
            };
            aExample.setOnClickListener(exampleListener);
            linearLayout.addView(aExample);
        }
    }
}
