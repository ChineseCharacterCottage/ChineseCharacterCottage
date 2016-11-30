package ecnu.chinesecharactercottage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

    }
}
