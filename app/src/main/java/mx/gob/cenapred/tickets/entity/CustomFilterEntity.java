package mx.gob.cenapred.tickets.entity;

import java.util.List;

public class CustomFilterEntity {
    private String id;
    private String label;
    private List<CustomFilterItemEntity> itemList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<CustomFilterItemEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<CustomFilterItemEntity> itemList) {
        this.itemList = itemList;
    }
}
