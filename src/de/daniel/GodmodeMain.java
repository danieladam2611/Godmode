package de.daniel;

import cn.nukkit.plugin.PluginBase;
import de.daniel.Commands.GodmodeCommand;

public class GodmodeMain extends PluginBase {

    private static GodmodeMain instance;
    public static GodmodeMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveResource("config.yml");

        getServer().getPluginManager().registerEvents(new GodmodeCommand(), this);
        getServer().getCommandMap().register("god", new GodmodeCommand());

        getLogger().info("§b" + getName() + " §fwas successfully §aEnabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("§b" + getName() + " §fwas successfully §cDisabled");
    }
}
