package dev.slickcollections.kiwizin.thebridge.api.cache;

import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.object.CacheObject;

public class RemoveProtectionCache extends CacheObject {
    public RemoveProtectionCache(String playerName)  {
        super(playerName, RemoveProtectionCache.class);
    }

    @Override
    public void destroyCache() {
        CacheManager.loadCaches().remove(this);
        this.keyFinder = null;
    }
}
