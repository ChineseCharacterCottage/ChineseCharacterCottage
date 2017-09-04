package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;

/**
 * @author 匡申升
 * 测试题。
 * 注：这是一个抽象类。
 */

public abstract class TestItem {
    /**
     * 获取正确的答案。*/
    abstract public Object getCorrectAnswer();
    /**
     * 将这个测试题转换为ContentValues以方便存入数据库。*/
    abstract public ContentValues toContentValue();
    /**
     * 获取这个测试题的ID。*/
    abstract public String getTestId();
    /**
     * 获取这个测试题的类型。*/
    abstract public String getType();
}
