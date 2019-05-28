package com.hc.book.checking.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hc.book.checking.R;


/**
 * Created by hutao on 2019/3/27.
 * 自定义actionBar   默认两边的图片和中间标题显示   两边的标题不显示
 */
public class CustomActionBar extends LinearLayout {

    private Context context;
    private LinearLayout actionBarLeftLayout;
    private LinearLayout actionBarRightLayout;
    private RelativeLayout actionBarLayout;
    private ImageView actionBarLeftImage;
    private ImageView actionBarRightImage;
    private TextView actionBarLeftTitle;
    private TextView actionBarRightTitle;
    private TextView actionBarCenterTitle;

    public CustomActionBar(Context context) {
        this(context, null);
    }

    public CustomActionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.my_action_bar);

        Drawable left_image_drawable = typedArray.getDrawable(R.styleable.my_action_bar_left_image);
        int left_image_vis = typedArray.getInt(R.styleable.my_action_bar_left_image_vis, VISIBLE);
        Drawable right_image_drawable = typedArray.getDrawable(R.styleable.my_action_bar_right_image);
        int right_image_vis = typedArray.getInt(R.styleable.my_action_bar_right_image_vis, VISIBLE);

        String left_title = typedArray.getString(R.styleable.my_action_bar_left_title);
        int left_title_vis = typedArray.getInt(R.styleable.my_action_bar_left_title_vis, GONE);
        String right_title = typedArray.getString(R.styleable.my_action_bar_right_title);
        int right_title_vis = typedArray.getInt(R.styleable.my_action_bar_right_title_vis, GONE);
        String center_title = typedArray.getString(R.styleable.my_action_bar_center_title);
        int center_title_vis = typedArray.getInt(R.styleable.my_action_bar_center_title_vis, VISIBLE);


        View inflate = inflate(context, R.layout.custom_action_bar, this);
        actionBarLeftLayout = inflate.findViewById(R.id.actionBarLeftLayout);
        actionBarRightLayout = inflate.findViewById(R.id.actionBarRightLayout);
        actionBarLayout = inflate.findViewById(R.id.actionBarLayout);
        actionBarLeftImage = inflate.findViewById(R.id.actionBarLeftImage);
        actionBarRightImage = inflate.findViewById(R.id.actionBarRightImage);
        actionBarLeftTitle = inflate.findViewById(R.id.actionBarLeftTitle);
        actionBarRightTitle = inflate.findViewById(R.id.actionBarRightTitle);
        actionBarCenterTitle = inflate.findViewById(R.id.actionBarCenterTitle);

        setTitle(actionBarLeftTitle, TextUtils.isEmpty(left_title) ? "返回" : left_title, left_title_vis);
        setTitle(actionBarRightTitle, right_title, right_title_vis);
        setTitle(actionBarCenterTitle, center_title, center_title_vis);

        setImage(actionBarLeftImage, left_image_drawable, left_image_vis);
        setImage(actionBarRightImage, right_image_drawable, right_image_vis);

        typedArray.recycle();
    }

    private void setTitle(TextView textView, String content, int vis) {
        textView.setText(TextUtils.isEmpty(content) ? "" : content);
        if (vis == getResources().getInteger(R.integer.GONE)) {
            textView.setVisibility(GONE);
        } else if (vis == getResources().getInteger(R.integer.INVISIBLE)) {
            textView.setVisibility(INVISIBLE);
        } else {
            textView.setVisibility(VISIBLE);
        }
    }

    /***
     * 设置两个图片
     * @param imageView
     * @param drawable
     * @param vis
     */
    private void setImage(ImageView imageView, Drawable drawable, int vis) {
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        if (vis == getResources().getInteger(R.integer.GONE)) {
            imageView.setVisibility(GONE);
        } else if (vis == getResources().getInteger(R.integer.INVISIBLE)) {
            imageView.setVisibility(INVISIBLE);
        } else {
            imageView.setVisibility(VISIBLE);
        }
    }


    public CustomActionBar setActionBarLeftImage(int resource) {
        actionBarLeftImage.setImageResource(resource);
        return this;
    }

    public CustomActionBar setActionBarLeftImageVisibility(int Visibility) {
        actionBarLeftImage.setVisibility(Visibility);
        return this;
    }

    public CustomActionBar setActionBarLeftTitle(String leftTitle) {
        actionBarLeftTitle.setText(leftTitle);
        return this;
    }

    public CustomActionBar setActionBarLeftTitleVisibility(int Visibility) {
        actionBarLeftTitle.setVisibility(Visibility);
        return this;
    }

    public CustomActionBar setActionBarRightImage(int resource) {
        actionBarRightImage.setImageResource(resource);
        return this;
    }

    public CustomActionBar setActionBarRightImageVisibility(int Visibility) {
        actionBarRightImage.setVisibility(Visibility);
        return this;
    }

    public CustomActionBar setActionBarRightTitle(String rightTitle) {
        actionBarRightTitle.setText(rightTitle);
        return this;
    }

    public CustomActionBar setActionBarRightTitleVisibility(int Visibility) {
        actionBarRightTitle.setVisibility(Visibility);
        return this;
    }

    public CustomActionBar setActionBarBackGroundColor(int color) {
        actionBarLayout.setBackgroundColor(context.getResources().getColor(color));
        return this;
    }

    public CustomActionBar setActionBarCenterTitle(String centerTitle) {
        actionBarCenterTitle.setText(centerTitle);
        return this;
    }

    public CustomActionBar setActionBarCenterTitleVisibility(int Visibility) {
        actionBarCenterTitle.setVisibility(Visibility);
        return this;
    }

    public CustomActionBar setActionBarLeftLayoutClick(ActionBarLeftLayoutClickListener leftLayoutClick) {
        actionBarLeftLayout.setOnClickListener(v -> {
            leftLayoutClick.leftLayoutClickListener();
        });
        return this;
    }

    public CustomActionBar setActionBarRightLayoutClick(ActionBarRightLayoutClickListener rightLayoutClick) {
        actionBarRightLayout.setOnClickListener(v -> {
            rightLayoutClick.rightLayoutClickListener();
        });
        return this;
    }

    public interface ActionBarLeftLayoutClickListener {
        void leftLayoutClickListener();
    }

    public interface ActionBarRightLayoutClickListener {
        void rightLayoutClickListener();
    }
}
