package com.ponyvillelive.pvlmobile.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ponyvillelive.pvlmobile.R;

/**
 * https://stackoverflow.com/questions/16506275/imageview-be-a-square-with-dynamic-width
 */
public class SquareImageView extends ImageView {

    private static final int ORIENTATION_VERTICAL = 0;
    private static final int ORIENTATION_HORIZONTAL = 0;

    private int orientation;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray arr = context.getTheme()
                                .obtainStyledAttributes(attrs, R.styleable.SquareImageView, 0, 0);
        try {
            orientation = arr.getInt(R.styleable.SquareImageView_squareOrientation, 0);
        } finally {
            arr.recycle();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(orientation == ORIENTATION_HORIZONTAL) {
            int width = getMeasuredWidth();
            setMeasuredDimension(width, width);
        } else {
            int height = getMeasuredHeight();
            setMeasuredDimension(height, height);
        }
    }

}
