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

package com.alexive.graphicalutils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexive.graphicalutils.R;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * ----------------------------------------------------------------
 * ----------------------------------------------------------------
 * STILL IN EARLY DEVELOPMENT - NOT EVEN CLOSE TO FULLY WORKING YET
 * ----------------------------------------------------------------
 * ----------------------------------------------------------------
 *
 * Creates CardViews with a given content. You can use the same instance of a CardBuilder to
 * create different cards, you just have to make the changes from a card to another before
 * calling the build() method again.
 */
public class CardBuilder {


    private CharSequence text;
    private View customMainView;
    private View.OnClickListener mPrimaryAction;
    private String title;
    private String subTitle;
    private Object image = null;
    private ArrayList<CardAction> actions = new ArrayList<>();
    private CardType mCardType;
    private boolean useLightTheme = true;

    public CardBuilder(CardType type) {
        mCardType = type;
    }

    /**
     * Sets the type of the next card returned by a call to
     * {@link #build(Context)}. Please note that information may be lost when exchanging the
     * card's type
     */
    public CardBuilder setType(CardType type) {
        if (type == CardType.NO_IMAGE)
            image = null;
        this.mCardType = type;
        return this;
    }


    /**
     * @param listener What happens when the user clicks the card.
     * @return The caller instance of CardBuilder
     */
    public CardBuilder setPrimaryAction(View.OnClickListener listener) {
        mPrimaryAction = listener;
        return this;
    }

    /**
     * Sets the title of the card. The title will be in a TextView (id = android.R.id.text1).
     *
     * @param title Card's title
     * @return
     */
    public CardBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the card's subtitle. It will be in a TextView (id = android.R.id.text2)
     *
     * @param subTitle The card's subtitle
     * @return
     */
    public CardBuilder setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    /**
     * Sets the image for the card.
     *
     * @param bitmap The image
     * @return
     */
    public CardBuilder setImage(Bitmap bitmap) {
        this.image = bitmap;
        return this;
    }

    /**
     * @see #setImage(Bitmap)
     */
    public CardBuilder setImage(Drawable drawable) {
        this.image = drawable;
        return this;
    }

    /**
     * Adds a supplemental Action to the card. A supplemental action is basically a button with text or
     * an icon.
     *
     * @return
     */
    public CardBuilder addSupplementalAction(CardAction action) {
        actions.add(action);
        return this;
    }

    /**
     * Sets the card's text.
     *
     * @param text The card's text.
     * @return
     */
    public CardBuilder setText(CharSequence text) {
        this.text = text;
        return this;
    }

    /**
     * Adds a custom view to the card. It will be displayed after the title and before the text and/or actions.
     *
     * @param customMainView Custom view to be added to the card.
     * @return
     */
    public CardBuilder addCustomViewView(View customMainView) {
        this.customMainView = customMainView;
        return this;
    }

    /**
     * Resets this CardBuilder. The only thing kept it the CardType provided in the constructor.
     */
    public CardBuilder reset() {
        this.customMainView = null;
        actions.clear();
        useLightTheme = true;
        text = null;
        title = null;
        subTitle = null;
        image = null;
        mPrimaryAction = null;
        return this;
    }

    /**
     * Changes the card's theme. This affects background and text color. Note that you can still change
     * the card's background later, using {@link CardView#setCardBackgroundColor(int)}
     *
     * @param whiteCard If true (default), the card's background will be light and the text's color black.
     *                  Otherwise, it'll have a dark background and light text color.
     */
    public CardBuilder useLightTheme(boolean whiteCard) {
        this.useLightTheme = whiteCard;
        return this;
    }

    /**
     * Build a card given the information you passed by calling all the other methods in this class.
     * If you call build and a few other methods after calling build again, the second call will return the card with
     * the original settings AND the settings you changed before the second call.
     *
     * @param context
     * @return
     */
    public CardView build(Context context) {
        CardView mCardView = (CardView) LayoutInflater.from(context).inflate(mCardType.layoutResId, null);
        mCardView.setTag(mCardType.name());
        return build(context, mCardView, true, true, true);
    }

