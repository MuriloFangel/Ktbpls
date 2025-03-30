package dev.slickcollections.kiwizin.thebridge.cmd.tb;

import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import dev.slickcollections.kiwizin.thebridge.cosmetics.object.preview.KillEffectPreview;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PreviewCommand extends SubCommand {
  
  public PreviewCommand() {
    super("preview", "preview", "Setar as localizações das previsualizações.", true);
  }
  
  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eAjuda - Preview\n \n§6/tb preview killeffect [oponente/aliado/espectador] §f- §7Setar localizações de preview do Efeito de Abate.\n ");
      return;
    }
    
    String action = args[0];
    if (action.equalsIgnoreCase("killeffect")) {
      if (args.length < 2) {
        player.sendMessage("§cUtilize /tb preview killeffect [oponente/aliado/espectador]");
        return;
      }
      
      String type = args[1];
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      if (type.equalsIgnoreCase("oponente")) {
        KillEffectPreview.CONFIG.set("killeffect.1", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aLocalização do oponente setada!");
      } else if (type.equalsIgnoreCase("aliado")) {
        KillEffectPreview.CONFIG.set("killeffect.2", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aLocalização do aliado setada!");
      } else if (type.equalsIgnoreCase("espectador")) {
        KillEffectPreview.CONFIG.set("killeffect.3", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aLocalização do espectador setada!");
      } else {
        player.sendMessage("§cUtilize /tb preview killeffect [oponente/aliado/espectador]");
      }
    } else {
      player.sendMessage(" \n§eAjuda - Preview\n \n§6/tb preview killeffect [oponente/aliado/espectador] §f- §7Setar localizações de preview do Efeito de Abate.\n ");
    }
  }
}