package dev.slickcollections.kiwizin.thebridge.cosmetics.object.winanimations;

import dev.slickcollections.kiwizin.thebridge.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.thebridge.nms.NMS;
import org.bukkit.entity.Player;

public class EnderDragonExecutor extends AbstractExecutor {
  
  public EnderDragonExecutor(Player player) {
    super(player);
    NMS.createMountableEnderDragon(player);
  }
}

