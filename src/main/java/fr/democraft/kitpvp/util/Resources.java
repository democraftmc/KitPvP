package fr.democraft.kitpvp.util;

import java.io.File;
import java.util.*;

import fr.democraft.kitpvp.Game;
import fr.democraft.kitpvp.api.Ability;

public class Resources {

    private final Game plugin;
    private final Map<String, Resource> kitToResource;
    private final Map<String, Resource> abilityToResource;
    private final Map<String, Resource> messageResources;

    private final Resource config, abilities, killstreaks,
            levels, menu, scoreboard, signs;

    public final String defaultLang = "en";

    public Resources(Game plugin) {
        this.plugin = plugin;
        this.kitToResource = new HashMap<>();
        this.abilityToResource = new HashMap<>();
        this.messageResources = new HashMap<>();

        Toolkit.printToConsole("&7[&b&lKIT-PVP&7] &7Loading configuration files...");
        this.config = new Resource(plugin, "config.yml");
        this.abilities = new Resource(plugin, "abilities.yml");
        this.killstreaks = new Resource(plugin, "killstreaks.yml");
        this.levels = new Resource(plugin, "levels.yml");
        this.menu = new Resource(plugin, "menu.yml");
        this.scoreboard = new Resource(plugin, "scoreboard.yml");
        this.signs = new Resource(plugin, "signs.yml");

        if (!plugin.getDataFolder().exists()) {
            kitToResource.put("Fighter.yml", new Resource(plugin, "kits/Fighter.yml"));
            kitToResource.put("Archer.yml", new Resource(plugin, "kits/Archer.yml"));
            kitToResource.put("Tank.yml", new Resource(plugin, "kits/Tank.yml"));
            kitToResource.put("Soldier.yml", new Resource(plugin, "kits/Soldier.yml"));
            kitToResource.put("Bomber.yml", new Resource(plugin, "kits/Bomber.yml"));
            kitToResource.put("Kangaroo.yml", new Resource(plugin, "kits/Kangaroo.yml"));
            kitToResource.put("Warper.yml", new Resource(plugin, "kits/Warper.yml"));
            kitToResource.put("Witch.yml", new Resource(plugin, "kits/Witch.yml"));
            kitToResource.put("Ninja.yml", new Resource(plugin, "kits/Ninja.yml"));
            kitToResource.put("Thunderbolt.yml", new Resource(plugin, "kits/Thunderbolt.yml"));
            kitToResource.put("Vampire.yml", new Resource(plugin, "kits/Vampire.yml"));
            kitToResource.put("Rhino.yml", new Resource(plugin, "kits/Rhino.yml"));
            kitToResource.put("Example.yml", new Resource(plugin, "kits/Example.yml"));
            kitToResource.put("Trickster.yml", new Resource(plugin, "kits/Trickster.yml"));

            abilityToResource.put("HealthPack.yml", new Resource(plugin, "abilities/HealthPack.yml"));
            abilityToResource.put("ExampleAbility.yml", new Resource(plugin, "abilities/ExampleAbility.yml"));
            abilityToResource.put("ExampleAbility2.yml", new Resource(plugin, "abilities/ExampleAbility2.yml"));
            abilityToResource.put("SpeedBoost.yml", new Resource(plugin, "abilities/SpeedBoost.yml"));
            abilityToResource.put("Stampede.yml", new Resource(plugin, "abilities/Stampede.yml"));

            // Create message files from resources/messages if not existing
            File messagesFolder = new File(plugin.getDataFolder(), "messages");
            if (!messagesFolder.exists()) {
                messagesFolder.mkdirs();
            }
            File resourceMessagesFolder = new File(plugin.getClass().getClassLoader().getResource("messages").getFile());
            if (resourceMessagesFolder.exists() && resourceMessagesFolder.isDirectory()) {
                for (String fileName : resourceMessagesFolder.list()) {
                    File dest = new File(messagesFolder, fileName);
                    if (!dest.exists()) {
                        // Copy file from resources to plugin data folder
                        try (java.io.InputStream in = plugin.getClass().getClassLoader().getResourceAsStream("messages/" + fileName);
                             java.io.FileOutputStream out = new java.io.FileOutputStream(dest)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        } catch (Exception e) {
                            Toolkit.printToConsole("&cFailed to copy message file: " + fileName + " - " + e.getMessage());
                        }
                    }
                }
            }
        }

        Toolkit.printToConsole("&7[&b&lKIT-PVP&7] &7Loading kit files...");
        load();
    }

