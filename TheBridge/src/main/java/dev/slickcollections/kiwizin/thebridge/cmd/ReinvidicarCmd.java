package dev.slickcollections.kiwizin.thebridge.cmd;

import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.listeners.player.AsyncPlayerChatListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReinvidicarCmd extends Commands{

    public ReinvidicarCmd() {
        super("reinvidicar");
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
            if (args.length == 0) {
                player.sendMessage("§cUtilize /reinvidicar [code]");
                return;
            }

            String code = args[0];
            TheBridge game = AsyncPlayerChatListener.findGameByKey(code);
            if (game == null) {
                player.sendMessage("§cEste jogo não está mais em andamento.");
                return;
            }

            if (!game.getState().equals(GameState.EMJOGO)) {
                player.sendMessage("§cSó é possível encerrar um jogo em andamento.");
                return;
            }

            if (game.listPlayers().size() > 1) {
                player.sendMessage("§cSó é possível encerrar o jogo caso esteja só você.");
                return;
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), ()-> game.stop(game.getTeam(player)));
        }
    }

}
