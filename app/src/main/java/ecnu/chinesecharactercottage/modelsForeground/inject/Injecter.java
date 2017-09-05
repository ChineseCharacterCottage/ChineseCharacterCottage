package ecnu.chinesecharactercottage.modelsForeground.inject;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * @author 胡家斌
 * 这个类负责解析注解，原理很简单，就是利用反射遍历调用对象的域，找到被@InjectView注释的成员，并且绑定它们到传入的id的控件上去
 */
public class Injecter {
    //这个方法解析活动
    public static void autoInjectAllField(Activity activity) {
        //得到Activity对应的Class
        Class clazz=activity.getClass();
        //得到该Activity的所有字段
        Field[] fields=clazz.getDeclaredFields();
        for(Field field :fields)//循环遍历其所有域
        {
            //判断属性是否已经被@InjectView注释了
            if(field.isAnnotationPresent(InjectView.class))
            {
                //如果注释了，就获得它的id
                InjectView inject=field.getAnnotation(InjectView.class);
                int id=inject.id();//获取注释类对象的id属性
                if(id>0)//保证id已经正确设置了，因为如果没设置则id=-1
                {
                    //设置可见性为可访问,否则访问权限不够的的变量外部无法操作
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

    //这个方法解析碎片
    public static void autoInjectAllField(Fragment fragment,View view) {
        //得到Fragment对应的Class
        Class clazz=fragment.getClass();
        //得到该Fragment的所有字段
        Field[] fields=clazz.getDeclaredFields();
        for(Field field :fields)
        {
            //判断属性是否已经被@InjectView注释了
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
