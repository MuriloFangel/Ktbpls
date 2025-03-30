package dev.slickcollections.kiwizin.thebridge.game;

import dev.slickcollections.kiwizin.game.GameState;
import org.bukkit.scheduler.BukkitRunnable;

import static dev.slickcollections.kiwizin.thebridge.game.TheBridge.QUEUE;

public class ArenaRollbackerTask extends BukkitRunnable {
  
  private TheBridge rollbacking = null;
  private boolean locked;
  
  @Override
  public void run() {
    if (this.locked) {
      return;
    }
    
    if (rollbacking != null) {
      this.locked = true;
      this.rollbacking.getConfig().reload(() -> {
        this.rollbacking.setState(GameState.AGUARDANDO);
        this.rollbacking.setTimer(11);
        this.rollbacking.getTask().reset();
        this.rollbacking = null;
        this.locked = false;
      });
      return;
    }
    
    if (!QUEUE.isEmpty()) {
      rollbacking = QUEUE.get(0);
      QUEUE.remove(0);
    }
  }
}