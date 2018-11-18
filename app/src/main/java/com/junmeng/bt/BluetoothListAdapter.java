package com.junmeng.bt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.MyViewHolder> {

    private List<SearchResult> mList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mClickListener;

    public BluetoothListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public List<SearchResult> getList(){
        return mList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_bluetooth_device, null);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SearchResult result = mList.get(position);
        holder.nameView.setText(result.getName());
        holder.addressView.setText(result.getAddress());
        if (mClickListener != null) {
            /**
             * 这里加了判断,itemViewHolder.itemView.hasOnClickListeners()
             * 目的是减少对象的创建,如果已经为view设置了click监听事件,就不用重复设置了
             * 不然每次调用onBindViewHolder方法,都会创建两个监听事件对象,增加了内存的开销
             */
            if (!holder.itemView.hasOnClickListeners()) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.OnItemClick(v, result,position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView addressView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_name);
            addressView = itemView.findViewById(R.id.tv_address);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, SearchResult result,int position);
    }

}
