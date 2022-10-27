package com.example.pcr_hud;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<RecyclerViewItem> mData = null;

    public RecyclerViewAdapter(ArrayList<RecyclerViewItem> data) {
        mData = data;
    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item = mData.get(position);

        holder.mainText.setText(item.getMainTitle());
        holder.subText.setText(item.getSubTitle());
        holder.listlat.setText(item.getLat());
        holder.listlon.setText(item.getLon());

        holder.mainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext() , "click : "+item.getMainTitle() , Toast.LENGTH_SHORT).show();
                NaviFragment.select_addressnumber(item.getMainTitle(),item.getLat(), item.getLon() );
            }
        });

        holder.subText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext() , "click : "+item.getMainTitle() , Toast.LENGTH_SHORT).show();
                NaviFragment.select_addressnumber(item.getMainTitle(),item.getLat(), item.getLon() );
            }
        });

    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mainText;
        TextView subText;
        TextView listlat;
        TextView listlon;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            mainText = itemView.findViewById(R.id.icon_main_text);
            subText = itemView.findViewById(R.id.icon_sub_text);
            listlat = itemView.findViewById(R.id.icon_sub_lat);
            listlon = itemView.findViewById(R.id.icon_sub_lon);


        }
    }
}