package ecnu.chinesecharactercottage.modelsBackground;

import java.io.Serializable;

/**
 * @author 匡申升
 * 部件实例。
 * 部件分为形部和声部。
 */

public class ComponentItem implements Serializable {
    public static final int SHAPE=0;
    public static final int VOICE=1;
    private String mGId;//这个部件的ID
    private String mShape;//这个部件的形状
    private String[] mChars;//这个部件的例字。
    private String mExp;//这个部件的描述。
    private String mVoS;//这个部件是形部还是声部
    /**
     * 生成一个部件实例。*/
    public ComponentItem(String shape,String[] chars,String explanation,String voiceOrShape,String globalID){
        mShape=shape;
        mChars=chars;
        mExp=explanation;
        mGId=globalID;
        if(voiceOrShape.equals("s") || voiceOrShape.equals("v")) mVoS=voiceOrShape;
        else throw new RuntimeException("ComponentItem voiceOrShape must be \"v\" or \"s\".");
    }
    /**
     * 方便调试。*/
    @Override
    public String toString(){
        return mShape+" "+mExp+" "+mVoS;
    }
    /**
     * 获取这个部件的形状。*/
    public String getShape(){
        return mShape;
    }
    /**
     * 获取这个部件的ID。*/
    public String getGlobalId(){
        return mGId;
    }
    /**
     * 获取这个部件的解释。*/
    public String getExplanation(){
        return mExp;
    }
    /**
     * 获取这个部件的例字。*/
    public String[] getCharacters(){
        return mChars;
    }
    /**
     * 返回这个部件是声部还是形部。*/
    public String getVoiceOrShape(){
        return mVoS;
    }
    /**
     * 返回一个常量用来判断这个部件是声部还是形部。*/
    public int getModel(){
        int model=-1;
        if("s".equals(mVoS))
            model=SHAPE;
        else if("v".equals(mVoS))
            model=VOICE;
        return model;
    }
}
