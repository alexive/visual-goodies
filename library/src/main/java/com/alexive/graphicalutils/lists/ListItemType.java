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

import com.alexive.graphicalutils.R;

/**
 * Lists the various types of list items. Each value basically says what an item of that type
 * will contain, per example {@link #ONE_TEXT_AND_AVATAR} will have a textview and an ImageView (avatar)
 * on the left side.
 */
public enum ListItemType {

    /**
     * A list item with only one TextViews. Doesn't contain a CheckBox
     */
    SINGLE_TEXT(R.layout.listem_item_one_text, 0),
    /**
     * Same as {@link #SINGLE_TEXT} but with two TextViews
     */
    TWO_TEXTS(R.layout.list_item_two_texts, 0),

    /**
     * An item with a TextView and an ImageView (for an icon/avatar) on the left. Contains a
     * Checkbox behind the ImageView
     */
    ONE_TEXT_AND_AVATAR(R.layout.single_avatar_text, R.id.imageView),
    /**
     * Same as {@link #ONE_TEXT_AND_AVATAR} but with two TextViews
     */
    TWO_TEXTS_AND_AVATAR(R.layout.double_avatar_text, R.id.imageView),

    /**
     * An item with a TextView, an ImageView on the left (for an icon/avatar)
     * and an ImageButton on the right. Contains a Checkbox behind the Button
     */
    ONE_TEXT_WITH_AVATAR_AND_BUTTON(R.layout.single_avatar_button_text, R.id.imageButton),
    /**
     * Same as {@link #ONE_TEXT_WITH_AVATAR_AND_BUTTON} but with two TextViews
     */
    TWO_TEXTS_WITH_AVATAR_AND_BUTTON(R.layout.double_avatar_button_text, R.id.imageButton),

    /**
     * Ordinary GridItem that takes a single cell
     */
    GRID_ITEM(0, 0),
    /**
     * Subheader. Contains a single TextView. On a grid, it spans all columns
     */
    SUB_HEADER(1,AwesomeViewTypes.SUB_HEADER_NORMAL.code);


    protected int resId;
    protected int beneathCheckBoxId;

    ListItemType(int resId, int beneathCheckBoxId) {
        this.resId = resId;
        this.beneathCheckBoxId = beneathCheckBoxId;
    }

    protected static ListItemType findTypeByResId(int resId){
        for (ListItemType val : ListItemType.values())
            if (val.resId == resId)
                return val;
        return null;
    }
}
