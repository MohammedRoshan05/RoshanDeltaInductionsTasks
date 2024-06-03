package com.example.color_conquest_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class CustomGridAdapter extends ArrayAdapter<Cells>{
    ArrayList<Cells> cellsArrayList;
    Context context;
    private int circle_clr[];
    private int tile_clr[];

    public CustomGridAdapter(ArrayList<Cells> cellsArrayList, Context context) {
        super(context,R.layout.grid_item,cellsArrayList);
        this.cellsArrayList = cellsArrayList;
        this.context = context;
        this.circle_clr = new int[cellsArrayList.size()];
        this.tile_clr = new int[cellsArrayList.size()];
        for(int i = 0; i < circle_clr.length;i++){
            circle_clr[i] = R.drawable.rounded_corner;
            tile_clr[i] = R.drawable.square_corner;
        }
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Cells cell = getItem(position);
        myViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new myViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
            viewHolder.num = (TextView) convertView.findViewById(R.id.cell);
            viewHolder.layout = (ConstraintLayout) convertView.findViewById(R.id.tile); // Get the ConstraintLayout reference
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (myViewHolder) convertView.getTag();
        }
        viewHolder.num.setText(cell.getNum());
        viewHolder.num.setBackgroundResource(circle_clr[position]);
        viewHolder.layout.setBackgroundResource(tile_clr[position]);
        return convertView;
    }
    private static class myViewHolder{
        TextView num;
        ConstraintLayout layout;
    }
    public void updateColor(int position,int text_color,int tile_colour){
        circle_clr[position] = text_color;
        tile_clr[position] = tile_colour;
        notifyDataSetChanged();
    }
    public int getColor(int position){
        return circle_clr[position];
    }
}
