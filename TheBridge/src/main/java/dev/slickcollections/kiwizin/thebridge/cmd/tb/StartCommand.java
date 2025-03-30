package dev.slickcollections.kiwizin.thebridge.cmd.tb;

import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.entity.Player;

public class StartCommand extends SubCommand {
  
  public StartCommand() {
    super("iniciar", "iniciar", "Iniciar a partida.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      TheBridge game = profile.getGame(TheBridge.class);
      if (game != null) {
        if (game.getState() == GameState.AGUARDANDO) {
          game.start();
          player.sendMessage("§aVocê iniciou a partida!");
        } else {
          player.sendMessage("§cA partida já está em andamento.");
        }
      }
    }
  }
}
