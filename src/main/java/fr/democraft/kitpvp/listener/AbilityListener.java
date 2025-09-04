package fr.democraft.kitpvp.listener;

import fr.democraft.kitpvp.Game;
import fr.democraft.kitpvp.api.Ability;
import fr.democraft.kitpvp.util.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import fr.democraft.kitpvp.api.PlayerAbilityEvent;
import fr.democraft.kitpvp.game.Arena;
import fr.democraft.kitpvp.util.Resources;
import fr.democraft.kitpvp.util.Toolkit;

public class AbilityListener implements Listener {

	private final Arena arena;
	private final Resources resources;
    private final Game plugin;
	
	public AbilityListener(Game plugin) {
		this.arena = plugin.getArena();
		this.resources = plugin.getResources();
        this.plugin = plugin;
	}
	
	@EventHandler
	public void onAbility(PlayerAbilityEvent e) {
		Player p = e.getPlayer();

		if (!arena.getUtilities().isCombatActionPermittedInRegion(p)) {
			return;
		}

		Ability ability = e.getAbility();

		String abilityPermission = "kp.ability." + ability.getName().toLowerCase();
		if (!p.hasPermission(abilityPermission)) {
			p.sendMessage(resources.getMessages(plugin.getPlayerLanguage(p)).fetchString("Messages.General.Permission")
					.replace("%permission%", abilityPermission));
			return;
		}

		Cooldown cooldownRemaining = arena.getCooldowns().getRemainingCooldown(p, ability);
		if (cooldownRemaining.toSeconds() > 0) {
			p.sendMessage(resources.getMessages(plugin.getPlayerLanguage(p)).fetchString("Messages.Error.CooldownAbility")
					.replace("%cooldown%", cooldownRemaining.formatted(false)));
			return;
		}

		if (ability.getMessage() != null)
			p.sendMessage(Toolkit.translate(ability.getMessage()));

		if (ability.getSound() != null)
			p.playSound(p.getLocation(), ability.getSound(), ability.getSoundVolume(), ability.getSoundPitch());

		if (ability.getEffects().size() > 0)
			ability.getEffects().forEach(p::addPotionEffect);

		if (ability.getCommands().size() > 0)
			Toolkit.runCommands(p, ability.getCommands(), "none", "none");

		if (ability.getCooldown() == null) {
			ItemStack abilityItem = Toolkit.getHandItemForInteraction(e.getOriginalInteractionEvent());
			abilityItem.setAmount(abilityItem.getAmount() - 1);
		} else {
			arena.getCooldowns().setAbilityCooldown(p.getName(), ability.getName());
		}
	}

}
