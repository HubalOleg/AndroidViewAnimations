package com.oleg.hubal.registrationviewtransitions;

import android.widget.LinearLayout;

/**
 * Created by User on 27.03.2017.
 */

public interface ChangeFragmentListener {
    void onMainFragment();
    void onSignInFragment(LinearLayout linearLayout, int markX);
    void onBackClick();
}
