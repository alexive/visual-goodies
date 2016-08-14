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

package com.alexive.graphicalutils.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Contains methods to display a dialog with a message and a title (optional)
 */
public class MessageDialog {

    /**
     * Shows a simple message dialog with an OK button
     *
     * @param context
     * @param title   Dialog's title (null ok)
     * @param message Dialog's message
     */
    public static void show(Context context, String title, String message){
        askConfirmation(context, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Shows a simple message dialog with an OK button
     * @param context
     * @param title Dialog's title resource id
     * @param message Dialog's message resource id
     */
    public static void show(Context context, int title, int message){
        show(context, context.getString(title), context.getString(message));
    }

    /**
     * Identical to
     * {@link #askConfirmation(Context, String, String,
     * DialogInterface.OnClickListener, android.content.DialogInterface.OnClickListener)}
     * with a default action (the dialog is closed) when the user presses cancel
     */
    public static void askConfirmation(Context context,
                                       String title,
                                       String message,
                                       DialogInterface.OnClickListener onConfirm) {
        askConfirmation(context, title, message, onConfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Asks the user to confirm something
     * @param context
     * @param title Dialog's title
     * @param message Dialog's message
     * @param onConfirm Called if the user chose OK
     * @param onCancel Called if the user chose Cancel
     */
    public static void askConfirmation(Context context, String title, String message,
                                       DialogInterface.OnClickListener onConfirm,
                                       DialogInterface.OnClickListener onCancel){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, onConfirm)
                .setNegativeButton(android.R.string.cancel, onCancel)
                .show();
    }
}
