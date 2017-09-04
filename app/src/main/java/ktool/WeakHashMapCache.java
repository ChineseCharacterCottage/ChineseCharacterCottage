package ktool;

import java.util.WeakHashMap;

/**
 * @author 匡申升
 * RAM部分采用弱哈希表的三级缓存
 * @see ThreeLevelCache
 */

public abstract class WeakHashMapCache<K,V> extends ThreeLevelCache<K,V> {

    private WeakHashMap<K,V> mWeakHashMap;

    public WeakHashMapCache(){
        mWeakHashMap = new WeakHashMap<>();
    }

    @Override
    protected V getObjectFromRAM(K key){
        return mWeakHashMap.get(key);
    }

    @Override
    protected void restoreObjectToRAM(K key,V value){
        mWeakHashMap.put(key,value);
    }

}
