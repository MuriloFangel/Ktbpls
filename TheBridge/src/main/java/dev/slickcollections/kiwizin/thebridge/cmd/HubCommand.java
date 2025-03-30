package dev.slickcollections.kiwizin.thebridge.cmd;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.servers.ServerItem;
import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand extends Commands {

    public HubCommand() {
        super("hub");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cComando exclusivo para jogadores");
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            TheBridge game = profile.getGame(TheBridge.class);
            if (game != null) {
                game.leave(profile, null);
            }

            CacheManager.removeProtectionPlayer(player.getName());
            player.teleport(new Location(Bukkit.getWorld("hub"), 0, 100,  0, 90, 0));
            player.sendMessage("§aConectando...");
        }
    }

}
