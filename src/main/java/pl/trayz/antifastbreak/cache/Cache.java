package pl.trayz.antifastbreak.cache;

import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Trayz
 **/

public class Cache {

    public static final com.google.common.cache.Cache<UUID, Byte> fastBreakPunishments = CacheBuilder
            .newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();

    public static final Map<String, Byte> ignoredPlayers = new ConcurrentHashMap<>();
}
