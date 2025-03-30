package dev.slickcollections.kiwizin.thebridge.lobby.leaderboards;

import com.mongodb.client.MongoCursor;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MongoDBDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.lobby.Leaderboard;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bson.Document;
import org.bukkit.Location;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class WinstreakLeaderboard extends Leaderboard {
  
  public WinstreakLeaderboard(Location location, String id) {
    super(location, id);
  }
  
  @Override
  public String getType() {
    return "winstreak";
  }
  
  @Override
  public List<String[]> getSplitted() {
    List<String[]> list = new ArrayList<>();
    if (Database.getInstance() instanceof MongoDBDatabase) {
      MongoDBDatabase database = (MongoDBDatabase) Database.getInstance();
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      long dayInit = calendar.getTimeInMillis();
      calendar.set(Calendar.HOUR, 23);
      calendar.set(Calendar.MINUTE, 59);
      calendar.set(Calendar.SECOND, 59);
      long dayFinish = calendar.getTimeInMillis();
      try {
        MongoCursor<Document> cursor = database.getExecutor().submit(
            () -> database.getCollection().find(new Document("kCoreTheBridge.laststreak", new Document("$gte", dayInit).append("$lte", dayFinish)))
                .projection(fields(include("_id", "role", "kCoreTheBridge.winstreak"))).sort(new Document("kCoreTheBridge.winstreak", -1)).limit(10).cursor()).get();
        while (cursor.hasNext()) {
          Document document = cursor.next();
          list.add(new String[] {StringUtils.getLastColor(Role.getRoleByName(document.getString("role")).getPrefix()) + document.getString("_id"),
              StringUtils.formatNumber(document.get("kCoreTheBridge", Document.class).getLong("winstreak"))});
        }
        cursor.close();
      } catch (Exception ignore) {}
    } else {
      CachedRowSet rs;
      if (Database.getInstance() instanceof MySQLDatabase) {
        rs = ((MySQLDatabase) Database.getInstance()).query(
            "SELECT `name`, `winstreak` FROM `kCoreTheBridge` WHERE DATE(DATE_ADD('1970-1-1', INTERVAL `laststreak` / 1000 SECOND)) = curdate() ORDER BY `winstreak` + 0 DESC LIMIT 10");
      } else {
        rs = ((HikariDatabase) Database.getInstance()).query(
            "SELECT `name`, `winstreak` FROM `kCoreTheBridge` WHERE DATE(DATE_ADD('1970-1-1', INTERVAL `laststreak` / 1000 SECOND)) = curdate() ORDER BY `winstreak` + 0 DESC LIMIT 10");
      }
      try {
        if (rs != null) {
          rs.beforeFirst();
          while (rs.next()) {
            list.add(new String[] {Role.getColored(rs.getString(1), true), StringUtils.formatNumber(rs.getLong(2))});
          }
        }
      } catch (SQLException ignore) {} finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (SQLException ignore) {}
        }
      }
    }
    
    while (list.size() < 10) {
      list.add(new String[] {Language.lobby$leaderboard$empty, "0"});
    }
    return list;
  }
  
  @Override
  public List<String> getHologramLines() {
    return Language.lobby$leaderboard$daily_winstreak$holograms;
  }
}