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

/**
 * Created by jmoliveira on 03-05-2016.
 */
public class Divider {

    /**
     * List divider color for the material light theme, as of the material design guidelines
     * (Black + 12% transparency)
     */
    public static final int MATERIAL_LIGHT_DIVIDER_COLOR = Color.argb(31, 0, 0, 0);

    /**
     * List divider color for the material dark theme, as of the material design guidelines
     * (White + 12% transparency)
     */
    public static final int MATERIAL_DARK_DIVIDER_COLOR = Color.argb(31, 255, 255, 255);

    /**
     * Divider Styles. See {@link #ALL_ITEMS}, {@link #JUST_SUB_HEADERS} and
     * {@link #NONE}.
     */
    public enum Style {
        /**
         * All items have dividers (except the last one).
         */
        ALL_ITEMS,
        /**
         * Only items BEFORE subheaders have dividers
         */
        JUST_SUB_HEADERS,
        /**
         * No one has dividers.
         */
        NONE
    }
}
