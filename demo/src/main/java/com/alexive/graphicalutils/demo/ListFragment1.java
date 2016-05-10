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

package com.alexive.graphicalutils.demo;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexive.graphicalutils.dialog.MessageDialog;
import com.alexive.graphicalutils.dialog.TextInputDialog;
import com.alexive.graphicalutils.fragments.RecyclerViewFragment;
import com.alexive.graphicalutils.lists.Divider;
import com.alexive.graphicalutils.lists.ListAdapter;
import com.alexive.graphicalutils.lists.ListItemType;
import com.alexive.graphicalutils.lists.OnItemClickListener;
import com.alexive.graphicalutils.lists.OnItemLongClickListener;
import com.alexive.graphicalutils.view.CardBuilder;
import com.alexive.graphicalutils.view.ViewUtils;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Displays a list
 */
public class ListFragment1 extends RecyclerViewFragment implements OnItemLongClickListener, OnItemClickListener {

    /**
     * The objects to show on the list. They'll contain a title, a subtitle and an avatar
     */
    private static final Object[] LIST_ITEMS = new Object[]{
            new DummyObject("Lorem ipsum", "Dolor", TextDrawable.builder().buildRound("L",
                    ColorGenerator.MATERIAL.getColor("L"))),
            new DummyObject("asdfgjklç", "qwertyuiop", TextDrawable.builder().buildRound("A",
                    ColorGenerator.MATERIAL.getColor("a"))),
            new DummyObject("abcdef", "12345", TextDrawable.builder().buildRound("A",
                    ColorGenerator.MATERIAL.getColor("A"))),
            "Subheader 1",
            new SimplerDummyObject("Tap to display a dialogue"),
            new SimplerDummyObject("This one'll show a confirmation dialog"),
            new SimplerDummyObject("and this a TextInputDialog"),
            new DummyObject("asdfghjkl", "qwertyuiop", TextDrawable.builder().buildRound("A",
                    ColorGenerator.MATERIAL.getColor("â"))),
            "Subheader 2",
            new DummyObject("Lets see some cards", "Tap to view more", TextDrawable.builder().buildRound("L",
                    ColorGenerator.MATERIAL.getColor("Le"))),
            new SimplerDummyObject("Long press me and see what happens"),
            new SimplerDummyObject("https://github.com/amulyakhare/TextDrawable is amazing")
    };

    private Object[] items = LIST_ITEMS;
    private ListAdapter mListAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Star from here, don't forget to call super!
        setHasOptionsMenu(true);
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mListAdapter = new ListAdapter() {

            @Override
            public String getSubHeaderText(int position) {
                return items[position].toString();
            }

            @Override
            public int getNumItems() {
                return items.length;
            }

            @Override
            public void bindDataToListItem(int index, View itemView, ImageView iconOrAvatar, ImageButton button, TextView... texts) {
                //here you actually compose the list item
                Object current = items[index];
                //This will be the title, it's safe not to check for the type here
                //because every type of list item has at leat one textView
                //The sequence is: top to bottom, left to right
                texts[0].setText(((DummyObject)current).getName());
                if (getListItemDataType(index) == ListItemType.TWO_TEXTS_AND_AVATAR) {
                    texts[1].setText(((DummyObject) current).getDescription());
                    iconOrAvatar.setImageDrawable(((DummyObject) current).getAvatar());
                }

            }

            @Override
            public long getIdForItem(int position) {
                //OVERRIDE THIS METHOD IF YOU'RE USING CHECKBOXES
                //the default implementation just returns position
                //it's good enough ONLY if the items are not changing (being moved/removed)
                return super.getIdForItem(position);
            }

            @Override
            public ListItemType getListItemDataType(int index) {
                if (items[index] instanceof String)
                    return ListItemType.SUB_HEADER;
                else if (items[index] instanceof SimplerDummyObject)
                    return ListItemType.SINGLE_TEXT;
                else
                    return ListItemType.TWO_TEXTS_AND_AVATAR;
            }
        };

