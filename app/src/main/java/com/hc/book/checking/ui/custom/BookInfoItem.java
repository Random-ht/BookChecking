package com.hc.book.checking.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.book.checking.R;

/**
 * Created by hutao on 2019/4/17.
 * 书本详情的每一条
 */
public class BookInfoItem extends LinearLayout {

    private String left_content;
    private String right_content;
    private TextView mTvLeft;
    private TextView mTvRight;

    public BookInfoItem(Context context) {
        this(context, null);
    }

    public BookInfoItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookInfoItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.book_info_item);

        left_content = array.getString(R.styleable.book_info_item_left_content);
        right_content = array.getString(R.styleable.book_info_item_right_content);

        array.recycle();
        View inflate = inflate(context, R.layout.custom_book_info_item, this);
        mTvLeft = inflate.findViewById(R.id.left);
        mTvRight = inflate.findViewById(R.id.right);

        mTvLeft.setText(TextUtils.isEmpty(left_content) ? "" : left_content);
        mTvRight.setText(TextUtils.isEmpty(right_content) ? "" : right_content);
    }

    public void setLeftContent(String content) {
        mTvLeft.setText(TextUtils.isEmpty(content) ? "" : content);
    }

    public void setRightContent(String content) {
        mTvRight.setText(TextUtils.isEmpty(content) ? "" : content);
    }
}
