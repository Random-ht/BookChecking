package com.hc.book.checking.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.book.checking.R;
import com.hc.book.checking.entity.BookInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutao on 2019/4/16.
 */
public class SearchContentAdapter extends RecyclerView.Adapter<SearchContentAdapter.ViewHolder> {
    private Context context;
    private List<BookInfo> mData = new ArrayList<>();

    public SearchContentAdapter(Context context, List<BookInfo> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookInfo bookInfo = mData.get(position);
//        Log.i("------------", new Gson().toJson(bookInfo));
        holder.bookName.setText(getString(bookInfo.getBookName()));
        holder.bookPurchaseTime.setText(getString(bookInfo.getPurchaseDate()));
        holder.bookAuthor.setText(getString("作者:" + bookInfo.getBookAuthor()));
        holder.bookNum.setText(getString("数量:" + bookInfo.getBookCount()));
        holder.bookprise.setText(getString("单价:" + bookInfo.getMoney()));
        holder.layout.setOnClickListener(v -> {
            listener.OnItemClick(getString(bookInfo.getBookISBNCode()));
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private String getString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        } else {
            return string.trim();
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(String isbnCode);
    }

    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookName;
        TextView bookPurchaseTime;
        TextView bookAuthor;
        TextView bookNum;
        TextView bookprise;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            bookPurchaseTime = itemView.findViewById(R.id.book_purchase_time);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookNum = itemView.findViewById(R.id.book_num);
            bookprise = itemView.findViewById(R.id.book_prise);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
