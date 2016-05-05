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

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.alexive.graphicalutils.R;

/**
 * This avoids creating an activity when you have to ask the user for an input.
 * Shows a dialog asking the user to write something.
 */
public class TextInputDialog{

    private boolean shouldBlankSpaceBeAccepted;
    private InputPolicy inputPolicy;
    private TextListener listener;
    private AlertDialog dialog;
    private Watcher watcher;
    private EditText editText;
    private TextInputLayout mDialogView;
    private DialogInterface.OnClickListener positiveButtonListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onTextAccepted(getText());
                }
            };
    private AlertDialog.Builder mDialog;

    /**
     * Creates a new TextInputDialog
     * @param context
     */
    public TextInputDialog(Context context){
        mDialog = new AlertDialog.Builder(context);
        this.mDialogView = (TextInputLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_text_input, null);

        editText = mDialogView.getEditText();
        this.mDialog.setView(this.mDialogView);
    }

    /**
     * {@link android.app.AlertDialog.Builder#setTitle(CharSequence)}
     */
    public TextInputDialog setTitle(CharSequence title){
        this.mDialog.setTitle(title);
        return this;
    }

    /**
     * {@link android.app.AlertDialog.Builder#setTitle(int)}
     */
    public TextInputDialog setTitle(int titleResId){
        this.mDialog.setTitle(titleResId);
        return this;
    }

    /**
     * {@link android.app.AlertDialog.Builder#setCustomTitle(View)}
     */
    public TextInputDialog setCustomTitle(View customTitle){
        this.mDialog.setCustomTitle(customTitle);
        return this;
    }

    /**
     * Sets the dialog's text field's hint. This will be displayed when the text field is empty.
     * {@link EditText#setHint(CharSequence)}
     */
    public TextInputDialog setTextHint(CharSequence hint){
        mDialogView.setHint(hint);
        return this;
    }

    /**
     * This will set the error message on the dialog. It'll appear below the text field
     *
     * @param errorMessage
     */
    private void setErrorMessage(CharSequence errorMessage){
        mDialogView.setError(errorMessage);
        mDialogView.setErrorEnabled(errorMessage != null);
    }

    public String getText(){
        return mDialogView.getEditText().getText().toString();
    }

    /**
     * Sets an initial text to be displayed in the dialog's textView
     */
    public TextInputDialog setText(CharSequence text){
        editText.setText(text);
        editText.setSelection(0, editText.getText().length());
        return this;
    }

    /**
     * Sets an input policy. The text the user writes will be checked by the policy supplied and
     * the user will only be able to submit the text if the policy supplied by this methods lets them.
     * See {@link InputPolicy}
     */
    public TextInputDialog setInputPolicy(InputPolicy inputPolicy){
        this.inputPolicy = inputPolicy;
        return this;
    }

    /**
     * Sets whether or not the positive button of the dialog is visible when
     * the textview only contains blank space
     */
    public TextInputDialog setAllowBlankSpace(boolean shouldBlankSpaceBeAccepted){
        this.shouldBlankSpaceBeAccepted = shouldBlankSpaceBeAccepted;
        return this;
    }

    /**
     * Shows the dialog.
     * @param listener What to do when the dialog is accepted.
     */
    public TextInputDialog show(TextListener listener){
        this.listener = listener;
        watcher = new Watcher();
        mDialogView.getEditText().addTextChangedListener(watcher);
        this.dialog = mDialog.create();
        this.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //Just to update the positive's button visibility in case the dev want's
                //just non blank-space input.
                watcher.onTextChanged(mDialogView.getEditText().getText().toString(), 0, 0, 0);
            }
        });
        this.dialog.show();
        return this;
    }

    /**
     * @see AlertDialog.Builder#setNegativeButton(CharSequence, DialogInterface.OnClickListener)
     */
    public TextInputDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener
            listener){
        this.mDialog.setNegativeButton(text, listener);
        return this;
    }

    /**
     * @see AlertDialog.Builder#setNegativeButton(int, DialogInterface.OnClickListener)
     */
    public TextInputDialog setNegativeButton(int txtResId, DialogInterface.OnClickListener listener){
        this.mDialog.setNegativeButton(txtResId, listener);
        return this;
    }

    /**
     * @see AlertDialog.Builder#setPositiveButton(CharSequence, DialogInterface.OnClickListener)
     */
    public TextInputDialog setPositiveButton(CharSequence text){
        this.mDialog.setPositiveButton(text, positiveButtonListener);
        return this;
    }

    /**
     * @see AlertDialog.Builder#setPositiveButton(int, DialogInterface.OnClickListener)
     */
    public TextInputDialog setPositiveButton(int txtResId){
        this.mDialog.setPositiveButton(txtResId, positiveButtonListener);
        return this;
    }

    /**
     * Enables or disables the dialog's positive button
     * @param enabled Whether or not to enable the button
     */
    private void setPositiveButtonEnabled(boolean enabled){
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
    }

    /**
     * Observes the input
     */
    public interface InputPolicy {

        /**
         * Whether or not the text the user just typed should be accepted.
         *
         * @param text What the user has typed.
         * @return null if what the user typed is okay. If not, return an error message
         * containing indications so that the user can correct the text.
         */
        String isTextAcceptable(String text);

    }

    public interface TextListener {

        /**
         * Called when the user taps the positive button.
         *
         * @param inputText What the user typed.
         */
        void onTextAccepted(String inputText);

    }

    private class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString();
            if (!shouldBlankSpaceBeAccepted && text.trim().equals("")){
                setPositiveButtonEnabled(false);
                return;
            }
            if (inputPolicy == null){
                setPositiveButtonEnabled(true);
                return;
            }
            String errMsg = inputPolicy.isTextAcceptable(text);
            setErrorMessage(errMsg);
            if (errMsg != null)
                setPositiveButtonEnabled(false);
            else if ((text.trim().equals("") && !shouldBlankSpaceBeAccepted))
                setPositiveButtonEnabled(false);
            else
                setPositiveButtonEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
