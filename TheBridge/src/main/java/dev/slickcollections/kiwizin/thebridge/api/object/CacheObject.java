package dev.slickcollections.kiwizin.thebridge.api.object;

public abstract class CacheObject {

    protected Object keyFinder;
    protected Class<? extends CacheObject> cacheClass;

    public CacheObject(Object keyFinder, Class<? extends CacheObject> cacheClass) {
        this.keyFinder = keyFinder;
        this.cacheClass = cacheClass;
    }

    public Object getKeyFinder() {
        return this.keyFinder;
    }

    public Class<? extends CacheObject> getCacheClass() {
        return this.cacheClass;
    }

    public abstract void destroyCache();
}
