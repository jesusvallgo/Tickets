package mx.gob.cenapred.tickets.entity;

import java.util.ArrayList;
import java.util.List;

public class CustomFilterEntity {
    private String filter;
    private List<CustomFilterItemEntity> itemList;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<CustomFilterItemEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<CustomFilterItemEntity> itemList) {
        this.itemList = itemList;
    }
}
