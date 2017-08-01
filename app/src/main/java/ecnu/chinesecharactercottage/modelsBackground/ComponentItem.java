package ecnu.chinesecharactercottage.modelsBackground;

import java.io.Serializable;

/**
 * Created by Shensheng on 2016/12/9.
 */

public class ComponentItem implements Serializable {
    public static final int SHAPE=0;
    public static final int VOICE=1;
    private String mGId;
    private String mShape;
    private String[] mChars;
    private String mExp;
    private String mVoS;
    public ComponentItem(String shape,String[] chars,String explanation,String voiceOrShape,String globalID){
        mShape=shape;
        mChars=chars;
        mExp=explanation;
        mGId=globalID;
        if(voiceOrShape.equals("s") || voiceOrShape.equals("v")) mVoS=voiceOrShape;
        else throw new RuntimeException("ComponentItem voiceOrShape must be \"v\" or \"s\".");
    }
    @Override
    public String toString(){
        return mShape+" "+mExp+" "+mVoS;
    }
    public String getShape(){
        return mShape;
    }
    public String getGlobalId(){
        return mGId;
    }
    public String getExplanation(){
        return mExp;
    }

    public String[] getCharacters(){
        return mChars;
    }

    public String getVoiceOrShape(){
        return mVoS;
    }

    public int getModel(){
        int model=-1;
        if("s".equals(mVoS))
            model=SHAPE;
        else if("v".equals(mVoS))
            model=VOICE;
        return model;
    }
}
