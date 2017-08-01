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

/**
 * Created by 10040 on 2016/12/10.
 */

public class ComponentDialog extends DialogFragment {

    //模式,0:shape.1:voice
    private static int sModel;
    //部首
    private static ComponentItem sComponent;

    //部首字形
    private TextView mFigure;
    //部首中文意思
    private TextView mMeaning;
    //部首例字
    private LinearLayout mExampleCharacter;

    static public void startDialog(Activity activity, ComponentItem componentItem, int model){
        sComponent=componentItem;
        sModel=model;
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

            View.OnClickListener exampleListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AsyncTask task=new AsyncTask<Object,Object,CharItem>() {
                        //例字发音
                        private MediaPlayer mMediaPlayer;
                        @Override
                        protected CharItem doInBackground(Object[] params) {
                            DataManager dataManager=DataManager.getInstance(getActivity());
                            CharItem exampleItem=dataManager.getCharItemByShape(Uri.encode((String)params[0]));
                            if(sModel==ComponentItem.VOICE && exampleItem!=null)
                                mMediaPlayer= new SoundGetter(getActivity()).getMediaPlayer(exampleItem);
                            return exampleItem;
                        }

                        @Override
                        protected void onPostExecute(CharItem exampleItem) {
                            if(exampleItem==null){
                                Toast.makeText(getActivity(),"This character hasn't been added",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if(sModel==ComponentItem.SHAPE)
                                ExampleActivity.startActivity(getActivity(), exampleItem);
                            else {
                                MediaManager mm=MediaManager.getInstance();
                                mm.setMediaPlayer(mMediaPlayer);
                                mm.startMediaPlayer();
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
