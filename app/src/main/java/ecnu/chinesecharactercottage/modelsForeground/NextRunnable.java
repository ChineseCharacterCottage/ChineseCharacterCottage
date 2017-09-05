package ecnu.chinesecharactercottage.modelsForeground;

/**
 * @author 胡家斌
 * 一个简单的接口，其实功能和Runnable差不多，只不过方法名叫next()比较便于理解
 * 主要是用来在活动间或者活动和碎片之间传递操作的
 */

public abstract class NextRunnable{
    abstract public void next();
}
