package ecnu.chinesecharactercottage.modelsForeground.inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 胡家斌
 * 这个类是个注释类，使用时使用@InjectView(id=xxx)这样的格式，则会标记它后面的成员，并设置其id为xxx
 */
@Target(ElementType.FIELD)//@Target是元注解，这里表示注解的对象是类的域
@Retention(RetentionPolicy.RUNTIME)//@Retention也是元注解，这里表示注解会保留到运行时
public @interface InjectView
{
    //控件id
    int id() default -1;//id的默认值是-1
}