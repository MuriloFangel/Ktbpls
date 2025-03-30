package dev.slickcollections.kiwizin.thebridge.cmd.tb;

import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class CloneCommand extends SubCommand {
  
  public CloneCommand() {
    super("clonar", "clonar [mundo] [novoMundo]", "Clonar uma sala.", false);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      sender.sendMessage("§cUtilize /tb " + this.getUsage());
      return;
    }
    
    TheBridge game = TheBridge.getByWorldName(args[0]);
    if (game == null) {
      sender.sendMessage("§cNão existe uma sala neste mundo.");
      return;
    }
    
    String newWorld = args[1];
    if (TheBridge.getByWorldName(newWorld) != null) {
      sender.sendMessage("§cJá existe uma sala no mundo \"" + newWorld + "\".");
      return;
    }
    
    sender.sendMessage("§aRealizando processo de clonação...");
    KConfig config = Main.getInstance().getConfig("arenas", newWorld);
    for (String key : game.getConfig().getConfig().getKeys(false)) {
      Object value = game.getConfig().getConfig().get(key);
      if (value instanceof ConfigurationSection) {
        for (String key2 : ((ConfigurationSection) value).getKeys(false)) {
          Object value2 = ((ConfigurationSection) value).get(key2);
          if (value2 instanceof String) {
            value2 = ((String) value2).replace(game.getGameName(), newWorld);
          }
          config.set(key + "." + key2, value2);
        }
      } else {
        if (value instanceof String) {
          value = ((String) value).replace(game.getGameName(), newWorld);
        }
        config.set(key, value);
      }
    }
    
    Main.getInstance().getFileUtils().copyFiles(new File("plugins/kTheBridge/mundos", args[0]), new File("plugins/kTheBridge/mundos", newWorld));
    TheBridge.load(config.getFile(), () -> sender.sendMessage("§aA sala foi clonada."));
  }
}