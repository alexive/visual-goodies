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

package com.alexive.graphicalutils.lists;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alexive.graphicalutils.R;
import com.alexive.graphicalutils.view.ViewUtils;

import java.util.ArrayList;

/**
 * Adapter that contains checkboxes. Handles animations
 */
public abstract class ListAdapter extends BaseAdapter {

    protected AvatarClickListener clickListener;
    protected ListButtonClickListener bttnListener;
    ArrayList<Long> checkedIds = new ArrayList<>();
    private ListCheckingActionMode mode;
    private boolean showCheckBoxes;
    private CheckedStateListener listener;
    private boolean disableClickListenerInCheckBoxMode = true;

    @Override
    protected boolean shouldHaveStableIds() {
        return true;
    }

    /**
     * @return true if the checkboxes are visible, false otherwise.
     */
    public boolean areCheckBoxesShown() {
        return showCheckBoxes;
    }

    /**
     * Changes whether or not the checkboxes are visible.
     *
     * @param showCheckBoxes If true, checkboxes will be shown (and the icon hidden) the next
     *                       time onBindViewHolder is called. false otherwise.
     */
    public void setShowCheckBoxes(boolean showCheckBoxes) {
        this.showCheckBoxes = showCheckBoxes;
//        try {
        notifyDataSetChanged();
//        } catch (IllegalStateException ex) {
        //nasty workaround
//            ex.printStackTrace();
//        }
    }

    /**
     * @return The listener to be called when a checkbox state changes.
     */
    public CheckedStateListener getListener() {
        return listener;
    }

    /**
     * Sets a checkbox checked state listener.
     *
     * @param listener Called when the user checks/unchecks the checkbox.
     */
    public void setCheckBoxCheckedListener(CheckedStateListener listener) {
        this.listener = listener;
    }

    ListCheckingActionMode getMode() {
        return this.mode;
    }

    void setMode(ListCheckingActionMode mode) {
        this.mode = mode;
    }

    /**
     * Checks/unchecks and item at a certain position
     *
     * @param index Index of the item
     * @param check Whether or not you want the item to be checked
     */
    public void checkItem(int index, boolean check) {
        checkItem(index, check, true);
    }

    //using isChecked is better because you'll only have to iterate through the list once
    //to get and use the state
//    public boolean[] getCheckedState() {
//        boolean[] result = new boolean[getNumItems()];
//        for (int i = 0; i < result.length; i++)
//            result[i] = isChecked(i);
//        return result;
//    }

    /**
     * {@link #checkItem(int, boolean)}
     * @param notify Whether or not to call {@link #notifyItemChanged(int)} after changing
     *               the item's state
     */
    private void checkItem(int index, boolean check, boolean notify) {
        index = getRealAdapterPosition(index);
        long id = getItemId(index);
        if (check) {
            if (checkedIds.indexOf(id) == -1)
                checkedIds.add(id);
        } else {
            int i = checkedIds.indexOf(id);
            if (i != -1)
                checkedIds.remove(i);
        }
        if (notify)
            notifyItemChanged(index);
    }

    /**
     * Whether or not an item at a certain position is checked
     */
    public boolean isChecked(int index) {
        return checkedIds.contains(getIdForItem(index));
    }

    public void uncheckAll() {
        checkedIds.clear();
    }

    @Override
    public final long getItemId(int position) {
        if (hasHeaderView())
            if (position == 0)
                return Integer.MIN_VALUE;
            else
                return getIdForItem(position - 1);
        else
            return getIdForItem(position);
    }

    /**
     * Required to implement the checked adapter. All items must be assigned to a unique ID in order
     * to maintain the items' checked state. You should override this method (instead of
     * {@link android.support.v7.widget.RecyclerView.Adapter#getItemId(int)} since the default
     * implementation just returns the item's position. So, if the data changes, items that were not
     * checked before may be checked and vice-versa.
     */
    public long getIdForItem(int position) {
        return position;
    }

    @Override
    public AwesomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        AwesomeViewHolder avh = super.onCreateViewHolder(viewGroup, i);
        if (avh.left != null)
            avh.left.setOnClickListener(getListenerForAvatar(avh));

        if (avh.button != null)
            avh.button.setOnClickListener(new ButtonListener(avh));

