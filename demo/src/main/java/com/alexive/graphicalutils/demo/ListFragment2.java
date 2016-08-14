/*
 * Copyright 2016 J. Alexandre Oliveira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexive.graphicalutils.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexive.graphicalutils.fragments.RecyclerViewFragment;
import com.alexive.graphicalutils.lists.Divider;
import com.alexive.graphicalutils.lists.GridAdapter;
import com.alexive.graphicalutils.lists.ListAdapter;
import com.alexive.graphicalutils.lists.ListItemType;
import com.squareup.picasso.Picasso;

/**
 * Displays a grid
 */
public class ListFragment2 extends RecyclerViewFragment implements ListAdapter.ListButtonClickListener {

    private static final int IMAGE_BUTTON_RESID = R.drawable.ic_favorite_black_24dp;
    private GridAdapter mListAdapter;
    private Object[] objects;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        objects = new Object[]{
                new DummyObject("Lisbon", "Seen from Terreiro do Paço",
                        R.drawable.lisbon),
                new DummyObject("Praça D. Pedro IV", "aka Rossio, Lisboa",
                        R.drawable.rossio),
                new DummyObject("Sky", "At sunset",
                        R.drawable.sky)
        };
        super.onViewCreated(view, savedInstanceState);
        this.mListAdapter = new SecondListAdapter();
        getRecyclerView().setLayoutManager(this.mListAdapter.getGridLayoutManager(getActivity(), 2));
        getRecyclerView().addItemDecoration(this.mListAdapter.getDecorationForLargeSpacing(getActivity()));

        //This styles the dividers
        this.mListAdapter.setDividerColor(Divider.MATERIAL_LIGHT_DIVIDER_COLOR);
        this.mListAdapter.setDividerStyle(Divider.Style.JUST_SUB_HEADERS);
        //
        //setOnItemClickListener(this);
        //setOnItemLongClickListener(this);
        mListAdapter.setButtonOnClickListener(this);

        setAdapter(this.mListAdapter);

        //Note that this one need to be called AFTER the adapter is set!
        setEmptyText("Oh no, the list is empty!");

    }

    @Override
    public void onButtonClick(ImageButton button, int index) {
        Toast.makeText(getActivity(), "You pressed heart #" + index, Toast.LENGTH_SHORT).show();
    }

    private class SecondListAdapter extends GridAdapter {

        public SecondListAdapter() {
            super(DetailsType.TWO_LINES);
        }

        @Override
        public void bindDataToItem(int index,
                                   View itemView,
                                   View detailsView,
                                   ImageView imageViewOrAvatar,
                                   ImageButton imageButton,
                                   TextView... txt) {
            DummyObject current = (DummyObject) objects[index];

            itemView.setBackgroundColor(Color.parseColor("#F3F3F3"));

            Picasso.with(getActivity()).load((Integer) current.getAvatar()).into(imageViewOrAvatar);

            txt[0].setText(current.getName());          //1st text view's text
            txt[1].setText(current.getDescription());   //2nd

            imageButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), IMAGE_BUTTON_RESID));

        }

        @Override
        public String getSubHeaderText(int position) {
            return null;
        }

        @Override
        public int getNumItems() {
            return objects.length;
        }

        @Override
        public ListItemType getListItemDataType(int index) {
            /**
             * You can return any type here, the adapter will handle them as a normal list item
             * and make it span all columns. For single cell grid items, return this one
             */
            return ListItemType.GRID_ITEM;
        }
    }
}
