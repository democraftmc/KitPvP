package fr.democraft.kitpvp.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.function.Consumer;

public class Menu {

	private final String title;
	private final int size;
	private final Inventory menu;
	private final InventoryHolder owner;
	public final SimpleForm.@NonNull Builder form;
	private final FloodgateApi api = FloodgateApi.getInstance();
	
	public Menu(String title, InventoryHolder owner, int size) {
		this.menu = Bukkit.createInventory(owner, size, Toolkit.translate(title));
		this.form = SimpleForm.builder().title(Toolkit.translate(title));
		this.title = title;
		this.size = size;
		this.owner = owner;
	}
	
	public void addItem(String name, Material material, List<String> lore, int slot) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		lore = Toolkit.colorizeList(lore);
		
		meta.setDisplayName(Toolkit.translate(name));
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		menu.setItem(slot, item);
		this.form.button(name);
	}
	
	public void addItem(String name, Material material, List<String> lore, int amount, int slot) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		lore = Toolkit.colorizeList(lore);

		meta.setDisplayName(Toolkit.translate(name));
		meta.setLore(lore);
		item.setAmount(amount > 0 ? amount : 1);
		item.setItemMeta(meta);
		
		menu.setItem(slot, item);
	}

	public void setItem(ItemStack item, int slot) {
		menu.setItem(slot, item);
	}
	
	public void openMenu(Player p) {
		FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance()
				.getPlayer(p.getUniqueId());
		if (floodgatePlayer == null) {
			p.openInventory(menu);
		} else {
			floodgatePlayer.sendForm(form.build());
		}
	}
	
	public void closeMenu(Player p) {
		p.closeInventory();
	}
	
	public ItemStack getSlot(int slot) { return menu.getItem(slot); }
	
	public String getTitle() { return title; }
	
	public InventoryHolder getOwner() { return owner; }
	
	public int getSize() { return size; }

	public void setResultHandler(Consumer<SimpleFormResponse> function) {
		this.form.responseHandler((simpleForm, rawData) -> {
			SimpleFormResponse response = simpleForm.parseResponse(rawData);
			if (!response.isCorrect())
				return;
			function.accept(response); // Call the function passed as an argument
		});
	}

}