        if (avh.checkBox != null) {
            avh.checkBox.setOnCheckedChangeListener(
                    new CheckBoxChangeListenerWrapper(this, avh));
            ListItemType type = ListItemType.findTypeByResId(i);
            View v;
            if (type.beneathCheckBoxId == R.id.imageView)
                v = avh.left;
            else
                v = avh.button;
            if (v == null)
                return avh;
            if (areCheckBoxesShown()) {
                v.setVisibility(View.INVISIBLE);
                avh.checkBox.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.VISIBLE);
                avh.checkBox.setVisibility(View.INVISIBLE);
            }
        }
        return avh;
    }

    @Override
    public void onBindViewHolder(AwesomeViewHolder awesomeViewHolder, int i) {
        int ii = getAdapterPositionForViewHolder(awesomeViewHolder);
        super.onBindViewHolder(awesomeViewHolder, i);
        if (ii < 0) //In this case, I is the headerview
            return;
        if (getListItemDataType(ii) == ListItemType.SUB_HEADER)
            return;
        View v; //The view that is below the checkbox
        if (getListItemDataType(ii).beneathCheckBoxId == R.id.imageView) {
            v = awesomeViewHolder.left;
        } else
            v = awesomeViewHolder.button;

        if (v == null) {
            return;
        }
        if (awesomeViewHolder.checkBox == null) {
            return;
        }
        if (areCheckBoxesShown() && showCheckboxOnItem(ii)) {
            //Alright, show the checkbox
            ViewUtils.animateSetViewVisibilityVisible(awesomeViewHolder.checkBox);
            awesomeViewHolder.checkBox.setChecked(isChecked(
                    getAdapterPositionForViewHolder(awesomeViewHolder)));
            ViewUtils.animateSetViewVisibilityGone(v);
        } else {
            //Hide it
            ViewUtils.animateSetViewVisibilityGone(awesomeViewHolder.checkBox);
            ViewUtils.animateSetViewVisibilityVisible(v);
        }

    }

    /**
     * Whether or not a certain item should have a checkbox displayed when in checking mode.
     * Default implementation always returns true;
     */
    public boolean showCheckboxOnItem(int position) {
        return true;
    }

    /**
     * Whether or not the itemView's onclickListener and onLongClickListener should be called
     * when the adapter is in the checkbox mode.
     * If set to true, when the user clicks a listitem in the checkbox mode,
     * the item will be checked/unchecked.
     * Default value is true;
     *
     * @param disable Whether or not the listeners provided in
     *                {@link BaseAdapter#setOnItemClickListener(OnItemClickListener)} and
     *                {@link BaseAdapter#setOnItemLongClickListener(OnItemLongClickListener)}
     *                should be called in checkbox mode.
     */
    public void disableListenersInCheckboxMode(boolean disable) {
        disableClickListenerInCheckBoxMode = disable;
    }

    @Override
    protected void handleClick(AwesomeViewHolder avh) {
        if (areCheckBoxesShown() && disableClickListenerInCheckBoxMode) {
            if (avh.checkBox != null) {
                boolean check = !isChecked(getAdapterPositionForViewHolder(avh));
                checkItem(getAdapterPositionForViewHolder(avh), check, false);
                avh.checkBox.setChecked(check);
            }
        } else
            super.handleClick(avh);
    }

    @Override
    protected boolean handleLongClick(AwesomeViewHolder avh) {
        if (areCheckBoxesShown() && disableClickListenerInCheckBoxMode) {
            if (avh.checkBox != null) {
                boolean check = !isChecked(getAdapterPositionForViewHolder(avh));
                checkItem(getAdapterPositionForViewHolder(avh), check, false);
                avh.checkBox.setChecked(check);
            }
            return true;
        } else
            return super.handleLongClick(avh);
    }

    //TODO CODE BELOW ISN'T TOUCHED SINCE IT WAS WRITTEN -> IT'S A MESS, PLEASE DON'T JUDGE

    public void setOnAvatarClickListener(AvatarClickListener listener) {
        clickListener = listener;
    }

    //AVATAR RELATED STUFF HERE

    protected View.OnClickListener getListenerForAvatar(AwesomeViewHolder avh) {
        return new AvatarListener(avh);
    }

    public void setButtonOnClickListener(ListButtonClickListener listener) {
        this.bttnListener = listener;
    }

    /**
     * Handles a check/uncheck.
     */
    interface CheckedStateListener {
        /**
         * Called when the checkbox's state changed.
         *
         * @param itemId  List item's id, the same as returned by
         *                {@link #getIdForItem(int)} (you should definitely override that method).
         * @param checked Whether or not the checkbox is checked.
         */
        void onItemChecked(long itemId, boolean checked);
    }

    public interface AvatarClickListener {
        void onAvatarClick(ImageView avatar, int index);
    }

    /**
     * Handles a list item button click
     */
    public interface ListButtonClickListener {
        /**
         * Called when the button is clicked.
         *
         * @param button The button
         * @param index  Index of the button in the list.
         */
        void onButtonClick(ImageButton button, int index);
    }


