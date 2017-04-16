package ru.boyda.popov.homework_1;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class MainActivity extends AppCompatActivity {

    private View battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        battery = findViewById(R.id.battery);

        ValueAnimator mAnimator = ValueAnimator.ofInt(0, 10000);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new DecelerateInterpolator(2));
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                battery.getBackground().setLevel(value);

            }
        });
        mAnimator.start();
    }

}
