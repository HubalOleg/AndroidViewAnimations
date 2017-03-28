package com.oleg.hubal.registrationviewtransitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

/**
 * Created by User on 27.03.2017.
 */

public class SignInFragment extends Fragment implements BackableFragment {

    private static final String TAG = "SignInFragment";

    public static final String BUNDLE_IS_VISIBLE = "BUNDLE_IS_VISIBLE";
    public static final String BUNDLE_MARK_X = "BUNDLE_MARK_X";
    private ChangeFragmentListener mChangeFragmentListener;
    private LinearLayout mLinearLayout;
    private LinearLayout mActionsLinear;
    private LinearLayout mSwitchOne;
    private LinearLayout mSwitchTwo;
    private ViewSwitcher mViewSwitcher;

    private Button mSignInButton;
    private Button mRegisterButton;
    private Button mSwitchButton;
    private HorizontalScrollView mHorizontalScrollView;

    private ImageView mMarkImageView;
    private int mMarkX;
    private LinearLayout mSwitchThree;
    private boolean isResetActive;

    public static SignInFragment newInstance(int markX) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_MARK_X, markX);

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_sing_in_register);
        mActionsLinear = (LinearLayout) view.findViewById(R.id.ll_actions);
        mSignInButton = (Button) view.findViewById(R.id.btn_sign_in);
        mRegisterButton = (Button) view.findViewById(R.id.btn_register);
        mHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.scroll_view);
        mMarkImageView = (ImageView) view.findViewById(R.id.iv_mark);
        mSwitchOne = (LinearLayout) view.findViewById(R.id.ll_switch_one);
        mSwitchTwo = (LinearLayout) view.findViewById(R.id.ll_switch_two);
        mSwitchThree = (LinearLayout) view.findViewById(R.id.ll_switch_three);
        mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.view_switcher);
        mSwitchButton = (Button) view.findViewById(R.id.btn_switch);

        mViewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));
        mViewSwitcher.setInAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));

//        mActionsLinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        mMarkX = getArguments().getInt(BUNDLE_MARK_X);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchTwo.setVisibility(View.GONE);
                mSwitchOne.setVisibility(View.VISIBLE);
                mSwitchThree.setVisibility(View.GONE);
                moveMark(mHorizontalScrollView.getWidth() / 4 - mHorizontalScrollView.getWidth() / 20);
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchOne.setVisibility(View.GONE);
                mSwitchTwo.setVisibility(View.VISIBLE);
                mSwitchThree.setVisibility(View.GONE);
                moveMark(mHorizontalScrollView.getWidth() / 4 * 3 - mHorizontalScrollView.getWidth() / 20);
            }
        });
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isResetActive = true;
                mSwitchTwo.setVisibility(View.GONE);
                mSwitchOne.setVisibility(View.GONE);
                mSwitchThree.setVisibility(View.VISIBLE);
                mViewSwitcher.showNext();
                moveMark(mHorizontalScrollView.getWidth() / 2);
            }
        });
    }

    private void moveMark(float to) {
        Log.d(TAG, "moveMark: " + to);
        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(mMarkImageView, "x", to);
        moveAnim.setDuration(200);
        moveAnim.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mChangeFragmentListener = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMarkImageView.setX(mMarkX);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(mActionsLinear, View.ALPHA, 0, 1);
        fadeAnim.setDuration(500);
        fadeAnim.setStartDelay(200);
        fadeAnim.setInterpolator(new AccelerateInterpolator());
        fadeAnim.start();
    }

    @Override
    public void onBackPressed() {
        if (isResetActive) {
            mViewSwitcher.showNext();
            mSwitchOne.setVisibility(View.VISIBLE);
            mSwitchTwo.setVisibility(View.GONE);
            mSwitchThree.setVisibility(View.GONE);
            moveMark(mHorizontalScrollView.getWidth() / 4 - mHorizontalScrollView.getWidth() / 20);
            isResetActive = false;
        } else {
            startFadeOutAnimation();
        }
    }

    private void startFadeOutAnimation() {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(mActionsLinear, View.ALPHA, 1, 0);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mChangeFragmentListener.onBackClick();
            }
        });
        fadeAnim.setDuration(400);
        fadeAnim.setInterpolator(new FastOutLinearInInterpolator());
        fadeAnim.start();
    }
}
