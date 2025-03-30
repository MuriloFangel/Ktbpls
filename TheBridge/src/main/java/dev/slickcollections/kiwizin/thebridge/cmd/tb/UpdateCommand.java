package dev.slickcollections.kiwizin.thebridge.cmd.tb;

import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import org.bukkit.entity.Player;

public class UpdateCommand extends SubCommand {
  
  public UpdateCommand() {
    super("atualizar", "atualizar", "Atualizar o kTheBridge.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    player.sendMessage("§aO plugin já se encontra em sua última versão.");
  }
  
}
