package fr.democraft.kitpvp.menu;

import fr.democraft.kitpvp.util.Menu;
import fr.democraft.kitpvp.util.Resources;
import fr.democraft.kitpvp.util.Toolkit;
import org.bukkit.entity.Player;

public class RefillMenu {

    private final Resources resources;
    private Menu menu;

    public RefillMenu(Resources resources) {
        this.resources = resources;
    }

    private void create() {
        this.menu = new Menu(resources.getMessages().fetchString("Messages.Other.RefillMenuTitle"), resources.getMessages().fetchString("Messages.Other.RefillMenuBedrockTitle"), null, 54);

        for (int i = 0; i < menu.getSize(); i++) {
            menu.addItem(resources.getConfig().fetchString("Soups.Name"),
                    Toolkit.safeMaterial("MUSHROOM_STEW"),
                    resources.getConfig().getStringList("Soups.Lore"), i);
        }
    }

    public void open(Player p) {
        create();
        menu.openMenu(p);
    }

}
