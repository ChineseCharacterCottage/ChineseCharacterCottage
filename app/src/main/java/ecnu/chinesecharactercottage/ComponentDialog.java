package ecnu.chinesecharactercottage;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by 10040 on 2016/12/10.
 */

public class ComponentDialog extends DialogFragment {

    //部首
    private static RadicalItem sComponent;

    //部首字形
    private TextView mFigure;
    //部首图片
    private ImageView mImage;
    //部首中文名
    private TextView mChineseName;
    //部首英文名
    private TextView mEnglishName;
    //部首中文意思
    private TextView mMeaning;
    //形近部首
    private TextView mSimilarity;
    //部首例字
    private LinearLayout mExampleCharacter;

    static public ComponentDialog getDialogInstance(RadicalItem componentItem){
        sComponent=componentItem;
        ComponentDialog myComponentDialog=new ComponentDialog();
        return myComponentDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_component_item,container,false);
        mFigure=(TextView)v.findViewById(R.id.component_figure);
        mImage=(ImageView)v.findViewById(R.id.component_image);
        mChineseName=(TextView)v.findViewById(R.id.component_chinese_name);
        mEnglishName=(TextView)v.findViewById(R.id.component_english_name);
        mMeaning=(TextView)v.findViewById(R.id.component_meaning);
        mSimilarity=(TextView)v.findViewById(R.id.component_similarity);
        mExampleCharacter=(LinearLayout) v.findViewById(R.id.component_example);

        setRadical();
        
        return v;
    }

    private void setRadical() {
        mFigure.setText(sComponent.getRadical());
        //mImage.setImageBitmap(sRadical.getImage());
        mEnglishName.setText(sComponent.getName());
        //mMeaning.setText(sRadical.getMeaning));

        String[] examples = sComponent.getExamples();
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
            aExample.setText(examples[i]+"   ");
            aExample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharItem exampleItem;
                    CharItemLab charItemLab;
                    //获取对应例字
                    try {
                        charItemLab = CharItemLab.getLabWithoutContext();
                    } catch (Exception e) {
                        Log.d("getLab IOException", e.toString());
                        getActivity().finish();
                        return;
                    }
                    if (charItemLab == null) {
                        Log.d("charItemLab", "is null");
                    }
                    try {
                        exampleItem = charItemLab.findCharItemsByShape(((TextView) view).getText().toString())[0];
                        ExampleActivity.startActivity(getActivity(), exampleItem);
                    } catch (Exception e) {
                        Log.d("findCharItemsByShape", e.toString());
                        e.printStackTrace();
                    }
                }
            });
            linearLayout.addView(aExample);
        }
    }
}
