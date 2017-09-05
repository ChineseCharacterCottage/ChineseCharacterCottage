package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * ListView的适配器，其中利用了反射来动态定义获取数据的方法，提高了重用性；
 * 该类通过设置方法名来获取字符串填入视图中的一个id为text_view的TextView
 * @param <T>
 */

public class ListViewItemAdapter<T> extends ArrayAdapter<T> {

    //资源id
    private int mResourceId;
    //获取数据的方法名称
    private String mMethodName;
    //数据存储器，保存一些数值避免重复加载，提高加载速度
    private ViewHolder mViewHolder=null;

    /**
     * 构造函数，需要自定义的视图
     * @param context 上下文
     * @param viewResourceId 每个项的视图id
     * @param objects 数据列表
     * @param methodName 从每个数据项中获取数据的方法
     */
    public ListViewItemAdapter(Context context, int viewResourceId, List<T> objects, String methodName){
        super(context,viewResourceId,objects);
        mResourceId=viewResourceId;
        mMethodName=methodName;
    }

    /**
     * 构造函数，不需要自定义的视图，使用默认的视图，只含有一个TextView
     * @param context 上下文
     * @param objects 数据列表
     * @param methodName 从每个数据项中获取数据的方法
     */
    public ListViewItemAdapter(Context context, List<T> objects, String methodName){
        this(context, R.layout.default_list_item, objects, methodName);
    }

    @Override
    @NonNull
    public View getView(int position, View converView, @NonNull ViewGroup parent){
        T t=getItem(position);//从列表中获取对应项
        View view;//需要渲染的视图
        if(mViewHolder==null) {//如果数据存储器
            try {
                //创建一个新的数据存储器，把项视图和方法名保存起来
                mViewHolder=new ViewHolder(LayoutInflater.from(getContext()).inflate(mResourceId, null),
                        t.getClass().getDeclaredMethod(mMethodName));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        view=mViewHolder.view;//从数据存储器获取视图
        TextView figure=(TextView) view.findViewById(R.id.text_view);//绑定控件
        try {
            figure.setText((String)mViewHolder.method.invoke(t));//设置TextView内容
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return view;
    }

    private class ViewHolder{
        //保存的视图
        public View view;
        //保存的方法
        public Method method;

        ViewHolder(View v, Method m){
            view=v;
            method=m;
        }
    }
}
