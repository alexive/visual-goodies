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
 * will contain, per exame {@link #ONE_TEXT_AND_AVATAR} will have a textview and an ImageView (avatar)
 * on the left side.
 */
public enum ListItemType {

    SINGLE_TEXT(R.layout.listem_item_one_text, 0),
    TWO_TEXTS(R.layout.list_item_two_texts, 0),

    ONE_TEXT_AND_ICON(R.layout.listem_item_one_text_icon, R.id.imageView),
    TWO_TEXTS_AND_ICON(R.layout.listem_item_two_text_icon, R.id.imageView),

    ONE_TEXT_AND_AVATAR(R.layout.single_avatar_text, R.id.imageView),
    TWO_TEXTS_AND_AVATAR(R.layout.double_avatar_text, R.id.imageView),

    ONE_TEXT_WITH_AVATAR_AND_BUTTON(R.layout.single_avatar_button_text, R.id.imageButton),
    TWO_TEXTS_WITH_AVATAR_AND_BUTTON(R.layout.double_avatar_button_text, R.id.imageButton),

    GRID_ITEM(0, 0),
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
