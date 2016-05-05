/*
 * Copyright (C) 2016 J. Oliveira
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexive.graphicalutils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.alexive.graphicalutils.R;

/**
 * Subclass of image view capable of having a proportional size.
 * So far it supports 16:9, 4:3, 1:1 and no proportion
 */
public class ProportionalImageView extends ImageView {

    private int proportionCode = -1;
    private boolean heightAsRef;

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProportionalImageView,
                0, 0);
        proportionCode = a.getInt(R.styleable.ProportionalImageView_proportion, 0);
        a.recycle();
    }

    public void setSquare() {
        proportionCode = 0;
    }

    public void setSquareSideIsHeight() {
        heightAsRef = true;
    }

    public void setSixteenNine() {
        proportionCode = 169;
    }

    public void setFourThree() {
        proportionCode = 43;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (proportionCode == -1)
            return;

        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();

        boolean square = proportionCode == 0;
        if (square)
            mHeight = !heightAsRef ? mWidth : mHeight;
        else if (proportionCode == 1)
            mHeight = mHeight > mWidth ? mHeight : mWidth;
        else if (proportionCode == 169)
            mHeight = (int) (mWidth * 9f / 16f + 0.5f);
        else if (proportionCode == 43)
            mHeight = (int) (mWidth * 3f / 4f + 0.5f);
        setMeasuredDimension(mWidth, mHeight);
    }
}
