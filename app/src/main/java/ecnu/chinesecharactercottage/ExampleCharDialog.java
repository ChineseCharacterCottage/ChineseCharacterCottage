package ecnu.chinesecharactercottage;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 10040 on 2016/12/4.
 */

public class ExampleCharDialog extends DialogFragment {

    private CharacterFragment mCharacterFragment;

    public static ExampleCharDialog startDialog(CharItem charItem){
        ExampleCharDialog myExampleDialog=new ExampleCharDialog();
        myExampleDialog.setCharacter(charItem);
        return myExampleDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.example_char_dialog,container,false);

        mCharacterFragment=(CharacterFragment)getFragmentManager().findFragmentById(R.id.example_fragment);

        return view;
    }

    public void setCharacter(CharItem charItem){
        mCharacterFragment.setCharacter(charItem);
    }
}
