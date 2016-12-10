package ecnu.chinesecharactercottage;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 10040 on 2016/12/10.
 */

public class ComponentDialog extends DialogFragment {

    //部首
    private static ComponentItem sComponent;

    //部首字形
    private TextView mFigure;
    //部首中文意思
    private TextView mMeaning;
    //部首例字
    private LinearLayout mExampleCharacter;

    static public ComponentDialog getDialogInstance(ComponentItem componentItem){
        sComponent=componentItem;
        ComponentDialog myComponentDialog=new ComponentDialog();
        return myComponentDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME,android.R.style.Theme_Holo_Light_Dialog);
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
            aExample.setText(examples[i]);
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
