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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexive.graphicalutils.R;

/**
 * Common RecyclerView.ViewHolder used by all Adapter classes in the lists package.
 */
class AwesomeViewHolder extends RecyclerView.ViewHolder {

    public TextView text1;
    public TextView text2;
    //  public TextView text3;
    public ImageView left;
    public View divider;
    public ImageButton button;
    public CheckBox checkBox;

    /**
     * Creates a new AwesomeViewHolder
     * @param layoutResId Layout resource id.
     * @param mViewGroup ViewGroup provided by the onCreateViewHolder adapter method.
     */
    public AwesomeViewHolder(int layoutResId, ViewGroup mViewGroup) {
        super(LayoutInflater.from(mViewGroup.getContext()).inflate(layoutResId, mViewGroup, false));
        construct();
    }

    public AwesomeViewHolder(View view) {
        super(view);
    }

    private void construct() {
        this.text1 = (TextView) itemView.findViewById(android.R.id.text1);
        this.text2 = (TextView) itemView.findViewById(android.R.id.text2);
        this.left = (ImageView) itemView.findViewById(R.id.imageView);
//    this.icon = (ImageView) itemView.findViewById(R.id.iconView);
        this.divider = itemView.findViewById(R.id.separator);
        this.button = (ImageButton) itemView.findViewById(R.id.imageButton);
        this.checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
    }

    //In order not to create another class and to let GridAdapter (#noHate!) use this Holder class,
    //I have established the following getters/setters.

    public TextView getText1() {
        return text1;
    }

    public void setText1(TextView text1) {
        this.text1 = text1;
    }

    public TextView getText2() {
        return text2;
    }

    public void setText2(TextView text2) {
        this.text2 = text2;
    }

    public ImageView getImageView1() {
        return left;
    }

    public void setImageView1(ImageView avatar) {
        this.left = avatar;
    }

    public ImageButton getButton() {
        return button;
    }

    public void setButton(ImageButton button) {
        this.button = button;
    }

    public View getDetailsView() {
        return divider;
    }

    public void setDetailsView(View detailsView) {
        this.divider = detailsView;
    }

    //End of the GridAdapter noHate paranoia.

    /**
     * Sets a click listener.
     * @param listener Called when itemView is clicked.
     */
    public void setOnClickListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
    }

    /**
     * Sets a long click listener.
     * @param listener Called when itemView is long-clicked.
     */
    public void setOnLongClickListener(View.OnLongClickListener listener){
        itemView.setOnLongClickListener(listener);
    }







}
