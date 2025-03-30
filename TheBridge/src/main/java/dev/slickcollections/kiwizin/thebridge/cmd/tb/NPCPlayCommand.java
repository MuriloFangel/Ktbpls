package dev.slickcollections.kiwizin.thebridge.cmd.tb;


import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeModeType;
import dev.slickcollections.kiwizin.thebridge.lobby.PlayNPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCPlayCommand extends SubCommand {
  
  public NPCPlayCommand() {
    super("npcjogar", "npcjogar", "Adicione/remova NPCs de Jogar.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eAjuda\n \n§6/tb npcjogar adicionar [id] [modo] §f- §7Adicionar NPC.\n§6/tb npcjogar remover [id] §f- §7Remover NPC.\n ");
      return;
    }
    
    String action = args[0];
    if (action.equalsIgnoreCase("adicionar")) {
      if (args.length <= 2) {
        player.sendMessage("§cUtilize /tb npcjogar adicionar [id] [casual/competivivo]");
        return;
      }
      
      String id = args[1];
      if (PlayNPC.getById(id) != null) {
        player.sendMessage("§cJá existe um NPC de Jogar utilizando \"" + id + "\" como ID.");
        return;
      }

      TheBridgeModeType mode = TheBridgeModeType.findByName(args[2]);
      if (mode == null) {
        player.sendMessage("§cUtilize /tb npcjogar adicionar [id] [casual/competivivo]");
        return;
      }
      
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      PlayNPC.add(id, location, mode);
      player.sendMessage("§aNPC de Jogar adicionado com sucesso.");
    } else if (action.equalsIgnoreCase("remover")) {
      if (args.length <= 1) {
        player.sendMessage("§cUtilize /tb npcjogar remover [id]");
        return;
      }
      
      String id = args[1];
      PlayNPC npc = PlayNPC.getById(id);
      if (npc == null) {
        player.sendMessage("§cNão existe um NPC de Jogar utilizando \"" + id + "\" como ID.");
        return;
      }
      
      PlayNPC.remove(npc);
      player.sendMessage("§cNPC de Jogar removido com sucesso.");
    } else {
      player.sendMessage(" \n§eAjuda - NPC Jogar\n \n§6/tb npcjogar adicionar [id] [modo] §f- §7Adicionar NPC.\n§6/tb npcjogar remover [id] §f- §7Remover NPC.\n ");
    }
  }
}
