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

/**
 * Defines the Type of view to be drawn at a certain position in an adapter.
 * This was my first approach to various types of list items. A few months later, ListItemType was born.
 * This enum was kept for compatibility (and in order not to include HEADER in the ListItemType enum)
 */
public enum AwesomeViewTypes {
    /**
     * Ordinary itemView
     */
    REGULAR(0),

    /**
     * Subheader.
     */
    SUB_HEADER(1),

    HEADER(-1);

    public int code;

    AwesomeViewTypes(int code){
        this.code = code;
    }
}
