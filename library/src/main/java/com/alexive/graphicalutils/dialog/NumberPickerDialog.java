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

package com.alexive.graphicalutils.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.alexive.graphicalutils.R;

/**
 * Shows a dialog asking the user to choose a number.
 */
public class NumberPickerDialog {

    protected final Context context;
    protected NumberPicker mNumberPicker;
    protected EditText mNumberField;
    private int min;
    private int max;
    private AlertDialog.Builder mDialog;
    /**
     * Creates a new NumberPickerDialog
     * @param context
     * @param min The minimum value the user can choose
     * @param max The maximum value
     */
    public NumberPickerDialog(Context context, int min, int max){
        mDialog = new AlertDialog.Builder(context);
        this.context = context;
        this.min = min;
        this.max = max;
    }

    /**
     * {@link android.app.AlertDialog.Builder#setTitle(CharSequence)}
     */
    public NumberPickerDialog setTitle(CharSequence title){
        this.mDialog.setTitle(title);
        return this;
    }

    /**
     * {@link android.app.AlertDialog.Builder#setTitle(int)}
     */
    public NumberPickerDialog setTitle(int titleResId){
        this.mDialog.setTitle(titleResId);
        return this;
    }

    /**
     * {@link android.app.AlertDialog.Builder#setCustomTitle(View)}
     */
    public NumberPickerDialog setCustomTitle(View customTitle){
        this.mDialog.setCustomTitle(customTitle);
        return this;
    }

    /**
     * {@link AlertDialog.Builder#show()}
     * @param listener Called when the user pics a number, or when the user cancels the dialog
     * @return
     */
    public NumberPickerDialog show(final NumberPickerListener listener){
        View mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_number_picker, null);
        this.mDialog.setView(mDialogView);
        if (Build.VERSION.SDK_INT >= 11){
            //NumberPicker
            mNumberPicker = (NumberPicker) mDialogView.findViewById(R.id.picker);
            mNumberPicker.setMaxValue(max);
            mNumberPicker.setMinValue(min);
        }else{
            mNumberField = (EditText) mDialogView.findViewById(android.R.id.text1);
            mNumberField.setText(min + "");
            mDialogView.findViewById(R.id.buttonLess).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(mNumberField.getText().toString()) - 1;
                    mNumberField.setText(String.valueOf(number >= min ? number : max));
                }
            });
            mDialogView.findViewById(R.id.buttonMore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(mNumberField.getText().toString()) + 1;
                    mNumberField.setText(String.valueOf(number <= max ? number : min));
                }
            });
        }
        this.mDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onCancelled();
            }
        }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNumberAccepted(Build.VERSION.SDK_INT >= 11 ? mNumberPicker.getValue() :
                        Integer.parseInt(mNumberField.getText().toString()));
            }
        }).show();
        return this;

    }

    public interface NumberPickerListener{
        void onNumberAccepted(int number);
        void onCancelled();
    }

}
