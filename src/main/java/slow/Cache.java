package slow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private final Map<CacheKey, Object> holder = new ConcurrentHashMap<>();

    public void put(Object key, Object value) {
        holder.put(new CacheKey(key), value);
    }

    public Object get(Object key) {
        return holder.get(new CacheKey(key));
    }

    public static class CacheKey {
        public Object key;

        public CacheKey(Object key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (key != null ? !key.equals(cacheKey.key) : cacheKey.key != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return key != null ? key.hashCode() : 0;
        }
    }
}
