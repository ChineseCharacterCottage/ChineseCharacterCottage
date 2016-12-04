package ecnu.chinesecharactercottage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 10040 on 2016/11/30.
 */

public class RadicalAdapter extends ArrayAdapter<RadicalItem> {

    private int resourceId;

    public RadicalAdapter (Context context,int textViewResourceId,List<RadicalItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }


    @Override
    public View getView(int position, View converView, ViewGroup parent){
        RadicalItem thisRadical=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView figure=(TextView)view.findViewById(R.id.radical_figure);
        TextView englishName=(TextView)view.findViewById(R.id.radical_english_name);
        figure.setText(thisRadical.getRadical());
        englishName.setText(thisRadical.getName());

        return view;
    }
}
