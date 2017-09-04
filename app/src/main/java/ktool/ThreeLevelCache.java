package ktool;

import android.support.annotation.Nullable;

/**
 * @author 匡申升
 * 简单的三级缓存实现
 */

abstract public class ThreeLevelCache<K,V> {

    /**
     * 访问完成调用的接口。*/
    public interface onAccessCompleteListener<V>{
        void onAccessComplete(boolean isSuccess,V object);
    }
    /**
     * 访问远程服务器的线程。*/
    private class AccessThread extends Thread{

        onAccessCompleteListener<V> mListener;
        K mKey;
        public AccessThread(@Nullable onAccessCompleteListener<V> listener, K key){
            super();
            mListener = listener;
            mKey = key;
        }
        @Override
        public void run(){
            V object = getObjectFromWebServer(mKey);
            if(object != null){
                restoreObjectToRAM(mKey,object);
                restoreObjectToDisk(mKey,object);
                if(mListener!=null){
                    mListener.onAccessComplete(true,object);
                }
            }
            if(mListener!=null){
                mListener.onAccessComplete(false,null);
            }
        }
    }
    /**
     * 当缓存有内容时，直接返回该对象，否则，启动一个线程从远程服务器获取资源，该线程结束后会调用回调函数，返回null。*/
    protected V getObjectWithListener(K key,@Nullable onAccessCompleteListener<V> listener){
        V object = this.getObjectFromRAM(key);
        if(object != null) return object;
        object = this.getObjectFromDisk(key);
        if(object != null){
            restoreObjectToRAM(key,object);
            return object;
        }
        new AccessThread(listener,key).start();
        return null;
    }
    /**
     * 获取一个对象。如果在缓存中，则直接返回，否则从更高一级的存储中获取。*/
    protected V getObject(K key){
        V object = this.getObjectFromRAM(key);
        if(object != null) return object;
        object = this.getObjectFromDisk(key);
        if(object != null){
            restoreObjectToRAM(key,object);
            return object;
        }
        object = this.getObjectFromWebServer(key);
        if(object != null){
            restoreObjectToRAM(key,object);
            restoreObjectToDisk(key,object);
            return object;
        }
        return null;
    }
    /**
     * 从内存中获取对象。*/
    abstract protected V getObjectFromRAM(K key);
    /**
     * 从磁盘获取对象。*/
    abstract protected V getObjectFromDisk(K key);
    /**
     * 从服务器获取对象。*/
    abstract protected V getObjectFromWebServer(K key);
    /**
     * 将远程获取的对象存在磁盘上。*/
    abstract protected void restoreObjectToDisk(K key,V value);
    /**
     * 将远程获取的对象存在内存里。*/
    abstract protected void restoreObjectToRAM(K key,V value);


}
