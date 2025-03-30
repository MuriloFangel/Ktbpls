package dev.slickcollections.kiwizin.thebridge.lobby.leaderboards;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.lobby.Leaderboard;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WinsLeaderboard extends Leaderboard {
  
  public WinsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$wins$holograms;
  }
  
  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = Database.getInstance().getLeaderBoard("kCoreTheBridge", (this.getStatus() ?
        Collections.singletonList("monthlywins") : Arrays.asList("1v1wins", "2v2wins")).toArray(new String[0]));
    while (list.size() < 10) {
      list.add(new String[]{Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }
  
  
  @Override
  public String getType() {
    return "vitorias";
  }
}