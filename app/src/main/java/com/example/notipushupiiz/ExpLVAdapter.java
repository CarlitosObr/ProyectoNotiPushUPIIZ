package com.example.notipushupiiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ExpLVAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String>> mapChild;
    private Context context;

    public ExpLVAdapter(Context context, ArrayList<String> listCategorias, Map<String, ArrayList<String>> mapChild){
        this.context = context;
        this.listCategorias = listCategorias;
        this.mapChild = mapChild;
    }

    @Override
    public int getGroupCount() {
        return listCategorias.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapChild.get(listCategorias.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCategorias.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapChild.get(listCategorias.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String tituloCategoria = (String) getGroup(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.elv_group, null);
        TextView tvgroup = convertView.findViewById(R.id.tvgroup);
        tvgroup.setText(tituloCategoria);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String item = (String) getChild(groupPosition,childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.elv_child, null);
        TextView tvchild = convertView.findViewById(R.id.tvchild);
        tvchild.setText(item);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
