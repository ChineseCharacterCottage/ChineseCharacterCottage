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

public class ComponentAdapter extends ArrayAdapter<ComponentItem> {

    private int resourceId;

    public ComponentAdapter (Context context,int textViewResourceId,List<ComponentItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }


    @Override
    public View getView(int position, View converView, ViewGroup parent){
        ComponentItem thisComponent=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView figure=(TextView)view.findViewById(R.id.component_figure);
        figure.setText(thisComponent.getShape());

        return view;
    }
}
