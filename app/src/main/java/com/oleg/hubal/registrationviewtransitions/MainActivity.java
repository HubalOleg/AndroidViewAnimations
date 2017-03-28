package com.oleg.hubal.registrationviewtransitions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements ChangeFragmentListener {

    private Transition mChangeTransform;
    private Transition mExplodeTransform;
    private SignInFragment mSignInFragment;
    private MainFragment mMainFragment;
    private BackableFragment mBackableFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChangeTransform = TransitionInflater.from(this).
                inflateTransition(R.transition.transition);
        mExplodeTransform = TransitionInflater.from(this).
                inflateTransition(android.R.transition.no_transition);

        mMainFragment = new MainFragment();
        mMainFragment.setSharedElementReturnTransition(mChangeTransform);
        mMainFragment.setExitTransition(mExplodeTransform);

        onMainFragment();
    }

    @Override
    public void onMainFragment() {
        mBackableFragment = mMainFragment;
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, mMainFragment)
                .commit();
    }

    @Override
    public void onSignInFragment(LinearLayout linearLayout, int markX) {
        mSignInFragment = SignInFragment.newInstance(markX);
        mSignInFragment.setSharedElementEnterTransition(mChangeTransform);
        mSignInFragment.setEnterTransition(mExplodeTransform);

        mBackableFragment = mSignInFragment;
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, mSignInFragment)
                .addToBackStack("")
                .addSharedElement(linearLayout, "transition")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mBackableFragment != null) {
            mBackableFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackClick() {
        super.onBackPressed();
    }
}
