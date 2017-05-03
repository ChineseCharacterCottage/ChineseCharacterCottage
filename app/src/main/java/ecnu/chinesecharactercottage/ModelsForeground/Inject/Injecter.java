package ecnu.chinesecharactercottage.modelsForeground.inject;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

public class Injecter {
    public static void autoInjectAllField(Activity activity) {
        //得到Activity对应的Class
        Class clazz=activity.getClass();
        //得到该Activity的所有字段
        Field[] fields=clazz.getDeclaredFields();
        for(Field field :fields)
        {
            //判断属性是否已经被注释了
            if(field.isAnnotationPresent(InjectView.class))
            {
                //如果注释了，就获得它的id
                InjectView inject=field.getAnnotation(InjectView.class);
                int id=inject.id();
                if(id>0)
                {
                    //设置可见性为可访问
                    field.setAccessible(true);
                    //对这个属性赋值
                    try {
                        field.set(activity, activity.findViewById(id));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Log.d("Injecter","视图的id不存在或者不匹配");
                    }
                }
            }
        }
    }

    public static void autoInjectAllField(Fragment fragment,View view) {
        //得到Fragment对应的Class
        Class clazz=fragment.getClass();
        //得到该Fragment的所有字段
        Field[] fields=clazz.getDeclaredFields();
        for(Field field :fields)
        {
            //判断属性是否已经被注释了
            if(field.isAnnotationPresent(InjectView.class))
            {
                //如果注释了，就获得它的id
                InjectView inject=field.getAnnotation(InjectView.class);
                int id=inject.id();
                if(id>0)
                {
                    //设置可见性为可访问
                    field.setAccessible(true);
                    //对这个属性赋值
                    try {
                        field.set(fragment, view.findViewById(id));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
