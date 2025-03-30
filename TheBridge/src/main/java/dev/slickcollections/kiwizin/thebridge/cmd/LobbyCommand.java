package dev.slickcollections.kiwizin.thebridge.cmd;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand extends Commands {

    public LobbyCommand() {
        super("lobby");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = Profile.getProfile(player.getName());
            if (profile != null) {
                TheBridge game = profile.getGame(TheBridge.class);
                if (game != null) {
                    game.leave(profile, null);
                    game.disconnectPlayer(player);
                    return;
                }

                if (CacheManager.getCache(player.getName(), RemoveProtectionCache.class) != null) {
                    profile.refresh();
                    CacheManager.clearCache(player.getName(), RemoveProtectionCache.class);
                }

                player.sendMessage("§aConectando...");
            }
        }
    }
}
