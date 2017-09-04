package ecnu.chinesecharactercottage.modelsBackground;

/**
 * @author 匡申升
 * @see CharItem
 * 一个带有视频的CharItem。
 */

public class ShapeCharItem extends CharItem {
    String mVideo;
    String mShapeId;
    public String getShapeId(){
        return mShapeId;
    }
    public ShapeCharItem(CharItem charItem){
        super(charItem.toJSON());
    }
    /**
     * 获取视频源地址。*/
    public String getVideo(){
        return mVideo;
    }
}
