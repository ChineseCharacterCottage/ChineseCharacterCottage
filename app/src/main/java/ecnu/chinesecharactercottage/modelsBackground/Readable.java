package ecnu.chinesecharactercottage.modelsBackground;

/**
 * @author 匡申升
 * Readable接口，表示这是一个可以朗读出来的类。
 * 注意！！！此Readable不同于于java.lang.Readable
 */

public interface Readable {
    /**
     * 获取音频的键。
     * @return 返回一个音频媒体的键，用于从SoundGetter中取出MediaPlayer实例。
     * @see SoundGetter
     */
    String getMediaKey();
}
