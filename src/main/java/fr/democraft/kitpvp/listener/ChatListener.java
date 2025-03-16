package fr.democraft.kitpvp.listener;

import fr.democraft.kitpvp.game.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.democraft.kitpvp.Game;
import fr.democraft.kitpvp.util.Resources;
import fr.democraft.kitpvp.util.Toolkit;

public class ChatListener implements Listener {

	private final Arena arena;
	private final Resources resources;
	
	public ChatListener(Game plugin) {
		this.arena = plugin.getArena();
		this.resources = plugin.getResources();
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (resources.getConfig().getBoolean("Chat.Enabled") && Toolkit.inArena(e.getPlayer())) {
			Player p = e.getPlayer();
			String levelPrefix = arena.getUtilities().getPlayerLevelPrefix(p.getName());

			String format = resources.getConfig().fetchString("Chat.Format")
					.replace("%player%", "%s")
					.replace("%message%", "%s")
					.replace("%level%", levelPrefix);

			e.setFormat(Toolkit.addPlaceholdersIfPossible(p, format));
		}
	}

}
