package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.ContentValues;

/**
 * Created by Shensheng on 2017/3/6.
 */

public abstract class TestItem {
    abstract public Object getCorrectAnswer();
    abstract public ContentValues toContentValue();
}