    public CardView build(Context context, CardView mCardView,
                          boolean overrideBackground,
                          boolean overrideElevation,
                          boolean overrideRadius) {
        if (!mCardView.getTag().toString().equals(mCardType.name()))
            throw new IllegalArgumentException("Attempting to recycle a card of another type.");
        LinkedList<TextView> textViews = new LinkedList<>();
        View view = mCardView.findViewById(android.R.id.text1);
        if (view != null) {
            textViews.add(((TextView) view));
            ((TextView) view).setText(title);
            view.setVisibility(title != null ? View.VISIBLE : View.GONE);
        }

        view = mCardView.findViewById(android.R.id.text2);
        if (view != null) {
            textViews.add(((TextView) view));
            ((TextView) view).setText(subTitle);
            view.setVisibility(subTitle != null ? View.VISIBLE : View.GONE);
        }


        view = mCardView.findViewById(R.id.content);
        if (view != null) {
            textViews.add(((TextView) view));
            ((TextView) view).setText(text);
            view.setVisibility(text != null ? View.VISIBLE : View.GONE);
        }

        mCardView.setOnClickListener(mPrimaryAction);
        //TODO add the custom view

        view = mCardView.findViewById(android.R.id.content);
        if (view != null) {
            if (image instanceof Bitmap)
                ((ImageView) view).setImageBitmap((Bitmap) image);
            else if (image instanceof Drawable)
                ((ImageView) view).setImageDrawable((Drawable) image);
            else
                ((ImageView) view).setImageDrawable(null);
        }

        LinearLayout mActionContainer = (LinearLayout) mCardView.findViewById(R.id.action_container);
        if (actions.isEmpty())
            mActionContainer.setVisibility(View.GONE);
        else {
            mActionContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i < actions.size(); i++) {
                CardAction action = actions.get(i);
                int j = i;
                View optimalView = null;

                while (j < mActionContainer.getChildCount()) {
                    optimalView = mActionContainer.getChildAt(j);
                    if ((action.drawable != null && optimalView instanceof ImageButton) || (action.title !=
                            null && optimalView instanceof Button)) {
                        mActionContainer.removeViewAt(j);
                        mActionContainer.addView(optimalView, i);
                        break;
                    } else {
                        optimalView = null;
                        j++;
                    }
                }
                if (optimalView == null) {
                    if (action.title != null){
                        Button button = (Button) LayoutInflater.from(context).inflate(R.layout.card_bttn, null);
                        button.setText(action.title);
                        button.setOnClickListener(action.actionListener);
                        mActionContainer.addView(button);
                        //NASTY workaround for the padding
                        View space = new View(context);
                        mActionContainer.addView(space, new LinearLayout.LayoutParams(
                                ViewUtils.convertDPtoPixels(context, 8),
                                ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }
            }
        }

        if (useLightTheme) {
            if (overrideBackground)
                mCardView.setCardBackgroundColor(0xFFFFFF);
            int textColor = ContextCompat.getColor(context, android.R.color.primary_text_light);
            for (TextView tv : textViews)
                tv.setTextColor(textColor);
        } else {
            if (overrideBackground)
                mCardView.setCardBackgroundColor(0x424242);
            int textColor = ContextCompat.getColor(context, android.R.color.primary_text_dark);
            for (TextView tv : textViews)
                tv.setTextColor(textColor);
        }

        //Material design guidelines: 2dp resting elevation (or 8dp when raised)
        //Let's assume the card is resting
        //Also: 2dp corner radius
        int dPtoPixels = ViewUtils.convertDPtoPixels(context, 2);
        if (overrideElevation)
            mCardView.setCardElevation(dPtoPixels);
        if (overrideRadius)
            mCardView.setRadius(dPtoPixels);

        return mCardView;
    }

    public enum CardType {
        /**
         * No ImageView! (Default)
         */
        NO_IMAGE(R.layout.card_no_image),
        /**
         * The imageView will be the Card's background.
         */
        IMAGE_AS_BACKGROUND(R.layout.card_image_as_background),
        /**
         * The ImageView will stretch to actuallyBindDataToListItem the card's width and will keep a 16:9 ratio.
         */
        FULL_WIDTH_IMAGE(R.layout.card_image_full_width),
        //TODO simple square with title
        /**
         * The image will be displayed at the right. Next to the title and/or subtitle, with proper
         * padding.
         */
        IMAGE_NEXT_TO_TITLE(R.layout.card_image_next_to_title),
        /**
         * The imageView will fill the whole Card's width and height, starting from the top left,
         * leaving space just for the actions, placed on the right.
         */
        IMAGE_FILLS_WITH_ACTIONS_ON_LEFT(R.layout.card_big_image_left);

        protected int layoutResId;

        CardType(int layoutResId) {
            this.layoutResId = layoutResId;
        }
    }

    //TODO add view that expands/collapses when user taps a button


    public static class CardAction {

        private String title;
        private View.OnClickListener actionListener;
        private Drawable drawable;

        public CardAction(String title, View.OnClickListener actionListener) {
            this.title = title;
            this.actionListener = actionListener;
        }

        public CardAction(Drawable drawable, View.OnClickListener actionListener) {
            this.drawable = drawable;
            this.actionListener = actionListener;
        }
    }

}
