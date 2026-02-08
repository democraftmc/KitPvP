package fr.democraft.kitpvp.listener;

import fr.democraft.kitpvp.util.Resource;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import fr.democraft.kitpvp.Game;
import fr.democraft.kitpvp.game.Arena;
import fr.democraft.kitpvp.util.Toolkit;

public class JoinListener implements Listener {

	private final Game plugin;
	private final Arena arena;
	private final Resource config;
	
	public JoinListener(Game plugin) {
		this.plugin = plugin;
		this.arena = plugin.getArena();
		this.config = plugin.getResources().getConfig();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(p.getUniqueId());
        if (user == null) {
            luckPerms.getUserManager().loadUser(p.getUniqueId()).thenAccept(
                    loaded -> {
                        String metaValue = loaded.getCachedData().getMetaData().getMetaValue("lang.id");
                        if (metaValue != null && !metaValue.isEmpty()) {
                            plugin.setPlayerLanguage(p, metaValue);
                        } else {
                            plugin.setPlayerLanguage(p, "en");
                        }
                    }
            );
        } else {
            String metaValue = user.getCachedData().getMetaData().getMetaValue("lang.id");
            if (metaValue != null && !metaValue.isEmpty()) {
                plugin.setPlayerLanguage(p, metaValue);
            } else {
                plugin.setPlayerLanguage(p, "en");
            }
        }

		// Update checker
		if (plugin.needsUpdate()) {
			if (p.isOp()) {
				p.sendMessage(Toolkit.translate("&7[&b&lKIT-PVP&7] &aAn update was found: v" +
						plugin.getUpdateVersion() + " https://www.spigotmc.org/resources/27107/"));
			}
		}

		arena.getStats().createPlayer(p);

		if (Toolkit.inArena(p)) {
			if (config.getBoolean("Arena.ClearInventoryOnJoin")) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
			}

			arena.addPlayer(p, config.getBoolean("Arena.ToSpawnOnJoin"),
					config.getBoolean("Arena.GiveItemsOnJoin"));
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();

		if (Toolkit.inArena(p)) {
			if (config.getBoolean("Arena.ClearInventoryOnJoin")) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
			}
			
			arena.addPlayer(p, config.getBoolean("Arena.ToSpawnOnJoin"),
					config.getBoolean("Arena.GiveItemsOnJoin"));
		}
	}
	
}
