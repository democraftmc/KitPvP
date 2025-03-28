package fr.democraft.kitpvp.game;

import fr.democraft.kitpvp.Game;
import fr.democraft.kitpvp.api.Ability;
import fr.democraft.kitpvp.api.Kit;
import fr.democraft.kitpvp.util.CacheManager;
import fr.democraft.kitpvp.util.Cooldown;
import org.bukkit.entity.Player;

public class Cooldowns {

	private final Stats stats;
	private final Infobase database;
	
	public Cooldowns(Game plugin, Arena arena) {
		this.stats = arena.getStats();
		this.database = plugin.getDatabase();
	}

	public void setAbilityCooldown(String playerName, String abilityName) {
		CacheManager.getPlayerAbilityCooldowns(playerName).put(abilityName, (System.currentTimeMillis() / 1000));
	}

	public void clearPlayerAbilityCooldowns(String playerName) {
		CacheManager.getPlayerAbilityCooldowns(playerName).clear();
	}

	public void setKitCooldown(String username, String kitName) {
		long timeKitLastUsed = System.currentTimeMillis() / 1000;
		stats.getOrCreateStatsCache(username).addKitCooldown(kitName, timeKitLastUsed);
	}

	public Cooldown getRemainingCooldown(Player p, Object type) {
		long currentTimeSeconds = (System.currentTimeMillis() / 1000);
		int timeLastUsedSeconds = 0;
		int actionCooldownSeconds = 0;
		Cooldown noCooldown = new Cooldown(0, 0, 0, 0);

		if (type instanceof Kit) {

			Kit kit = (Kit) type;
			if (kit.getCooldown() == null) return noCooldown;

			Object timeLastUsedResult = database.getData(kit.getName() + "_cooldowns", "last_used", p.getName());
			if (timeLastUsedResult != null) {
				timeLastUsedSeconds = (int) timeLastUsedResult;
			} else {
				return noCooldown;
			}
			actionCooldownSeconds = kit.getCooldown().toSeconds();

		} else if (type instanceof Ability) {

			Ability ability = (Ability) type;
			if (ability.getCooldown() == null ||
					!CacheManager.getPlayerAbilityCooldowns(p.getName()).containsKey(ability.getName()))
				return noCooldown;

			timeLastUsedSeconds = CacheManager.getPlayerAbilityCooldowns(p.getName()).get(ability.getName()).intValue();
			actionCooldownSeconds = ability.getCooldown().toSeconds();

		}

		int cooldownRemainingSeconds = (int) (timeLastUsedSeconds + actionCooldownSeconds - currentTimeSeconds);
		return new Cooldown(cooldownRemainingSeconds);
	}
	
}
