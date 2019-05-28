package com.hc.book.checking.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.book.checking.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hutao on 2019/4/16.
 * 文件的显示列表
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private Context context;
    private List<File> mData = new ArrayList<>();

    public FileListAdapter(Context context, List<File> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 设置数据
        File file = mData.get(position);
        if (file.isDirectory()) {
            holder.img.setImageResource(R.mipmap.folder);

        } else {
            if (file.getName().endsWith(".jpg")
                    || file.getName().endsWith(".png")
                    || file.getName().endsWith(".gif")) {
                holder.img.setImageResource(R.mipmap.img);
            } else if (file.getName().endsWith(".txt") || file.getName().endsWith(".log")) {
                holder.img.setImageResource(R.mipmap.txt);
            } else if (file.getName().endsWith(".xls")) {//获取出来的是excel表格
                holder.img.setImageResource(R.mipmap.xls);
            } else {
                holder.img.setImageResource(R.mipmap.unknow);
            }
        }
        holder.layout.setOnClickListener(v -> {
            listener.OnItemClick(file);
        });
        holder.name.setText(file.getName());
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(File file);
    }

    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView time;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
