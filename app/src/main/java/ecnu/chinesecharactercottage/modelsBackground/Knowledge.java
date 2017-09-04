package ecnu.chinesecharactercottage.modelsBackground;

/**
 * @author 匡申升
 * 汉字小知识实例。简单的数据模型类。
 */

public class Knowledge {
    public Knowledge(String pId,String pTitle,String pVideo,String pText){
        id=pId;
        title=pTitle;
        video=pVideo;
        text=pText;
    }
    /**
     * 四个公有属性。分别是：标题、视频、文本、ID*/
    public final String title;
    public final String video;
    public final String text;
    public final String id;

    /**
     * 返回这个小知识的标题。*/
    public String getTitle(){return title;}
}
