package com.ponyvillelive.pvlmobile.ui.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ponyvillelive.pvlmobile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by berwyn on 27/02/15.
 */
public class StationView extends CardView {

    @InjectView(R.id.station_icon)
    ImageView   icon;
    @InjectView(R.id.station_name)
    TextView    title;
    @InjectView(R.id.station_genre)
    TextView    description;
    @InjectView(R.id.station_menu)
    ImageButton streamButton;
    @InjectView(R.id.title)
    TextView    song;
    @InjectView(R.id.station_artist)
    TextView    artist;

    public StationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_station_list_item, this, true);
        ButterKnife.inject(this);
    }

    private void layoutView(View view, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;

        view.layout(leftWithMargins, topWithMargins,
                leftWithMargins + width, topWithMargins + height);
    }

    private int getWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthUsed = 0;
        int heightUsed = 0;

        measureChildWithMargins(icon,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(icon);

        measureChildWithMargins(title,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(title);

        measureChildWithMargins(description,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(description);

        measureChildWithMargins(streamButton,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(streamButton);

        measureChildWithMargins(song,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(song);

        measureChildWithMargins(artist,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(artist);

        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        int currentTop = getPaddingTop();

        layoutView(icon,paddingLeft, currentTop,
                icon.getMeasuredWidth(),
                icon.getMeasuredHeight());
    }
}
