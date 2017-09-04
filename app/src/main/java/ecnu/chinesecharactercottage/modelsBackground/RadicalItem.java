package ecnu.chinesecharactercottage.modelsBackground;

import java.io.Serializable;

/**
 * @author 匡申升
 * @see CharItem
 * 汉字的部首实例。
 */

public class RadicalItem implements Serializable {
    private String[] mExamples;
    private String mShape;
    private String mId;
    private String mName;
    public RadicalItem(String[] examples,String shape,String id,String name) {
        mId=id;
        mExamples=examples;
        mShape=shape;
        mName=name;
    }
    /**
     * 获取这个部首的名字。*/
    public String getName(){
        return mName;
    }
    /**
     * 获取这个部首的形状。*/
    public String getRadical(){
        return mShape;
    }
    /**
     * 获取这个部首的ID。*/
    public String getId(){
        return mId;
    }
    /**
     * 获取这个部首的例字。*/
    public String[] getExamples(){
        return mExamples;
    }
    /**
     * 方便调试。*/
    @Override
    public String toString(){
        return mShape+" "+mName;
    }
}
