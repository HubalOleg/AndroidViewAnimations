package com.oleg.hubal.registrationviewtransitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by User on 27.03.2017.
 */

public class MainFragment extends Fragment implements BackableFragment {

    public static final String BUNDLE_IS_VISIBLE = "BUNDLE_IS_VISIBLE";

    private ChangeFragmentListener mChangeFragmentListener;
    private LinearLayout mLinearLayout;
    private Button mSignInButton;
    private Button mRegisterButton;
    private Button mFadeButton;

    private int mMarkX = 0;

    private boolean isVisible = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState != null) {
            isVisible = savedInstanceState.getBoolean(BUNDLE_IS_VISIBLE);
        }

        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_sing_in_register);
        mSignInButton = (Button) view.findViewById(R.id.btn_sign_in);
        mRegisterButton = (Button) view.findViewById(R.id.btn_register);
        mFadeButton = (Button) view.findViewById(R.id.btn_fade);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarkX = mLinearLayout.getWidth() / 4 - mLinearLayout.getWidth() / 20;
                startAnimation();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarkX = mLinearLayout.getWidth() / 4 * 3 - mLinearLayout.getWidth() / 20;
                startAnimation();
            }
        });

        mFadeButton.setAlpha(isVisible ? 1 : 0);
        return view;
    }

    private void startAnimation() {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(mFadeButton, View.ALPHA, 1, 0);
        fadeAnim.setDuration(400);
        fadeAnim.setInterpolator(new FastOutLinearInInterpolator());
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isVisible = false;
                mChangeFragmentListener.onSignInFragment(mLinearLayout, mMarkX);
            }
        });
        fadeAnim.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisible) {
            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(mFadeButton, View.ALPHA, 0, 1);
            fadeAnim.setDuration(500);
            fadeAnim.setStartDelay(200);
            fadeAnim.setInterpolator(new AccelerateInterpolator());
            fadeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isVisible = true;
                }
            });
            fadeAnim.start();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mChangeFragmentListener = (MainActivity) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_VISIBLE, isVisible);
    }

    @Override
    public void onBackPressed() {
        mChangeFragmentListener.onBackClick();
    }
}
