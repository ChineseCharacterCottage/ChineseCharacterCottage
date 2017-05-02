package ecnu.chinesecharactercottage.modelsForeground;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ecnu.chinesecharactercottage.activitys.character.ComponentListActivity;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/12/11.
 */

public class ChoseComponentDialog extends DialogFragment {

    private Button mShapeComponent;
    private Button mVoiceComponent;
    private int mChoice;

    static public ChoseComponentDialog getDialogInstance(){
        return new ChoseComponentDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.choseDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_chose_component,container,false);

        mShapeComponent=(Button)v.findViewById(R.id.button_shape_component);
        mShapeComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoice=0;
                starComponent();
            }
        });
        mVoiceComponent=(Button)v.findViewById(R.id.button_voice_component);
        mVoiceComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoice=1;
                starComponent();
            }
        });
        return v;
    }

    private void starComponent(){
        ComponentListActivity.startActivity(getActivity(),mChoice);
    }
}
