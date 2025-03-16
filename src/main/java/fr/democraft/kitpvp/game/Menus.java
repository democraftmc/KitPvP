package fr.democraft.kitpvp.game;

import fr.democraft.kitpvp.menu.KitMenu;
import fr.democraft.kitpvp.menu.PreviewMenu;
import fr.democraft.kitpvp.menu.RefillMenu;
import fr.democraft.kitpvp.util.Resources;

public class Menus {

    private final KitMenu kitMenu;
    private final PreviewMenu previewMenu;
    private final RefillMenu refillMenu;

    public Menus(Resources resources) {
        this.kitMenu = new KitMenu(resources);
        this.previewMenu = new PreviewMenu();
        this.refillMenu = new RefillMenu(resources);
    }

    public KitMenu getKitMenu() { return kitMenu; }

    public PreviewMenu getPreviewMenu() { return previewMenu; }

    public RefillMenu getRefillMenu() { return refillMenu; }

}
