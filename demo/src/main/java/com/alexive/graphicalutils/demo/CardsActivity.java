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
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alexive.graphicalutils.view.CardBuilder;
import com.alexive.graphicalutils.view.ViewUtils;

public class CardsActivity extends AppCompatActivity implements CardBuilder.CardActionClickListener {

    private LinearLayout.LayoutParams params;
    private CardBuilder mCardBuilder;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //On a real project, you'd define the layout in an xml file
        this.mLinearLayout = new LinearLayout(this);
        this.mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView sv = new ScrollView(this);
        sv.addView(this.mLinearLayout);
        setContentView(sv);

        this.mCardBuilder = new CardBuilder(CardBuilder.CardType.FULL_WIDTH_IMAGE);
        this.params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int sixteenDP = ViewUtils.convertDPtoPixels(this, 16);
        this.params.setMargins(sixteenDP, sixteenDP / 2, sixteenDP, sixteenDP / 2);

        Drawable cardImage = ContextCompat.getDrawable(this, R.drawable.lisbon);
        this.mCardBuilder.setTitle("Card title here")
                .setSubTitle("subtitle here")
                .addSupplementalAction(new CardBuilder.CardAction("A1", 1))
                .addSupplementalAction(new CardBuilder.CardAction("A2", 2))
                .addActionClickListener(this)
                .setText("Text here! - To create a card like this use the " +
                        "FULL_WIDTH_IMAGE card type")
                .setImage(cardImage);

        addCardToLayout();

        this.mCardBuilder.setType(CardBuilder.CardType.IMAGE_AS_BACKGROUND)
                .useLightTheme(false) //This one supports dark theme only
                .setText("Text here. Use IMAGE_AS_BACKGROUND for a card like this one");

        addCardToLayout();

        this.mCardBuilder.setType(CardBuilder.CardType.IMAGE_FILLS_WITH_ACTIONS_ON_LEFT)
                .useLightTheme(true)
                .setText("Text here. IMAGE_FILLS_WITH_ACTIONS_ON_LEFT. " +
                        "Notice this one doesn't display text");

        addCardToLayout();

        this.mCardBuilder.setType(CardBuilder.CardType.IMAGE_NEXT_TO_TITLE)
                .setText("Notice the previous one (IMAGE_FILLS_WITH_ACTIONS_ON_LEFT) " +
                        "doesn't show any text. This one's a IMAGE_NEXT_TO_TITLE sample");

        addCardToLayout();


        this.mCardBuilder.reset(); //This clears the builder, you'll have to set it up again
        this.mCardBuilder.setType(CardBuilder.CardType.IMAGE_FILLS_WITH_ACTIONS_ON_LEFT)
                .setImage(cardImage)
                .addSupplementalAction(new CardBuilder.CardAction(
                        ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp), 3))
                .addSupplementalAction(new CardBuilder.CardAction(
                        ContextCompat.getDrawable(this, R.drawable.ic_info_outline_black_24dp), 4))
                .addActionClickListener(this);

        addCardToLayout();

        this.mCardBuilder.setType(CardBuilder.CardType.FULL_WIDTH_IMAGE)
                .setTitle("Look, icons!")
                .setText("You can use icons as actions instead of text (or mix both)")
                .addSupplementalAction(new CardBuilder.CardAction("A5", 5));

        addCardToLayout();

        this.mCardBuilder.setType(CardBuilder.CardType.NO_IMAGE)
                .setTitle("<Title>")
                .setText("This is the simplest one: NO_IMAGE");
        addCardToLayout();


    }

    private void addCardToLayout() {
        mLinearLayout.addView(this.mCardBuilder.build(this), this.params);
    }

    @Override
    public void onCardActionClicked(CardBuilder.CardAction action) {
        //Clicks on the cards actions will be handled by this method
        //Usually you'd pass a resource id to a cardAction, but in this sample I passed
        //1..5
        Toast.makeText(this, "Action #" + action.getId() + " pressed!", Toast.LENGTH_LONG).show();
    }
}
