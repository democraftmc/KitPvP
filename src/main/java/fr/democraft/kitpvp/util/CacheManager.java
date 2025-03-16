package fr.democraft.kitpvp.util;

import fr.democraft.kitpvp.api.Kit;

import java.util.*;

public class CacheManager {

    private static final Map<String, String> usernameToUUID = new HashMap<>();
    private static final Map<String, Kit> kitCache = new HashMap<>();
    private static final Map<String, Menu> previewMenuCache = new HashMap<>();
    private static final Map<String, Map<String, Long>> abilityCooldowns = new HashMap<>();
    private static final Map<String, PlayerData> statsCache = new HashMap<>();
    private static final Set<String> potionSwitcherUsers = new HashSet<>();

    public static Map<String, String> getUUIDCache() { return usernameToUUID; }

    public static Map<String, Kit> getKitCache() { return kitCache; }

    public static Map<String, Menu> getPreviewMenuCache() { return previewMenuCache; }

    public static Map<String, PlayerData> getStatsCache() { return statsCache; }

    public static Set<String> getPotionSwitcherUsers() { return potionSwitcherUsers; }

    public static Map<String, Long> getPlayerAbilityCooldowns(String username) {
        if (!abilityCooldowns.containsKey(username)) {
            abilityCooldowns.put(username, new HashMap<>());
        }
        return abilityCooldowns.get(username);
    }

    public static void clearCaches() {
        kitCache.clear();
        previewMenuCache.clear();
        abilityCooldowns.clear();
        // stats, usernameToUUID, and cache isn't here as of right now
    }

}
