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

package com.alexive.graphicalutils.lists;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexive.graphicalutils.R;
import com.alexive.graphicalutils.view.ViewUtils;

/**
 * Base adapter class. Provides methods for divider customization, clicklistener setter
 * and subheader definition,
 */
abstract class BaseAdapter extends RecyclerView.Adapter<AwesomeViewHolder> {

    OnItemClickListener onItemClickListener;
    private boolean extraPadding = false;
    private Divider.Style dividerStyle = Divider.Style.NONE;
    private int dividerColor;
    private boolean shift = false;
    private OnItemLongClickListener onItemLongClickListener;
    private View headerView;
    private View emptyView;
    private DataSetObserver observer;

    public BaseAdapter() {
        if (shouldHaveStableIds())
            setHasStableIds(true);
        observer = new DataSetObserver() {
            @Override
            public void handleDataSizeChanged() {
                if (emptyView != null)
                    if (getNumItems() == 0) {
                        ViewUtils.createCircularReveal(emptyView);
                    } else {
                        ViewUtils.createCircularRevealInverse(emptyView, true);
                    }
            }
        };
        registerAdapterDataObserver(observer);
    }

    protected boolean shouldHaveStableIds() {
        return false;
    }

    /**
     * Whether or not the item drawn an a certain position in the list
     * is a subheader.
     *
     * @param position Position of the item in the adapter.
     * @return True if item at position is a subheader, false otherwise. If you don't want the list
     * to have subheaders, just make this return false.
     */
//    public abstract boolean isSubHeader(int position);


//    protected static void setImage(ImageView img, Object object) {
//        if (object == Tuple.NULL_PLACE_HOLDER || object == null)
//            return;
//        if (object instanceof Bitmap)
//            img.setImageBitmap((Bitmap) object);
//        else
//            img.setImageDrawable((Drawable) object);
//    }

    /**
     * The text of a subheader.
     *
     * @param position Position of the subheader in the adapter.
     */
    public abstract String getSubHeaderText(int position);

    /**
     * @return true if the SubHeaders' text color should be the theme's accent color,
     * false otherwise. If you're not using the material theme, just make this return false.
     */
    public boolean shouldSubHeaderTextColorBeAccent() {
        return false;
    }

//    /*
//     * @return Id of the itemViews layout.
//     */
//    public abstract int getItemLayoutResId();

    /**
     * @return The number of items in the list.
     */
    public abstract int getNumItems();

    /**
     * Binds data to an item of the list.
     *
     * @param index    The index of the item in the list
     * @return recycled with the updated data. Please note that recycled.getType()
     * must be equal to getListItemDataType(index) or the rendering will be misbehave.
     */
    public abstract void bindDataToListItem(int index, View itemView, ImageView iconOrAvatar,
                                            ImageButton button, TextView... texts);

    /**
     * The type of data that will be bound to the list item at a position index
     */
    public abstract ListItemType getListItemDataType(int index);

    /*
     * @return Id of the separator itemView layout.
     */
    public int getSubheaderLayoutResId() {
        return shift ? R.layout.subheader_72dp_padding :
                R.layout.subheader_simple_padding;
    }

    /**
     * @return Similliar as getSubheaderLayoutResId() but the text color is
     * the theme's text color, instead of the accent color.
     */
    protected int getSubHeaderNoAccentLayoutResId() {
        return shift ? R.layout.subheader_72dp_padding_no_accent :
                R.layout.subheader_simple_padding_no_accent;
    }

    public void setSubheaderTextShifted(boolean shift) {
        this.shift = shift;
    }

