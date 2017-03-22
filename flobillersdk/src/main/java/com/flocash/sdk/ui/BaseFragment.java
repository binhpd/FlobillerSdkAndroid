package com.flocash.sdk.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.flocash.sdk.R;

/**
 * Created by ${binhpd} on 8/27/2016.
 */
public class BaseFragment extends Fragment {
    protected PaymentActivity mActivity;
    protected boolean isFragmentClose = false;

    public void replaceFragment(int pLayoutResId, Fragment pFragment) {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_right_in, R.anim.anim_left_out, R.anim.anim_left_in, R.anim.anim_right_out);
        final String backStateName = pFragment.getClass().getName();
        fragmentTransaction.addToBackStack(backStateName);
        fragmentTransaction.add(pLayoutResId, pFragment, backStateName);
        fragmentTransaction.commit();
    }

    public void finishFragment() {
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        if (backStackCount > 0) {
            getFragmentManager().popBackStack();
            if (backStackCount == 1) {
                mActivity.finish();
            }
        } else {
            mActivity.finish();
        }
    }

    public void addFragment(int contentId, Fragment fragment) {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String nameFragment = fragment.getClass().toString();
        fragmentTransaction.setCustomAnimations(
                R.anim.anim_right_in, R.anim.anim_left_out,
                R.anim.anim_left_in, R.anim.anim_right_out).addToBackStack(nameFragment);
        fragmentTransaction.replace(contentId, fragment, nameFragment);
        fragmentTransaction.commit();
    }

    public void hideKeyboard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDetach() {
        isFragmentClose = true;
        super.onDetach();
    }

    public void showLoading() {
        ((PaymentActivity)mActivity).showLoading();
    }

    public void dismissLoading() {
        ((PaymentActivity)mActivity).dismissLoading();;
    }
}
