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

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexive.graphicalutils.R;
import com.alexive.graphicalutils.view.ProportionalImageView;
import com.alexive.graphicalutils.view.ViewUtils;

/**
 * Base grid adapter class. Provides subheaders, click listeners setting, etc.
 */
public abstract class GridAdapter extends ListAdapter {
    /*
    ---------------------
    * ---------------------
    * TODO MAJOR REFACTOR!!!!!!! or just make it an hybrid (rows with n columns or w/ 1)
    * ---------------------
    * ---------------------
     */

    private final DetailsType mDetailsViewType;
    private final String DETAILS_VIEW_TAG = "detailsviewtag";
    private GridItemSpecs gridSpecs;
    private int numCols;

    public GridAdapter(DetailsType type) {
        super();
        mDetailsViewType = type;
    }

    @Override
    public AwesomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == ListItemType.GRID_ITEM.resId) {
            return getItemView(viewGroup);
        } else
            return super.onCreateViewHolder(viewGroup, i);
    }

    protected AwesomeViewHolder getItemView(ViewGroup group) {
        if (gridSpecs == null)
            gridSpecs = new GridItemSpecs();
        LayoutInflater inflater = LayoutInflater.from(group.getContext());
        View base = inflater.inflate(gridSpecs.detailsViewFloats ? R.layout
                .grid_float_headerfooter_tile : R.layout.grid_dummy_tile, group, false);
        base.setClickable(true);
        AwesomeViewHolder result = new AwesomeViewHolder(base);
        setListenerForHolder(result);
        ProportionalImageView imageView = (ProportionalImageView) base.findViewById(R.id.imageView);
        if (gridSpecs.forceSquareImage)
            imageView.setSquare();
        result.setImageView1(imageView);
        if (mDetailsViewType == DetailsType.NONE) {
            return result;
        }
        View details = getDetailsView(inflater, group, result);
        if (gridSpecs.detailsViewFloats) {
            //We just add it to the framelayout and set the gravity
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                    .FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            FrameLayout fl = (FrameLayout) base.findViewById(R.id.floatParent);
            params.gravity = gridSpecs.detailsGravityIsTop ? Gravity.TOP : Gravity.BOTTOM;
            fl.addView(details, 1, params);
        } else {
            LinearLayout parent = (LinearLayout) base.findViewById(R.id.noFloatParent);
//            LinearLayout parent = (LinearLayout) base.findViewById(gridSpecs.detailsGravityIsTop ? R
//                    .id.headerNoFloat : R.id.footerNoFloat);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
//                    .FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parent.addView(details, gridSpecs.detailsGravityIsTop ? 0 : 1);
            base.findViewById(R.id.noFloatParent).requestLayout();
        }
        return result;
    }

//    public String getGridImageTransitionName(Context c) {
//        return c.getString(R.string.image_transition_name);
//    }

    public View getDetailsView(LayoutInflater inflater, ViewGroup parent,
                               AwesomeViewHolder result) {
        //TODO FORCE DARK MODE
        View view = null;
        switch (mDetailsViewType) {
            case SINGLE_LINE:
                view = inflater.inflate(R.layout.griddetails_single_line, null);
                break;
            case TWO_LINES:
                view = inflater.inflate(R.layout.griddetails_two_lines, null);
                break;
            default:
            case NONE:
                return null;
        }
        view.setTag(DETAILS_VIEW_TAG);
        result.setDetailsView(view);
        result.setText1((TextView) view.findViewById(android.R.id.text1));
        result.setText2((TextView) view.findViewById(android.R.id.text2));
        result.setButton((ImageButton) view.findViewById(R.id.imageButton));
        return view;
    }

    @Override
    public boolean shouldSubHeaderTextColorBeAccent() {
        return false;
    }

    @Override
    public boolean drawDivider(int i) {
        //Dividers in a grid make no sense.
        return false;
    }

    @Override
    public void bindDataToListItem(int index,
                                   View itemView,
                                   ImageView iconOrAvatar,
                                   ImageButton button,
                                   TextView... texts) {
        //useless
        //you should override bindDataToItem
    }

    @Override
    protected void preBindData(int i, AwesomeViewHolder awesomeViewHolder) {
        bindDataToItem(i,
                awesomeViewHolder.itemView,
                awesomeViewHolder.getDetailsView(),
                awesomeViewHolder.getImageView1(),
                awesomeViewHolder.button,
                awesomeViewHolder.text1,
                awesomeViewHolder.text2);
    }

    /**
     * Fills in the list item
     *
     * @param index        The index of the item
     * @param itemView     The list item's view
     * @param detailsView  Item's detail view, typically under the image
     * @param imageViewOrAvatar    The item's ImageView, or the avatar if it's an ordinary item
     * @param imageButton  If it's an ordinary list item, it's button
     * @param txt          The item's text views, from top to bottom, left to right
     */
    public abstract void bindDataToItem(int index,
                                        View itemView,
                                        View detailsView,
                                        ImageView imageViewOrAvatar,
                                        ImageButton imageButton,
                                        TextView... txt);


    public void setItemSpecs(GridItemSpecs specs) {
        gridSpecs = specs;
    }

    //public abstract void bindDataToItemView(int position, ImageView imageView, View... detailsView);

    public GridLayoutManager getGridLayoutManager(Context ctx, int numColumns) {
        this.numCols = numColumns;
        GridLayoutManager mngr = new GridLayoutManager(ctx, numColumns);
        mngr.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getItemViewType(position) == ListItemType.GRID_ITEM.resId ? 1 : numCols;
            }
        });
        return mngr;
    }

    public RecyclerView.ItemDecoration getDecorationForSmallSpacing(Context c) {
        return new Decorator(ViewUtils.convertDPtoPixels(c, 1));
    }

    public RecyclerView.ItemDecoration getDecorationForLargeSpacing(Context c) {
        return new Decorator(ViewUtils.convertDPtoPixels(c, 4));
    }

    public enum DetailsType {
        NONE,
        SINGLE_LINE,
        TWO_LINES
    }

    public static class GridItemSpecs {
        public boolean detailsGravityIsTop = false;
        public boolean detailsViewFloats = false;
        public boolean forceDarkMode = false;
        public boolean forceSquareImage = true;
    }

    private class Decorator extends RecyclerView.ItemDecoration {

        private final int padding;

        public Decorator(int padding) {
            this.padding = padding;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            if (getListItemDataType(pos) == ListItemType.SUB_HEADER)
                return;
            //TODO Faster approach
            //TODO actually compute the list item's padding IF it's on the edge.
            //Having subheaders/items taking up a full row made the code I had before messed up
//      //If the list has subheaders, without this the padding will be VERY EFFED Up.
//      int newPos = 0;
//      for (int i = 0; i <= pos; i++) {
//        if (isSubHeader(i)) {
//         newPos = 0;
//        }else
//          newPos++;
//      }
//
//      if(newPos % numCols != 0)
            outRect.left = padding;
//      if ((newPos + 1) % numCols != 0);
            outRect.right = padding;
            outRect.bottom = padding;
            outRect.top = padding;
        }
    }
}
