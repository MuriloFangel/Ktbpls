package dev.slickcollections.kiwizin.thebridge.api;

import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.api.object.CacheObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CacheManager {

    private static final List<CacheObject> CACHE_LIST = new ArrayList<>();

    @SafeVarargs
    public static void clearCache(String playerName, Class<? extends CacheObject>... classes) {
        for (Class<? extends CacheObject> clazz : classes) {
            for (CacheObject cacheObject : CACHE_LIST.stream().filter(cacheObject -> cacheObject.getCacheClass().isAssignableFrom(clazz) && cacheObject.getKeyFinder().equals(playerName)).collect(Collectors.toList())) {
                cacheObject.destroyCache();
            }
        }
    }

    public static void removeProtectionPlayer(String playerName) {
        CACHE_LIST.add(new RemoveProtectionCache(playerName));
    }

    public static void addProtectionPlayer(String playerName) {
        if (hasProtection(playerName)) {
            CACHE_LIST.remove(getCache(playerName, RemoveProtectionCache.class));
        }
    }

    public static boolean hasProtection(String playerName) {
        return getCache(playerName, RemoveProtectionCache.class) != null;
    }
    @SuppressWarnings("unchecked")
    public static <T extends CacheObject> T getCache(Object keyFinder, Class<T> cacheClass) {
        return (T) CACHE_LIST.stream().filter(cacheObject -> cacheObject.getKeyFinder().equals(keyFinder) && cacheClass.isAssignableFrom(cacheObject.getCacheClass())).findFirst().orElse(null);
    }

    public static List<CacheObject> loadCaches() {
        return CACHE_LIST;
    }
}
