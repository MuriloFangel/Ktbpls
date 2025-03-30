package dev.slickcollections.kiwizin.thebridge.container;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.database.data.interfaces.AbstractContainer;
import dev.slickcollections.kiwizin.thebridge.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.thebridge.cosmetics.CosmeticType;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class SelectedContainer extends AbstractContainer {
  
  public SelectedContainer(DataContainer dataContainer) {
    super(dataContainer);
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    if (!cosmetics.containsKey("KILL_EFFECT")) {
      for (CosmeticType type : CosmeticType.values()) {
        cosmetics.put(type.name(), 0L);
      }
    }
    
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public void setSelected(Cosmetic cosmetic) {
    this.setSelected(cosmetic.getType(), cosmetic.getId());
  }
  
  public void setSelected(CosmeticType type, long id) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    cosmetics.put(type.name(), id);
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public boolean isSelected(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    boolean selected = cosmetics.get(cosmetic.getType().name()).equals(cosmetic.getId());
    cosmetics.clear();
    return selected;
  }
  
  public <T extends Cosmetic> T getSelected(CosmeticType type, Class<T> cosmeticClass) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    T cosmetic = Cosmetic.findById(cosmeticClass, (long) cosmetics.get(type.name()));
    cosmetics.clear();
    return cosmetic;
  }
}