//BUTTON RELATED STUFF HERE

    protected static class CheckBoxChangeListenerWrapper implements CompoundButton.OnCheckedChangeListener {

        protected AwesomeViewHolder vh;
        protected ListAdapter adapter;

        public CheckBoxChangeListenerWrapper(ListAdapter adapter,
                                             AwesomeViewHolder vh) {
            this.vh = vh;
            this.adapter = adapter;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int index = adapter.getAdapterPositionForViewHolder(vh);
            long id = adapter.getIdForItem(index);
            adapter.checkItem(index, isChecked, false);
            if (adapter.getMode() != null) {
                adapter.mode.onItemChecked(id, isChecked);
                if (adapter.disableClickListenerInCheckBoxMode)
                    return;
            }
            if (adapter.getListener() != null)
                adapter.getListener().onItemChecked(id, isChecked);
        }
    }

    /**
     * ActionMode class that, when it is started, makes all the Checkboxes visible.
     * You should only use this class if your adapter, before instantiating this,
     * DOES NOT HAVE THE CHECKBOXES VISIBLE.
     */
    public abstract static class ListCheckingActionMode implements ActionMode.Callback {

        protected ListAdapter listAdapter;
        private ActionMode mode;

        public ListCheckingActionMode(RecyclerView recyclerView) {
            if (recyclerView.getAdapter() instanceof ListAdapter) {
                listAdapter = ((ListAdapter) recyclerView.getAdapter());
                if (listAdapter.areCheckBoxesShown())
                    throw new IllegalArgumentException("Adapter already has checkboxes shown!" +
                            "You've got to handle the ActionMode yourself");
            } else
                throw new IllegalArgumentException("RecyclerView's adapter doesn't support this" +
                        "feature. Try an adapter that extends from ListAdapter.");
        }

        @Override
        public final boolean onCreateActionMode(ActionMode mode, Menu menu) {
            this.mode = mode;
            listAdapter.setShowCheckBoxes(true);
            listAdapter.notifyDataSetChanged();
            listAdapter.setMode(this);
            updateTitle();
            return true;
        }

        @Override
        public abstract boolean onPrepareActionMode(ActionMode mode, Menu menu);

        @Override
        public abstract boolean onActionItemClicked(ActionMode mode, MenuItem item);

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listAdapter.uncheckAll();
            listAdapter.setShowCheckBoxes(false);
            listAdapter.setMode(null);
        }

        private void updateTitle() {
            mode.setTitle(listAdapter.checkedIds.size() + " item(s) selected");
        }

        public void onItemChecked(long id, boolean checked) {
            if (listAdapter.checkedIds.size() == 0)
                mode.finish();
            updateTitle();
            onListItemCheckedStateChange(id, checked);
        }

        /**
         * @deprecated
         */
        public abstract void onListItemCheckedStateChange(long id, boolean checked);

    }

    private class AvatarListener implements View.OnClickListener {

        private AwesomeViewHolder avh;

        public AvatarListener(AwesomeViewHolder avh) {
            this.avh = avh;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null && getMode() == null)
                clickListener.onAvatarClick((ImageView) v, getAdapterPositionForViewHolder(avh));
        }
    }

    private class ButtonListener implements View.OnClickListener {

        protected AwesomeViewHolder avw;

        public ButtonListener(AwesomeViewHolder vh) {
            avw = vh;
        }

        @Override
        public void onClick(View v) {
            if (bttnListener != null)
                bttnListener.onButtonClick((ImageButton) v, getAdapterPositionForViewHolder(avw));
        }
    }
}
