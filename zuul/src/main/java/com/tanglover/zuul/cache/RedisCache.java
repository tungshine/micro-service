package com.tanglover.zuul.cache;

import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DecoratedRedisConnection;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author TangXu
 * @create 2019-07-22 15:53
 * @description:
 */
public class RedisCache extends AbstractValueAdaptingCache {

    /**
     * 任务执行线程数200，队列缓冲数2000
     */
    private static ExecutorService exec = new ThreadPoolExecutor(20, 100, 60L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(10000));
    private final RedisOperations redisOperations;
    private final RedisCacheMetadata cacheMetadata;
    private final CacheValueAccessor cacheValueAccessor;

    /**
     * Constructs a new {@link RedisCache} instance.
     *
     * @param name            cache name
     * @param prefix
     * @param redisOperations
     * @param expiration
     */
    public RedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                      long expiration) {
        this(name, prefix, redisOperations, expiration, false);
    }

    /**
     * Constructs a new {@link RedisCache} instance.
     *
     * @param name            cache name
     * @param prefix          must not be {@literal null} or empty.
     * @param redisOperations
     * @param expiration
     * @param allowNullValues
     * @since 1.8
     */
    public RedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                      long expiration, boolean allowNullValues) {

        super(allowNullValues);

        Assert.hasText(name, "CacheName must not be null or empty!");

        RedisSerializer<?> serializer = redisOperations.getValueSerializer() != null
                ? redisOperations.getValueSerializer() : (RedisSerializer<?>) new JdkSerializationRedisSerializer();

        this.cacheMetadata = new RedisCacheMetadata(name, prefix);
        this.cacheMetadata.setDefaultExpiration(expiration);
        this.redisOperations = redisOperations;
        this.cacheValueAccessor = new CacheValueAccessor(serializer);

        if (allowNullValues) {

            if (redisOperations.getValueSerializer() instanceof StringRedisSerializer
                    || redisOperations.getValueSerializer() instanceof GenericToStringSerializer
                    || redisOperations.getValueSerializer() instanceof JacksonJsonRedisSerializer
                    || redisOperations.getValueSerializer() instanceof Jackson2JsonRedisSerializer) {
                throw new IllegalArgumentException(String.format(
                        "Redis does not allow keys with null value ¯\\_(ツ)_/¯. "
                                + "The chosen %s does not support generic type handling and therefore cannot be used with allowNullValues enabled. "
                                + "Please use a different RedisSerializer or disable null value support.",
                        ClassUtils.getShortName(redisOperations.getValueSerializer().getClass())));
            }
        }
    }

    @Override
    protected Object lookup(Object key) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }

    /**
     * Metadata required to maintain {@link RedisCache}. Keeps track of
     * additional data structures required for processing cache operations.
     *
     * @author Christoph Strobl
     * @since 1.5
     */
    static class RedisCacheMetadata {

        private final String cacheName;
        private final byte[] keyPrefix;
        private final byte[] setOfKnownKeys;
        private final byte[] cacheLockName;
        private long defaultExpiration = 0;

        /**
         * @param cacheName must not be {@literal null} or empty.
         * @param keyPrefix can be {@literal null}.
         */
        public RedisCacheMetadata(String cacheName, byte[] keyPrefix) {

            Assert.hasText(cacheName, "CacheName must not be null or empty!");
            this.cacheName = cacheName;
            this.keyPrefix = keyPrefix;

            StringRedisSerializer stringSerializer = new StringRedisSerializer();

            // name of the set holding the keys
            this.setOfKnownKeys = usesKeyPrefix() ? new byte[]{} : stringSerializer.serialize(cacheName + "~keys");
            this.cacheLockName = stringSerializer.serialize(cacheName + "~lock");
        }

        /**
         * @return true if the {@link RedisCache} uses a prefix for key ranges.
         */
        public boolean usesKeyPrefix() {
            return (keyPrefix != null && keyPrefix.length > 0);
        }

        /**
         * Get the binary representation of the key prefix.
         *
         * @return never {@literal null}.
         */
        public byte[] getKeyPrefix() {
            return this.keyPrefix;
        }

        /**
         * Get the binary representation of the key identifying the data
         * structure used to maintain known keys.
         *
         * @return never {@literal null}.
         */
        public byte[] getSetOfKnownKeysKey() {
            return setOfKnownKeys;
        }

        /**
         * Get the binary representation of the key identifying the data
         * structure used to lock the cache.
         *
         * @return never {@literal null}.
         */
        public byte[] getCacheLockKey() {
            return cacheLockName;
        }

        /**
         * Get the name of the cache.
         *
         * @return
         */
        public String getCacheName() {
            return cacheName;
        }

        /**
         * Set the default expiration time in seconds
         *
         * @param seconds
         */
        public void setDefaultExpiration(long seconds) {
            this.defaultExpiration = seconds;
        }

        /**
         * Get the default expiration time in seconds.
         *
         * @return
         */
        public long getDefaultExpiration() {
            return defaultExpiration;
        }

    }

    /**
     * @author Christoph Strobl
     * @since 1.5
     */
    static class CacheValueAccessor {

        @SuppressWarnings("rawtypes") //
        private final RedisSerializer valueSerializer;

        @SuppressWarnings("rawtypes")
        CacheValueAccessor(RedisSerializer valueRedisSerializer) {
            valueSerializer = valueRedisSerializer;
        }

        byte[] convertToBytesIfNecessary(Object value) {

            if (value == null) {
                return new byte[0];
            }

            if (valueSerializer == null && value instanceof byte[]) {
                return (byte[]) value;
            }

            return valueSerializer.serialize(value);
        }

        Object deserializeIfNecessary(byte[] value) {

            if (valueSerializer != null) {
                return valueSerializer.deserialize(value);
            }

            return value;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.6
     */
    static class BinaryRedisCacheElement extends RedisCacheElement {

        private byte[] keyBytes;
        private byte[] valueBytes;
        private RedisCacheElement element;
        private boolean lazyLoad;
        private CacheValueAccessor accessor;

        public BinaryRedisCacheElement(RedisCacheElement element, CacheValueAccessor accessor) {

            super(element.getKey(), element.get());
            this.element = element;
            this.keyBytes = element.getKeyBytes();
            this.accessor = accessor;

            lazyLoad = element.get() instanceof Callable;
            this.valueBytes = lazyLoad ? null : accessor.convertToBytesIfNecessary(element.get());
        }

        @Override
        public byte[] getKeyBytes() {
            return keyBytes;
        }

        public long getTimeToLive() {
            return element.getTimeToLive();
        }

        public boolean hasKeyPrefix() {
            return element.hasKeyPrefix();
        }

        public boolean isEternal() {
            return element.isEternal();
        }

        public RedisCacheElement expireAfter(long seconds) {
            return element.expireAfter(seconds);
        }

        @Override
        public byte[] get() {

            if (lazyLoad && valueBytes == null) {
                try {
                    valueBytes = accessor.convertToBytesIfNecessary(((Callable<?>) element.get()).call());
                } catch (Exception e) {
                    throw e instanceof RuntimeException ? (RuntimeException) e
                            : new RuntimeException(e.getMessage(), e);
                }
            }
            return valueBytes;
        }
    }

    /**
     * @param <T>
     * @author Christoph Strobl
     * @since 1.5
     */
    static abstract class AbstractRedisCacheCallback<T> implements RedisCallback<T> {

        private long WAIT_FOR_LOCK_TIMEOUT = 300;
        private final BinaryRedisCacheElement element;
        private final RedisCacheMetadata cacheMetadata;

        public AbstractRedisCacheCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            this.element = element;
            this.cacheMetadata = metadata;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.
         * springframework.data.redis.connection.RedisConnection)
         */
        @Override
        public T doInRedis(RedisConnection connection) throws DataAccessException {
            waitForLock(connection);
            return doInRedis(element, connection);
        }

        public abstract T doInRedis(BinaryRedisCacheElement element, RedisConnection connection)
                throws DataAccessException;

        protected void processKeyExpiration(RedisCacheElement element, RedisConnection connection) {
            if (!element.isEternal()) {
                connection.expire(element.getKeyBytes(), element.getTimeToLive());
            }
        }

        protected void maintainKnownKeys(RedisCacheElement element, RedisConnection connection) {

            if (!element.hasKeyPrefix()) {

                connection.zAdd(cacheMetadata.getSetOfKnownKeysKey(), 0, element.getKeyBytes());

                if (!element.isEternal()) {
                    connection.expire(cacheMetadata.getSetOfKnownKeysKey(), element.getTimeToLive());
                }
            }
        }

        protected void cleanKnownKeys(RedisCacheElement element, RedisConnection connection) {

            if (!element.hasKeyPrefix()) {
                connection.zRem(cacheMetadata.getSetOfKnownKeysKey(), element.getKeyBytes());
            }
        }

        protected boolean waitForLock(RedisConnection connection) {

            boolean retry;
            boolean foundLock = false;
            do {
                retry = false;
                if (connection.exists(cacheMetadata.getCacheLockKey())) {
                    foundLock = true;
                    try {
                        Thread.sleep(WAIT_FOR_LOCK_TIMEOUT);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    retry = true;
                }
            } while (retry);

            return foundLock;
        }

        protected void lock(RedisConnection connection) {
            waitForLock(connection);
            connection.set(cacheMetadata.getCacheLockKey(), "locked".getBytes());
        }

        protected void unlock(RedisConnection connection) {
            connection.del(cacheMetadata.getCacheLockKey());
        }
    }

    /**
     * @param <T>
     * @author Christoph Strobl
     * @since 1.5
     */
    static abstract class LockingRedisCacheCallback<T> implements RedisCallback<T> {

        private final RedisCacheMetadata metadata;

        public LockingRedisCacheCallback(RedisCacheMetadata metadata) {
            this.metadata = metadata;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.
         * springframework.data.redis.connection.RedisConnection)
         */
        @Override
        public T doInRedis(RedisConnection connection) throws DataAccessException {

            if (connection.exists(metadata.getCacheLockKey())) {
                return null;
            }
            try {
                connection.set(metadata.getCacheLockKey(), metadata.getCacheLockKey());
                return doInLock(connection);
            } finally {
                connection.del(metadata.getCacheLockKey());
            }
        }

        public abstract T doInLock(RedisConnection connection);
    }

    /**
     * @author Christoph Strobl
     * @since 1.5
     */
    static class RedisCacheCleanByKeysCallback extends LockingRedisCacheCallback<Void> {

        private static final int PAGE_SIZE = 128;
        private final RedisCacheMetadata metadata;

        RedisCacheCleanByKeysCallback(RedisCacheMetadata metadata) {
            super(metadata);
            this.metadata = metadata;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.cache.RedisCache.
         * LockingRedisCacheCallback#doInLock(org.springframework.data.redis.
         * connection.RedisConnection)
         */
        @Override
        public Void doInLock(RedisConnection connection) {

            int offset = 0;
            boolean finished = false;

            do {
                // need to paginate the keys
                Set<byte[]> keys = connection.zRange(metadata.getSetOfKnownKeysKey(), (offset) * PAGE_SIZE,
                        (offset + 1) * PAGE_SIZE - 1);
                finished = keys.size() < PAGE_SIZE;
                offset++;
                if (!keys.isEmpty()) {
                    connection.del(keys.toArray(new byte[keys.size()][]));
                }
            } while (!finished);

            connection.del(metadata.getSetOfKnownKeysKey());
            return null;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.5
     */
    static class RedisCacheCleanByPrefixCallback extends LockingRedisCacheCallback<Void> {

        private static final byte[] REMOVE_KEYS_BY_PATTERN_LUA = new StringRedisSerializer().serialize(
                "local keys = redis.call('KEYS', ARGV[1]); local keysCount = table.getn(keys); if(keysCount > 0) then for _, key in ipairs(keys) do redis.call('del', key); end; end; return keysCount;");
        private static final byte[] WILD_CARD = new StringRedisSerializer().serialize("*");
        private final RedisCacheMetadata metadata;

        public RedisCacheCleanByPrefixCallback(RedisCacheMetadata metadata) {
            super(metadata);
            this.metadata = metadata;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.cache.RedisCache.
         * LockingRedisCacheCallback#doInLock(org.springframework.data.redis.
         * connection.RedisConnection)
         */
        @Override
        public Void doInLock(RedisConnection connection) throws DataAccessException {

            byte[] prefixToUse = Arrays.copyOf(metadata.getKeyPrefix(),
                    metadata.getKeyPrefix().length + WILD_CARD.length);
            System.arraycopy(WILD_CARD, 0, prefixToUse, metadata.getKeyPrefix().length, WILD_CARD.length);

            if (isClusterConnection(connection)) {

                // load keys to the client because currently Redis Cluster
                // connections do not allow eval of lua scripts.
                Set<byte[]> keys = connection.keys(prefixToUse);
                if (!keys.isEmpty()) {
                    connection.del(keys.toArray(new byte[keys.size()][]));
                }
            } else {
                connection.eval(REMOVE_KEYS_BY_PATTERN_LUA, ReturnType.INTEGER, 0, prefixToUse);
            }

            return null;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.5
     */
    static class RedisCacheEvictCallback extends AbstractRedisCacheCallback<Void> {

        public RedisCacheEvictCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.cache.RedisCache.
         * AbstractRedisCacheCallback#doInRedis(org.springframework.data.redis.
         * cache.RedisCacheElement,
         * org.springframework.data.redis.connection.RedisConnection)
         */
        @Override
        public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {

            connection.del(element.getKeyBytes());
            cleanKnownKeys(element, connection);
            return null;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.5
     */
    static class RedisCachePutCallback extends AbstractRedisCacheCallback<Void> {

        public RedisCachePutCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {

            super(element, metadata);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.cache.RedisCache.
         * AbstractRedisPutCallback#doInRedis(org.springframework.data.redis.
         * cache.RedisCache.RedisCacheElement,
         * org.springframework.data.redis.connection.RedisConnection)
         */
        @Override
        public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {

            if (!isClusterConnection(connection)) {
                connection.multi();
            }

            if (element.get().length == 0) {
                connection.del(element.getKeyBytes());
            } else {
                connection.set(element.getKeyBytes(), element.get());

                processKeyExpiration(element, connection);
                maintainKnownKeys(element, connection);
            }

            if (!isClusterConnection(connection)) {
                connection.exec();
            }
            return null;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.5
     */
    static class RedisCachePutIfAbsentCallback extends AbstractRedisCacheCallback<byte[]> {

        public RedisCachePutIfAbsentCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.redis.cache.RedisCache.
         * AbstractRedisPutCallback#doInRedis(org.springframework.data.redis.
         * cache.RedisCache.RedisCacheElement,
         * org.springframework.data.redis.connection.RedisConnection)
         */
        @Override
        public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection)
                throws DataAccessException {

            waitForLock(connection);

            byte[] keyBytes = element.getKeyBytes();
            byte[] value = element.get();

            if (!connection.setNX(keyBytes, value)) {
                return connection.get(keyBytes);
            }

            maintainKnownKeys(element, connection);
            processKeyExpiration(element, connection);

            return null;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.7
     */
    static class RedisWriteThroughCallback extends AbstractRedisCacheCallback<byte[]> {

        public RedisWriteThroughCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        @Override
        public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection)
                throws DataAccessException {

            try {

                lock(connection);

                try {

                    byte[] value = connection.get(element.getKeyBytes());

                    if (value != null) {
                        return value;
                    }

                    if (!isClusterConnection(connection)) {

                        connection.watch(element.getKeyBytes());
                        connection.multi();
                    }

                    value = element.get();

                    if (value.length == 0) {
                        connection.del(element.getKeyBytes());
                    } else {
                        connection.set(element.getKeyBytes(), value);
                        processKeyExpiration(element, connection);
                        maintainKnownKeys(element, connection);
                    }

                    if (!isClusterConnection(connection)) {
                        connection.exec();
                    }

                    return value;
                } catch (RuntimeException e) {
                    if (!isClusterConnection(connection)) {
                        connection.discard();
                    }
                    throw e;
                }
            } finally {
                unlock(connection);
            }
        }
    }

    ;

    /**
     * @author Christoph Strobl
     * @since 1.7 (TODO: remove when upgrading to spring 4.3)
     */
    private static enum CacheValueRetrievalExceptionFactory {

        INSTANCE;

        private static boolean isSpring43;

        static {
            isSpring43 = ClassUtils.isPresent("org.springframework.cache.Cache$ValueRetrievalException",
                    ClassUtils.getDefaultClassLoader());
        }

        public RuntimeException create(Object key, Callable<?> valueLoader, Throwable cause) {

            if (isSpring43) {
                try {
                    Class<?> execption = ClassUtils.forName("org.springframework.cache.Cache$ValueRetrievalException",
                            this.getClass().getClassLoader());
                    Constructor<?> c = ClassUtils.getConstructorIfAvailable(execption, Object.class, Callable.class,
                            Throwable.class);
                    return (RuntimeException) c.newInstance(key, valueLoader, cause);
                } catch (Exception ex) {
                    // ignore
                }
            }

            return new RedisSystemException(
                    String.format("Value for key '%s' could not be loaded using '%s'.", key, valueLoader), cause);
        }
    }

    private static boolean isClusterConnection(RedisConnection connection) {

        while (connection instanceof DecoratedRedisConnection) {
            connection = ((DecoratedRedisConnection) connection).getDelegate();
        }

        return connection instanceof RedisClusterConnection;
    }
}