    public void load() {
        config.load();
        abilities.load();
        killstreaks.load();
        levels.load();
        menu.load();
        scoreboard.load();
        signs.load();

        menu.addCopyDefaultExemption("Menu.Items");
        menu.copyDefaults();

        levels.addCopyDefaultExemption("Levels.Levels.10.Commands");
        levels.addCopyDefaultExemption("Levels.Levels.10.Experience-To-Level-Up");
        levels.copyDefaults();

        scoreboard.addCopyDefaultExemption("Scoreboard.Lines");
        scoreboard.copyDefaults();

        config.addCopyDefaultExemption("Items.Kits.Commands");
        config.addCopyDefaultExemption("Items.Leave.Commands");
        config.copyDefaults();

        abilities.copyDefaults();
        signs.copyDefaults();

        // load new kits & abilities that have been added through file system (when doing /kp reload)
        for (String fileName : getPluginDirectoryFiles("kits", true)) {
            if (!kitToResource.containsKey(fileName) && !fileName.startsWith(".")) {
                kitToResource.put(fileName, new Resource(plugin, "kits/" + fileName));
            }
        }

        for (String fileName : getPluginDirectoryFiles("abilities", true)) {
            if (!abilityToResource.containsKey(fileName) && !fileName.startsWith(".")) {
                abilityToResource.put(fileName, new Resource(plugin, "abilities/" + fileName));
            }
        }

        // Reload all kitName.yml, abilityName.yml
        kitToResource.values().forEach(Resource::load);
        abilityToResource.values().forEach(Resource::load);

        // Load message files (like fr.yml, en.yml, etc.)
        messageResources.clear();
        for (String fileName : getPluginDirectoryFiles("messages", true)) {
            if (fileName.endsWith(".yml") && !fileName.startsWith(".")) {
                String id = fileName.replace(".yml", ""); // ex: fr.yml -> fr
                Resource res = new Resource(plugin, "messages/" + fileName);
                res.load();
                messageResources.put(id, res);
            }
        }
    }

    public void reload() {
        load();
    }

    public void addResource(String fileName, Resource resource) {
        kitToResource.put(fileName, resource);
        kitToResource.get(fileName).load();
    }

    public void removeResource(String fileName) {
        kitToResource.get(fileName).getFile().delete();
        kitToResource.remove(fileName);
    }

    public void addAbilityResource(Ability ability) {
        String abilityName = ability.getName();
        Resource abilityResource = new Resource(plugin, "abilities/" + abilityName + ".yml");
        ability.toResource(abilityResource);

        abilityToResource.put(abilityName, abilityResource);
        abilityToResource.get(abilityName).load();
    }

    public Resource getKit(String kitName) {
        if (kitToResource.containsKey(kitName + ".yml")) {
            return kitToResource.get(kitName + ".yml");
        }
        return null;
    }

    public List<String> getPluginDirectoryFiles(String directoryName, boolean withFileEndings) {
        File folder = new File(plugin.getDataFolder().getAbsolutePath() + "/" + directoryName);
        List<String> fileList = new ArrayList<>();

        if (folder.exists() && folder.list() != null) {
            for (String fileName : folder.list()) {
                fileList.add(withFileEndings ? fileName : fileName.split(".yml")[0]);
            }
        }
        return fileList;
    }

    public Resource getConfig() { return config; }

    public Resource getAbilities() { return abilities; }

    public Resource getKillStreaks() { return killstreaks; }

    public Resource getLevels() { return levels; }

    public Resource getMenu() { return menu; }

    public Resource getScoreboard() { return scoreboard; }

    public Resource getSigns() { return signs; }

    public Collection<Resource> getAbilityResources() { return abilityToResource.values(); }

    public Collection<Resource> getLangRessources() {
        return messageResources.values();
    }

    public Resource getMessageFile(String langId) {
        return messageResources.get(langId);
    }


    public Resource getMessages() {
        return messageResources.get(defaultLang);
    }

    public Resource getMessages(String langId) {
        return messageResources.get(langId);
    }
}