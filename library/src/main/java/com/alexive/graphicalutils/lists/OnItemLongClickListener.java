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

import android.view.View;

/**
 * Handles list item long clicks.
 */
public interface OnItemLongClickListener {

  /**
   * Called when the user long clicks an item.
   * @param itemView Item's view.
   * @param position Index of the item in the list.
   */
  boolean onItemLongClick(View itemView, int position);
}
