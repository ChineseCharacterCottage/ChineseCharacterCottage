package ecnu.chinesecharactercottage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by 10040 on 2016/11/22.
 */

public class ExampleCharacter extends LinearLayout{

    public ExampleCharacter(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.example_character,this);
    }

    public ExampleCharacter(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.example_character,this);
    }

    public void setExample()
}