        //This styles the dividers
        this.mListAdapter.setDividerColor(Divider.MATERIAL_LIGHT_DIVIDER_COLOR);
        this.mListAdapter.setDividerStyle(Divider.Style.JUST_SUB_HEADERS);
        //
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);

        setAdapter(this.mListAdapter);

        //Note that this one need to be called AFTER the adapter is set!
        setEmptyText("Oh no, the list is empty!");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_listfragment1, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.hide:
                if (items.length != 0){
                    items = new Object[]{};
                }else{
                    items = LIST_ITEMS;
                }
                notifyDataSetChanged();
                return true;
            case R.id.dividers:
                if (!item.isChecked()){
                    item.setChecked(true);
                    mListAdapter.setDividerStyle(Divider.Style.ALL_ITEMS);
                }else {
                    item.setChecked(false);
                    mListAdapter.setDividerStyle(Divider.Style.JUST_SUB_HEADERS);
                }
                notifyDataSetChanged();
                return true;
            case R.id.header:
                addHeaderView();
                notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addHeaderView(){
        //Lets add a header view
        CardBuilder cardBuilder = new CardBuilder(CardBuilder.CardType.FULL_WIDTH_IMAGE);
        cardBuilder.setTitle("This is an header view!")
                .setSubTitle("The cards are still in early development")
                .setText("some text goes \n\nhere!")
                .useLightTheme(false)
                .setPrimaryAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog.show(getActivity(), "You tapped the card", null);
                    }
                })
                .addSupplementalAction(new CardBuilder.CardAction("Action1", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Clicked 1st action", Toast.LENGTH_LONG).show();
                    }
                }))
                .setImage(BitmapFactory.decodeResource(getResources(), R.drawable.lisbon));

        //We want the card to have some padding so let's add it to a linear layout (not mandatory)
        LinearLayout linear = new LinearLayout(getActivity());
        linear.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        int sixteenPXasDP = ViewUtils.convertDPtoPixels(getActivity(), 16);
        linear.setPadding(sixteenPXasDP,
                sixteenPXasDP,
                sixteenPXasDP,
                (int) (sixteenPXasDP * 1.5));
        //This build the card and adds it to the linear layout
        CardView cardView = cardBuilder.build(getActivity());
        linear.addView(cardView);

        cardView.setCardBackgroundColor(Color.parseColor("#FF5722"));
        //Finally, we add the view to the adapter
        mListAdapter.addHeaderView(linear);
    }

    @Override
    public boolean onItemLongClick(View itemView, int position){
        if (position != 10)
            return false;
        ListAdapter.ListCheckingActionMode mode = new ListAdapter.ListCheckingActionMode(getRecyclerView()) {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //Setup the menu here
                menu.add("Action1");
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                //Handle action item clicks here
                return false;
            }

            @Override
            public void onListItemCheckedStateChange(long id, boolean checked) {
                //called when the user checked the item with id = id
            }
        };
        ((AppCompatActivity)getActivity()).startSupportActionMode(mode);
        return true;
    }

    @Override
    public void onItemClicked(View itemView, int position) {
        //You need to count with subheaders here, since they count as a regular list item
        if (position == 4){
            MessageDialog.show(getActivity(),
                    "This is a message dialog",
                    "Lorem ipsum dolor sit amet.");
        } else if (position == 5){
            MessageDialog.askConfirmation(getActivity(),
                    "This is a confirmation dialogue",
                    "Lorem ipsum dolor sit amet.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "User confirmed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (position == 6){
            new TextInputDialog(getActivity())
                    .setTitle("Write something")
                    .setTextHint("Write something except: whatever")
                    .setPositiveButton(android.R.string.ok)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setInputPolicy(new TextInputDialog.InputPolicy() {
                        @Override
                        public String isTextAcceptable(String text) {
                            if (text.toLowerCase().equals("whatever"))
                                return "No. Try again";
                            return null;
                        }
                    }).setAllowBlankSpace(false)
                    .show(new TextInputDialog.TextListener() {
                        @Override
                        public void onTextAccepted(String inputText) {
                            Toast.makeText(getActivity(),
                                    "You wrote " + inputText,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
    }
}
