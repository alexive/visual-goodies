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

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alexive.graphicalutils.view.CardBuilder;
import com.alexive.graphicalutils.view.ViewUtils;

public class CardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //On a real project, you'd define the layout in an xml file
        LinearLayout mLinearLayout = new LinearLayout(this);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView sv = new ScrollView(this);
        sv.addView(mLinearLayout);
        setContentView(sv);

        CardBuilder mCardBuilder = new CardBuilder(CardBuilder.CardType.FULL_WIDTH_IMAGE);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        int sixteenDP = ViewUtils.convertDPtoPixels(this, 16);
        params.setMargins(sixteenDP, sixteenDP / 2, sixteenDP, sixteenDP / 2);

        mCardBuilder.setTitle("Card title here")
                .setSubTitle("subtitle here")
                .addSupplementalAction(new CardBuilder.CardAction("A1", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CardsActivity.this, "Action 1 pressed", Toast.LENGTH_LONG).show();
                    }
                }))
                .addSupplementalAction(new CardBuilder.CardAction("A2", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CardsActivity.this, "Action 2 pressed", Toast.LENGTH_LONG).show();
                    }
                }))
                .setText("Text here! - To create a card like this use the " +
                        "FULL_WIDTH_IMAGE card type")
                .setImage(ContextCompat.getDrawable(this, R.drawable.lisbon));

        mLinearLayout.addView(mCardBuilder.build(this), params);

        mCardBuilder.setType(CardBuilder.CardType.IMAGE_AS_BACKGROUND)
                .useLightTheme(false) //This one supports dark theme only
                .setText("Text here. Use IMAGE_AS_BACKGROUND for a card like this one");

        mLinearLayout.addView(mCardBuilder.build(this), params);

        mCardBuilder.setType(CardBuilder.CardType.IMAGE_FILLS_WITH_ACTIONS_ON_LEFT)
                .useLightTheme(true)
                .setText("Text here. IMAGE_NEXT_TO_TITLE here");

        mLinearLayout.addView(mCardBuilder.build(this), params);


    }
}
