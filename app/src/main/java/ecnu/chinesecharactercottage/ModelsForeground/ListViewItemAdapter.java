package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/11/30.
 */

public class ListViewItemAdapter<T> extends ArrayAdapter<T> {

    private int resourceId;
    private String mMethodName;
    private ViewHolder mViewHolder=null;

    public ListViewItemAdapter(Context context, int textViewResourceId, List<T> objects, String methodName){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        mMethodName=methodName;
    }

    public ListViewItemAdapter(Context context, List<T> objects, String methodName){
        this(context, R.layout.default_list_item, objects, methodName);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent){
        T t=getItem(position);
        View view;
        if(mViewHolder==null) {
            try {
                mViewHolder=new ViewHolder(LayoutInflater.from(getContext()).inflate(resourceId, null),
                        t.getClass().getDeclaredMethod(mMethodName));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        view=mViewHolder.view;
        TextView figure=(TextView) view.findViewById(R.id.text_view);
        try {
            figure.setText((String)mViewHolder.method.invoke(t));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return view;
    }

    private class ViewHolder{
        public View view;
        public Method method;

        ViewHolder(View v, Method m){
            view=v;
            method=m;
        }
    }
}
