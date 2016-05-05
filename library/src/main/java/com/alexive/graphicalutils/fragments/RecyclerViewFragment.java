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

package com.alexive.graphicalutils.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexive.graphicalutils.R;
import com.alexive.graphicalutils.lists.ListAdapter;
import com.alexive.graphicalutils.lists.OnItemClickListener;
import com.alexive.graphicalutils.lists.OnItemLongClickListener;
import com.alexive.graphicalutils.view.ViewUtils;

/**
 * Simple fragment containing a RecyclerView. Has support for an empty view (even if the chosen
 * adapter is not a subclass of this library's ListAdapter class) and click and long click
 * listeners. Still, it'll work best if you use a class derived from {@link ListAdapter}.
 *
 * Just override {@link #onViewCreated(View, Bundle)} (don't forget to call super!) and you can go
 * from there
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Listener mOnItemTouchListener;
    private String emptyText;
    private View emptyView;
    private boolean emptyViewAddedToLayout = false;
    private boolean isEmptyViewEmptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.recycler_fragment_layout,
                container,
                false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (this.adapter != null)
            setAdapter(adapter);
        if (this.emptyText != null)
            setEmptyText(emptyText);
        else if (this.emptyView != null && !isEmptyViewEmptyText)
            if (emptyViewAddedToLayout)
                actuallySetEmptyView();
            else
                setEmptyView(emptyView);

        return view;
    }

    /**
     * @return The fragment's recyclerView
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * Gets the RecyclerView's adapter.
     *
     * @return The adapter provided when you called setAdapter, or null if the recyclerView as no
     * adapter.
     */
    public RecyclerView.Adapter getAdapter() {
        return adapter != null ? adapter :
                mRecyclerView != null ? mRecyclerView.getAdapter() : null;
    }

    /**
     * Sets the adapter to the recyclerView
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (this.mRecyclerView == null)
            return;
        mRecyclerView.setAdapter(adapter);
        if (mOnItemTouchListener != null)
            mRecyclerView.removeOnItemTouchListener(mOnItemTouchListener);
        else
            mOnItemTouchListener = new Listener();
        if (adapter instanceof ListAdapter) {
            if (mClickListener != null)
                ((ListAdapter) adapter).setOnItemClickListener(mClickListener);
            if (mOnItemLongClickListener != null)
                ((ListAdapter) adapter).setOnItemLongClickListener(mOnItemLongClickListener);
        } else
            mRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
        if (emptyViewAddedToLayout && emptyView != null)
            actuallySetEmptyView();
    }

    /**
     * Sets the callback to be called when the user clicks an item.
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mClickListener = listener;
        if (adapter != null && adapter instanceof ListAdapter)
            ((ListAdapter) adapter).setOnItemClickListener(mClickListener);
    }


    /**
     * Sets the callback to be called when the user long clicks an item.
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mOnItemLongClickListener = listener;
        if (adapter != null && adapter instanceof ListAdapter)
            ((ListAdapter) adapter).setOnItemLongClickListener(mOnItemLongClickListener);
    }

    /**
     * Notifies a change in the data set.
     */
    public void notifyDataSetChanged(){
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * Sets a message to be displayed when the list is empty.
     *
     * @param emptyText Will be shown when the list is empty.
     */
    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
        View view = getView();
        if (view == null)
            return;
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(emptyText);

        this.emptyView = textView;
        actuallySetEmptyView();
        isEmptyViewEmptyText = true;

    }

    /**
     * Sets a view to be displayed when the list is empty. This is the same as {@link
     * RecyclerViewFragment#setEmptyView(View, FrameLayout.LayoutParams)} passing null has the
     * second parameter. The view will be centered and will not stretch to the screen's width.
     * @param view Displayed when the list is empty.
     */
    public void setEmptyView(View view){
        setEmptyView(view, null);
    }

    /**
     * Sets a view to be displayed when the list is empty with optional LayoutParams.
     * @param view Displayed when the list is empty.
     * @param params Information about how view's layout in the countainer.
     */
    public void setEmptyView(View view, FrameLayout.LayoutParams params){
        emptyView = view;
        isEmptyViewEmptyText = true;
        FrameLayout frameLayout = (FrameLayout) getView();
        if (frameLayout == null)
            return;
        //In order to avoid having more than one emptyViews added to the layout
        if (frameLayout.getChildCount() > 2)
            frameLayout.removeViewAt(2);
        if (this.emptyText != null)
            frameLayout.findViewById(android.R.id.text1).setVisibility(View.GONE);
        if (params == null) {
            params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
        }

        frameLayout.addView(view, 1, params);
        this.emptyViewAddedToLayout = true;
        actuallySetEmptyView();
    }

    private void actuallySetEmptyView(){
        if (getAdapter() != null)
            if (getAdapter() instanceof ListAdapter)
                ((ListAdapter) getAdapter()).setEmptyView(emptyView);
            else {
                ListAdapter.DataSetObserver observer = new ListAdapter.DataSetObserver() {
                    @Override
                    public void handleDataSizeChanged() {
                        if (emptyView == null)
                            return;
                        if (getAdapter().getItemCount() == 0) {
                            if (emptyView.getVisibility() != View.VISIBLE) {
                                emptyView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int width = getView().getWidth();
                                        int height = getView().getHeight();
                                        ViewUtils.createCircularReveal(emptyView,
                                                width / 2,
                                                height / 2,
                                                Math.max(width, height));
                                    }
                                });
                            }
                        } else {
                            if (emptyView.getVisibility() == View.VISIBLE) {
                                emptyView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int width = getView().getWidth();
                                        int height = getView().getHeight();
                                        ViewUtils.createCircularRevealInverse(emptyView,
                                                width / 2,
                                                height / 2,
                                                width,
                                                true);
                                    }
                                });
                            }
                        }
                    }
                };
                getAdapter().registerAdapterDataObserver(observer);
                if (getAdapter().getItemCount() != 0)
                    this.emptyView.setVisibility(View.GONE);
            }
    }

    private class Listener implements RecyclerView.OnItemTouchListener{

        private GestureDetector mGestureDetector;

        public Listener(){
            mGestureDetector = new GestureDetector(mRecyclerView.getContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null)
                                if (mOnItemLongClickListener != null)
                                    mOnItemLongClickListener.onItemLongClick(child,
                                            mRecyclerView.getChildAdapterPosition(child));

                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && !mGestureDetector.onTouchEvent(e)){
                if (mClickListener != null)
                    mClickListener.onItemClicked(child,
                            mRecyclerView.getChildAdapterPosition(child));
            }
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