    @Override
    public AwesomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == AwesomeViewTypes.HEADER.code) {
            return new AwesomeViewHolder(headerView);
        } else if (i != AwesomeViewTypes.SUB_HEADER.code) {
            AwesomeViewHolder avh = new AwesomeViewHolder(i, viewGroup);
            setListenerForHolder(avh);
            return avh;
        } else
            return new AwesomeViewHolder(shouldSubHeaderTextColorBeAccent() ?
                    getSubheaderLayoutResId() : getSubHeaderNoAccentLayoutResId(),
                    viewGroup);
    }

    public void setListenerForHolder(AwesomeViewHolder avh) {
        ClickListener clickListener = getListenerForHolder(avh);
        avh.setOnClickListener(clickListener);
        avh.setOnLongClickListener(clickListener);
    }

    /**
     * Sets the divider style
     *
     * @param style Value from the {@link com.alexive.graphicalutils.lists.Divider.Style}
     *              enum
     */
    public void setDividerStyle(Divider.Style style) {
        dividerStyle = style;
    }

    /**
     * Sets the divider color.
     *
     * @param color Divider color. You can use {@link Divider#MATERIAL_DARK_DIVIDER_COLOR} or
     *              {@link Divider#MATERIAL_LIGHT_DIVIDER_COLOR} (in BaseAdapter class).
     */
    public void setDividerColor(int color) {
        dividerColor = color;
    }

    @Override
    public void onBindViewHolder(AwesomeViewHolder awesomeViewHolder, int index) {
        int i = index;
        if (getItemViewType(i) == AwesomeViewTypes.HEADER.code)
            return;
        if (headerView != null)
            i--;
        configExtraPadding(awesomeViewHolder, i);
        if (awesomeViewHolder.divider != null)
            if (drawDivider(index))
                awesomeViewHolder.divider.setBackgroundColor(dividerColor);
            else
                awesomeViewHolder.divider.setBackgroundColor(Color.TRANSPARENT);
        if (getItemViewType(index) == AwesomeViewTypes.SUB_HEADER.code) {
//            if (headerView != null)
//                i--;
            awesomeViewHolder.text1.setText(getSubHeaderText(i));
        } else {
//            if (headerView != null)
//                i--;
            preBindData(i, awesomeViewHolder);
        }
    }

    protected void preBindData(int i, AwesomeViewHolder awesomeViewHolder) {
        bindDataToListItem(i,
                awesomeViewHolder.itemView,
                awesomeViewHolder.left,
                awesomeViewHolder.button,
                awesomeViewHolder.text1, awesomeViewHolder.text2);
    }

    private void configExtraPadding(AwesomeViewHolder awesomeViewHolder, int i) {
        if (!drawDivider(i) || awesomeViewHolder.divider == null)
            return;
        if (extraPadding) {
            ((RelativeLayout.LayoutParams) awesomeViewHolder.divider.getLayoutParams())
                    .setMargins(ViewUtils.convertDPtoPixels(awesomeViewHolder.divider.getContext(), 72)
                            , 0, 0, 0);
        } else {
            ((RelativeLayout.LayoutParams) awesomeViewHolder.divider.getLayoutParams())
                    .setMargins(0, 0, 0, 0);
        }
        awesomeViewHolder.divider.requestLayout();
    }

    public void setDividersHaveExtraPadding(boolean extra) {
        extraPadding = extra;
    }

    public void setShiftSubheaderText(boolean shift) {
        this.shift = shift;
    }

    public boolean hasHeaderView() {
        return headerView != null;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        observer.handleDataSizeChanged();
    }

    @Override
    public final int getItemCount() {
        return getNumItems() + (headerView == null ? 0 : 1);
    }

    protected int getAdapterPositionForViewHolder(AwesomeViewHolder holder) {
        return holder.getAdapterPosition() + (headerView == null ? 0 : -1);
    }

    protected int getRealAdapterPosition(int index) {
        return index + (headerView == null ? 0 : 1);
    }

    protected boolean drawDivider(int i) {
        if (dividerStyle == Divider.Style.NONE)
            return false;
        else if (dividerStyle == Divider.Style.JUST_SUB_HEADERS && i < getItemCount() - 1)
            return getItemViewType(i + 1) == AwesomeViewTypes.SUB_HEADER.code;
        else
            return getItemCount() - 1 != getRealAdapterPosition(i);
    }

    public void addHeaderView(View headerView) {
        this.headerView = headerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null) {
            if (position == 0)
                return AwesomeViewTypes.HEADER.code;
            else
                position--;
        }
//        if (isSubHeader(position))
//            return AwesomeViewTypes.SUB_HEADER.code;
//        else
            return getListItemDataType(position).resId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        onItemLongClickListener = listener;
    }

    protected void handleClick(AwesomeViewHolder avh) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClicked(avh.itemView,
                    getAdapterPositionForViewHolder(avh));
    }

    protected boolean handleLongClick(AwesomeViewHolder avh) {
        return onItemLongClickListener != null &&
                onItemLongClickListener.onItemLongClick(avh.itemView,
                        getAdapterPositionForViewHolder(avh));
    }

    protected ClickListener getListenerForHolder(AwesomeViewHolder avh) {
        return new ClickListener(avh);
    }

    public static abstract class DataSetObserver extends RecyclerView.AdapterDataObserver {

        public abstract void handleDataSizeChanged();

        @Override
        public void onChanged() {
            super.onChanged();
            handleDataSizeChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            handleDataSizeChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            handleDataSizeChanged();
        }

    }

    private class ClickListener implements View.OnClickListener, View.OnLongClickListener {

        private AwesomeViewHolder avh;

        public ClickListener(AwesomeViewHolder avh) {
            this.avh = avh;
        }

        @Override
        public void onClick(View v) {
            handleClick(avh);

        }

        @Override
        public boolean onLongClick(View v) {
            return handleLongClick(avh);
        }
    }
}
