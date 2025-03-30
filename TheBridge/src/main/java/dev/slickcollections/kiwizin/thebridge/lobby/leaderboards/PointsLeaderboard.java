package dev.slickcollections.kiwizin.thebridge.lobby.leaderboards;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.lobby.Leaderboard;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PointsLeaderboard extends Leaderboard {
  
  public PointsLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "abates";
  }
  
  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = Database.getInstance().getLeaderBoard("kCoreTheBridge", (this.getStatus() ?
        Collections.singletonList("monthlypoints") : Arrays.asList("2v2points", "1v1points")).toArray(new String[0]));
    while (list.size() < 10) {
      list.add(new String[]{Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$points$holograms;
  }
}