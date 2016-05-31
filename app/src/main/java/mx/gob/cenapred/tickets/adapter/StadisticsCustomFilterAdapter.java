package mx.gob.cenapred.tickets.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.CustomFilterEntity;
import mx.gob.cenapred.tickets.entity.CustomFilterItemEntity;

public class StadisticsCustomFilterAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private CustomFilterEntity customFilterEntity;
    private CustomFilterItemEntity customFilterItemEntity;

    // Contenedor de filtros
    private List<CustomFilterEntity> filters = new ArrayList<>();

    private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };

    private String[][] children = {
            { "Arnold", "Barry", "Chuck", "David" },
            { "Ace", "Bandit", "Cha-Cha", "Deuce" },
            { "Fluffy", "Snuggles" },
            { "Goldy", "Bubbles" }
    };

    public StadisticsCustomFilterAdapter(Activity activity){
        this.activity = activity;

        // Contenedor de items para filtro por fecha
        List<CustomFilterItemEntity> dateItems = new ArrayList<>();

        // Generando un nuevo item del filtro
        customFilterItemEntity = new CustomFilterItemEntity();
        customFilterItemEntity.setType("DatePicker");
        customFilterItemEntity.setLabel("Fecha inicial");
        dateItems.add(customFilterItemEntity);
        // Generando un nuevo item del filtro
        customFilterItemEntity = new CustomFilterItemEntity();
        customFilterItemEntity.setType("DatePicker");
        customFilterItemEntity.setLabel("Fecha final");
        dateItems.add(customFilterItemEntity);

        // Generando un nuevo filtro
        customFilterEntity = new CustomFilterEntity();
        customFilterEntity.setFilter("Fechas");
        customFilterEntity.setItemList(dateItems);

        // Agregando el filtro al contenedor de filtros
        filters.add(customFilterEntity);

        // Contenedor de items para filtro por Area de Atencion
        List<CustomFilterItemEntity> attentionAreaItems = new ArrayList<>();

        // Generando un nuevo item del filtro
        customFilterItemEntity = new CustomFilterItemEntity();
        customFilterItemEntity.setType("CheckBox");
        customFilterItemEntity.setLabel("Soporte Técnico");
        attentionAreaItems.add(customFilterItemEntity);
        // Generando un nuevo item del filtro
        customFilterItemEntity = new CustomFilterItemEntity();
        customFilterItemEntity.setType("CheckBox");
        customFilterItemEntity.setLabel("Desarrollo");
        attentionAreaItems.add(customFilterItemEntity);
        // Generando un nuevo item del filtro
        customFilterItemEntity = new CustomFilterItemEntity();
        customFilterItemEntity.setType("CheckBox");
        customFilterItemEntity.setLabel("Redes y Enlaces");
        attentionAreaItems.add(customFilterItemEntity);

        // Generando un nuevo filtro
        customFilterEntity = new CustomFilterEntity();
        customFilterEntity.setFilter("Área de Atención");
        customFilterEntity.setItemList(attentionAreaItems);

        // Agregando el filtro al contenedor de filtros
        filters.add(customFilterEntity);
    }

    @Override
    public int getGroupCount() {
        return filters.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filters.get(groupPosition).getItemList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filters.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filters.get(groupPosition).getItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        /*
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_custom_stadistics_explsv_group, null);
            TextView labelFilter = (TextView) convertView.findViewById(R.id.group_name);
            labelFilter.setText(getGroup(groupPosition).toString());
        }
        return convertView;
        */
        if (convertView == null) {
            CustomFilterEntity filter = (CustomFilterEntity) getGroup(groupPosition);
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_custom_stadistics_explsv_group, null);
            TextView labelFilter = (TextView) convertView.findViewById(R.id.group_name);
            labelFilter.setText(filter.getFilter());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            Integer layout = 0;

            CustomFilterItemEntity item = (CustomFilterItemEntity) getChild(groupPosition, childPosition);
            switch (item.getType()) {
                case "DatePicker":
                    layout = R.layout.layout_custom_stadistics_explsv_item_dtpkr;
                    break;
                case "CheckBox":
                    layout = R.layout.layout_custom_stadistics_explsv_item_dtpkr;
                    break;
            }

            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View childView = infalInflater.inflate(layout, null);

            TextView labelChild = (TextView) childView.findViewById(R.id.item_name);
            labelChild.setText(item.getLabel());

        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
