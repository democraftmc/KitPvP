package fr.democraft.kitpvp.util;

import fr.democraft.database.TopEntry;
import fr.democraft.kitpvp.Game;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import fr.democraft.kitpvp.game.Arena;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Placeholders extends PlaceholderExpansion {

	private final Arena arena;
	private final Map<String, String> placeholderAPItoBuiltIn;
	
	public Placeholders(Game plugin) {
		this.arena = plugin.getArena();
		this.placeholderAPItoBuiltIn = new HashMap<>();

		placeholderAPItoBuiltIn.put("stats_kills", "%kills%");
		placeholderAPItoBuiltIn.put("stats_deaths", "%deaths%");
		placeholderAPItoBuiltIn.put("stats_kd", "%kd%");
		placeholderAPItoBuiltIn.put("stats_experience", "%xp%");
		placeholderAPItoBuiltIn.put("stats_level", "%level%");
		placeholderAPItoBuiltIn.put("player_killstreak", "%streak%");
		placeholderAPItoBuiltIn.put("player_kit", "%kit%");
		placeholderAPItoBuiltIn.put("max_level", "%max_level%");
		placeholderAPItoBuiltIn.put("max_xp", "%max_xp%");
		placeholderAPItoBuiltIn.put("level_prefix", "%level_prefix%");
	}
	
	@Override
	public String onPlaceholderRequest(Player p, @NotNull String identifier) {
		if (identifier.contains("top")) {
			return handleLeaderboardPlaceholder(identifier);
		}

		if (p != null) {
			return translatePlaceholderAPIPlaceholders(identifier, p.getName());
		}
		return null;
	}

	private String handleLeaderboardPlaceholder(String identifier) {
		String[] queries = identifier.split("_");
		String topType = queries[1]; // ex: kills
		String topIdentifier = queries[2]; // ex: amount, player
		String possibleTopValue = queries[3]; // 1, 15
		int topValue = 1; // if number parsing fails, use top player

		if (StringUtils.isNumeric(possibleTopValue)) {
			topValue = Integer.parseInt(possibleTopValue);
		} else {
			Toolkit.printToConsole("%prefix% &cFailed to properly parse placeholder, " +
					"expected number but received: " + possibleTopValue);
		}

		TopEntry entry = arena.getLeaderboards().getTopN(topType, topValue);
		boolean isUsernamePlaceholder = topIdentifier.equals("player");

		return isUsernamePlaceholder ? entry.getIdentifier() : String.valueOf(entry.getValue());
	}

	public String translatePlaceholderAPIPlaceholders(String placeholderAPIIdentifier, String username) {
		if (placeholderAPItoBuiltIn.containsKey(placeholderAPIIdentifier)) {
			String toBuiltInPlaceholder = placeholderAPItoBuiltIn.get(placeholderAPIIdentifier);
			return arena.getUtilities().replaceBuiltInPlaceholdersIfPresent(toBuiltInPlaceholder, username);
		} else {
			Toolkit.printToConsole(String.format("&7[&b&lKIT-PVP&7] &cUnknown placeholder identifier [%s]. " +
							"Please see plugin page.", placeholderAPIIdentifier));
			return "invalid-placeholder";
		}
	}
	
	@Override
	public boolean canRegister() { return true; }

	@Override
	public boolean persist() { return true; }

	@Override
	public @NotNull String getAuthor() { return "Cervinakuy"; }
	
	@Override
	public @NotNull String getIdentifier() { return "kitpvp"; }
	
	@Override
	public @NotNull String getVersion() { return "1.0.0"; }
	
}
