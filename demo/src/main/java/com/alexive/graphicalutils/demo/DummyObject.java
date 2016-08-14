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

/**
 * Dummy object used to actually give the list some content
 */
public class DummyObject {

    private String name;
    private String description;
    private Object avatar;

    public DummyObject(String name, String description, Object drawable) {
        this.name = name;
        this.description = description;
//        if (!(drawable instanceof TextDrawable || drawable instanceof Integer))
//            throw new RuntimeException("3rd param must be Integer or TextDrawable");
        avatar = drawable;

    }

    public Object getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
