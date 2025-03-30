package dev.slickcollections.kiwizin.thebridge.cmd;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.container.RejoinContainer;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RejoinCmd extends Commands {

    public RejoinCmd() {
        super("rejoin");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando é exclusivo para jogadores!");
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            if (profile.getGame() != null) {
                player.sendMessage("§cNão é possível reconectar a uma partida estando em uma.");
                return;
            }

            RejoinContainer container = profile.getAbstractContainer("kCoreTheBridge", "rejoin", RejoinContainer.class);
            TheBridge game = container.getRejoinGame(player);
            if (game == null) {
                player.sendMessage("§cNenhum jogo disponível para se reconectar.");
                return;
            }

            player.sendMessage("§aConectando...");
            game.rejoin(profile);
        }
    }

}
