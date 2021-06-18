package de.daniel.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.utils.Config;
import de.daniel.GodmodeMain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GodmodeCommand extends Command implements Listener {

    private static Config cfg = new Config(new File(GodmodeMain.getInstance().getDataFolder(), "config.yml"));
    private static ArrayList<Player> godmodeMap = new ArrayList<>();

    public GodmodeCommand() {
        super("god", "Put yourself or another player in godmode", "/god <target>");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        String prefix = cfg.getString("prefix");

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only Ingame!");
                return false;
            }

            Player p = (Player) sender;

            if (!p.hasPermission("command.god")) {
                p.sendMessage(prefix + cfg.getString("messages.nopermission"));
                return false;
            }

            if (godmodeMap.contains(p)) {
                godmodeMap.remove(p);
                p.sendMessage(prefix + cfg.getString("messages.outgodself"));
                return true;
            }

            godmodeMap.add(p);
            p.sendMessage(prefix + cfg.getString("messages.ingodself"));
            return true;

        } else if (args.length == 1) {

            if (!sender.hasPermission("command.god.other")) {
                sender.sendMessage(prefix + cfg.getString("messages.nopermission"));
                return false;
            }

            Player target = Server.getInstance().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(prefix + cfg.getString("messages.notonline").replace("%player", target.getName()));
                return false;
            }

            if (godmodeMap.contains(target)) {
                godmodeMap.remove(target);
                target.sendMessage(prefix + cfg.getString("messages.outgodother").replace("%target", target.getName()));
                return true;
            }

            godmodeMap.add(target);
            target.sendMessage(prefix + cfg.getString("messages.ingodother").replace("%target", target.getName()));
            return true;

        } else {
            sender.sendMessage(prefix + usageMessage);
        }
        return false;
    }

    @EventHandler
    public void onGetDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();

        if (!godmodeMap.contains(p)) {
            return;
        }

        e.setCancelled();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!cfg.getBoolean("setonjoin")) {
            return;
        }

        godmodeMap.add(e.getPlayer());
    }
}
