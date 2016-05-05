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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;

/**
 * Transforms plenty of methods (that usually require chiecking the sdk version, etc)
 * to a single line method call.
 */
public class ViewUtils {

    /**
     * Set the background to a given view, or remove it.
     * @param view The given view.
     * @param drawable The Drawable to be set as the background, or null to remove
     *                 the view's background.
     * @see View#setBackground(Drawable)
     */
    public static void setBackgroundDrawable(View view, Drawable drawable){
        if (Build.VERSION.SDK_INT >= 16)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    /**
     * Sets the view visibility as {@link View#GONE} with an animation.
     * @param view The view to animate/set as GONE.
     */
    public static void animateSetViewVisibilityGone(final View view){
        if (view.getVisibility() == View.GONE)
            return;
        if (Build.VERSION.SDK_INT < 12)
            view.setVisibility(View.GONE);
        else {
            ViewPropertyAnimator animator = view.animate();
//      animator.cancel(); //Only on API 14 cuz of this!
            animator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @SuppressLint("NewApi")
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    view.setScaleX(0);
                    view.setScaleY(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimationEnd(animation);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.scaleX(0).scaleY(0);
        }
    }

    /**
     * Sets the view's visibility as {@link View#VISIBLE} with an animation.
     * @param view The view to animate and set visible.
     */
    public static void animateSetViewVisibilityVisible(final View view){
        if (view.getVisibility() == View.VISIBLE)
            return;
        if (Build.VERSION.SDK_INT < 12){
            view.setVisibility(View.VISIBLE);
        }else {
            ViewPropertyAnimator animator = view.animate();
//      animator.cancel(); //Only on API 14 cuz of this!
            animator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                    //The view is hidden if animateSetViewVisibilityGone has
                    //been called before
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onAnimationCancel(animation);
                }

                @SuppressLint("NewApi")
                @Override
                public void onAnimationCancel(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                    view.setScaleX(1);
                    view.setScaleY(1);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.scaleX(1).scaleY(1);
        }
    }

    public static void createCircularReveal(View view, int cx, int cy, int radius){
        if (Build.VERSION.SDK_INT < 21){
            animateSetViewVisibilityVisible(view);
            return;
        }else {
            view.setVisibility(View.VISIBLE);
            //Clear traces from animateSetViewVisibility*
            if (Build.VERSION.SDK_INT >= 12) {
                view.setScaleY(1);
                view.setScaleX(1);
            }
            // get the center for the clipping circle

// get the final radius for the clipping circle

            Log.e("ViewUtils", "finalRadius = " + radius);

// create the animator for this view (the start radius is zero)
            try {

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
// make the view visible and start the animation
                anim.start();
            }catch (IllegalStateException ex){
                ex.printStackTrace();
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void createCircularReveal(View view){
        createCircularReveal(view, (view.getLeft() + view.getRight()) / 2,
                (view.getTop() + view.getBottom()) / 2,
                Math.max(view.getWidth(), view.getHeight()));
    }

    public static void createCircularRevealInverse(final View view,
                                                   int cx,
                                                   int cy,
                                                   int initialRadius,
                                                   final boolean setAsGone){
        if (Build.VERSION.SDK_INT < 21){
            if (setAsGone)
                animateSetViewVisibilityGone(view);
            else
                view.setVisibility(View.INVISIBLE);
        }else{
            view.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 12) {
                view.setScaleY(1);
                view.setScaleX(1);
            }

            try {
// create the animation (the final radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

// make the view invisible when the animation is done
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(setAsGone ? View.GONE : View.INVISIBLE);
                    }
                });

// start the animation
                anim.start();
            }catch (IllegalStateException ex) {
                ex.printStackTrace();
                view.setVisibility(setAsGone ? View.GONE : View.INVISIBLE);
            }
        }
    }

    public static void createCircularRevealInverse(final View view, final boolean setAsGone){
        createCircularRevealInverse(view,
                (view.getLeft() + view.getRight()) / 2,
                (view.getTop() + view.getBottom()) / 2,
                view.getWidth(),
                setAsGone);

    }

    public static int convertDPtoPixels(Context c, int dp){
        float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
