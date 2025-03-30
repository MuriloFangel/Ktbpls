package dev.slickcollections.kiwizin.thebridge.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.thebridge.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.thebridge.cosmetics.CosmeticType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class CosmeticsContainer extends AbstractContainer {
  
  public CosmeticsContainer(DataContainer dataContainer) {
    super(dataContainer);
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    if (!cosmetics.containsKey("KILL_EFFECT")) {
      for (CosmeticType type : CosmeticType.values()) {
        cosmetics.put(type.name(), new JSONArray());
      }
    }
    
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public void addCosmetic(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    ((JSONArray) cosmetics.get(cosmetic.getType().name())).add(cosmetic.getId());
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public boolean hasCosmetic(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    boolean has = ((JSONArray) cosmetics.get(cosmetic.getType().name())).contains(cosmetic.getId());
    cosmetics.clear();
    return has;
  }
}