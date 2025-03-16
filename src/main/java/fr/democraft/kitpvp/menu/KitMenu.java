package fr.democraft.kitpvp.menu;

import com.planetgallium.kitpvp.util.*;
import fr.democraft.kitpvp.util.Menu;
import fr.democraft.kitpvp.util.Resources;
import fr.democraft.kitpvp.util.Toolkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class KitMenu {

	private Menu menu;
	private final Resources resources;

	public KitMenu(Resources resources) {
		this.resources = resources;
		rebuildCache();
	}

	private void create() {
		this.menu = new Menu(resources.getMenu().fetchString("Menu.General.Title"), new KitHolder(), resources.getMenu().getInt("Menu.General.Size"));
		ConfigurationSection section = resources.getMenu().getConfigurationSection("Menu.Items");

		for (String slot : section.getKeys(false)) {
			String itemPath = "Menu.Items." + slot;

			String name = resources.getMenu().fetchString(itemPath + ".Name");
			Material material = Toolkit.safeMaterial(resources.getMenu().fetchString(itemPath + ".Material"));
			List<String> lore = resources.getMenu().getStringList(itemPath + ".Lore");

			menu.addItem(name, material, lore, Integer.parseInt(slot));
		}
	}

	public void rebuildCache() {
		create();
	}

	public void open(Player p) {
		this.menu.setResultHandler(response -> {
			String kit = response.clickedButton().text().substring(4);
			if (kit.endsWith(" Kit")) {
				kit = kit.substring(0, kit.length() - 4);
			}
			p.performCommand("kp kit " + kit);
		});
		menu.openMenu(p);
	}

}
