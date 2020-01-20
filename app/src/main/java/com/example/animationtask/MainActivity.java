package com.example.animationtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private float mDownX;
    private float mDownY;
    private VelocityTracker mVelocityTracker;
    private SpringForce force = new SpringForce();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVelocityTracker = VelocityTracker.obtain();
        force.setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY).
                setStiffness(SpringForce.STIFFNESS_VERY_LOW)
                .setFinalPosition(0);

        final ImageView droid = (ImageView) findViewById(R.id.imageView);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            private final Rect mRect = new Rect();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = event.getX();
                        mDownY = event.getY();
                        mVelocityTracker.addMovement(event);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        droid.getHitRect(mRect);
                        if (mRect.contains((int) x, (int) y)) {
                            droid.setTranslationX(x - mDownX);
                            droid.setTranslationY(y - mDownY);
                            mVelocityTracker.addMovement(event);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // fall-thru
                    case MotionEvent.ACTION_CANCEL:
                        mVelocityTracker.computeCurrentVelocity(1000);
                        if (droid.getTranslationX() != 0) {
                            SpringAnimation animX = new SpringAnimation(droid, SpringAnimation.TRANSLATION_X);
                            animX.setSpring(force);
                            animX.setStartVelocity(mVelocityTracker.getXVelocity());
                            animX.start();
                        }
                        if (droid.getTranslationY() != 0) {
                            SpringAnimation animY = new SpringAnimation(droid, SpringAnimation.TRANSLATION_Y);
                            animY.setSpring(force);
                            animY.setStartVelocity(mVelocityTracker.getYVelocity());
                            animY.start();
                        }
                        mVelocityTracker.clear();
                        return true;
                }
                return false;
            }
        });
    }
}
