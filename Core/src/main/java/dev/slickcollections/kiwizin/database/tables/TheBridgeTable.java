package dev.slickcollections.kiwizin.database.tables;

import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.DataTable;
import dev.slickcollections.kiwizin.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(name = "kCoreTheBridge",
    create = "CREATE TABLE IF NOT EXISTS `kCoreTheBridge` (`name` VARCHAR(32), `1v1kills` LONG, `1v1deaths` LONG, `1v1games` LONG, `1v1points` LONG, `1v1wins` LONG, `4v4kills` LONG, `4v4deaths` LONG, `4v4games` LONG, `4v4points` LONG, `4v4wins` LONG, `3v3kills` LONG, `3v3deaths` LONG, `3v3games` LONG, `3v3points` LONG, `3v3wins` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2games` LONG, `2v2points` LONG, `2v2wins` LONG, `comp1v1kills` LONG, `comp1v1deaths` LONG, `comp1v1games` LONG, `comp1v1points` LONG, `comp1v1wins` LONG, `comp4v4kills` LONG, `comp4v4deaths` LONG, `comp4v4games` LONG, `comp4v4points` LONG, `comp4v4wins` LONG, `comp3v3kills` LONG, `comp3v3deaths` LONG, `comp3v3games` LONG, `comp3v3points` LONG, `comp3v3wins` LONG, `comp2v2kills` LONG, `comp2v2deaths` LONG, `comp2v2games` LONG, `comp2v2points` LONG, `comp2v2wins` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlypoints` LONG, `monthlywins` LONG, `monthlygames` LONG, `month` TEXT, `coins` DOUBLE, `winstreak` LONG, `laststreak` LONG, `lastmap` LONG, `hotbar` TEXT, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `kCoreTheBridge` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `kCoreTheBridge` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `kCoreTheBridge` SET `1v1kills` = ?, `1v1deaths` = ?, `1v1games` = ?, `1v1points` = ?, `1v1wins` = ?, `3v3kills` = ?, `3v3deaths` = ?, `3v3games` = ?, `3v3points` = ?, `3v3wins` = ?, `4v4kills` = ?, `4v4deaths` = ?, `4v4games` = ?, `4v4points` = ?, `4v4wins` = ?, `2v2kills` = ?, `2v2deaths` = ?, `2v2games` = ?, `2v2points` = ?, `2v2wins` = ?, `comp1v1kills` = ?, `comp1v1deaths` = ?, `comp1v1games` = ?, `comp1v1points` = ?, `comp1v1wins` = ?, `comp3v3kills` = ?, `comp3v3deaths` = ?, `comp3v3games` = ?, `comp3v3points` = ?, `comp3v3wins` = ?, `comp4v4kills` = ?, `comp4v4deaths` = ?, `comp4v4games` = ?, `comp4v4points` = ?, `comp4v4wins` = ?, `comp2v2kills` = ?, `comp2v2deaths` = ?, `comp2v2games` = ?, `comp2v2points` = ?, `comp2v2wins` = ?, `monthlykills` = ?, `montlhydeaths` = ?, `monthlypoints` = ?, `monthlywins` = ?, `monthlygames` = ?, `month` = ?, `coins` = ?, `winstreak` = ?, `laststreak` = ?, `lastmap` = ?, `hotbar` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?")
public class TheBridgeTable extends DataTable {
  
  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreTheBridge` LIKE 'hotbar'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `kCoreTheBridge` ADD `hotbar` TEXT AFTER `lastmap`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreTheBridge` LIKE 'hotbar'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `kCoreTheBridge` ADD `hotbar` TEXT AFTER `lastmap`");
      }
    }
  }
  
  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    for (String key : new String[]{"1v1", "2v2", "3v3", "4v4", "comp1v1", "comp2v2", "comp3v3", "comp4v4"}) {
      defaultValues.put(key + "kills", new DataContainer(0L));
      defaultValues.put(key + "deaths", new DataContainer(0L));
      defaultValues.put(key + "games", new DataContainer(0L));
      defaultValues.put(key + "points", new DataContainer(0L));
      defaultValues.put(key + "wins", new DataContainer(0L));
    }
    for (String key : new String[]{"kills", "deaths", "points", "wins", "games"}) {
      defaultValues.put("monthly" + key, new DataContainer(0L));
    }
    defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
        Calendar.getInstance().get(Calendar.YEAR)));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("winstreak", new DataContainer(0L));
    defaultValues.put("laststreak", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("hotbar", new DataContainer("{}"));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    return defaultValues;
  }
}
