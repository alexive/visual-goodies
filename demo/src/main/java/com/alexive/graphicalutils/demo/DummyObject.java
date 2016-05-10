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

import android.graphics.drawable.Drawable;

/**
 * Dummy object used to actually give the list some content
 */
public class DummyObject {

    private String name;
    private String description;
    private Drawable avatar;

    public DummyObject(String name, String description, Drawable drawable){
        this.name = name;
        this.description = description;
        avatar = drawable;

    }

    public Drawable getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
