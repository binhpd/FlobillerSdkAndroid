package com.flocash.sdk.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.flocash.core.service.entity.Request;
import com.flocash.sdk.R;
import com.flocash.sdk.ussd.MyPhoneReceiver;
import com.flocash.sdk.ussd.UssdService;

/**
 * Created by ${binhpd} on 8/27/2016.
 */
public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, IReceiverUssd {

    public static final String EXTRA_REQUEST_OBJECT = "EXTRA_REQUEST_OBJECT";
    public static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    public static final String EXTRA_PAYMENT_OPTION = "EXTRA_PAYMENT_OPTION";
    public static final java.lang.String EXTRA_TRACE_NUMBER = "EXTRA_TRACE_NUMBER";
    public static final String EXTRA_LOGO = "LOGO";
    public static final String EXTRA_PHONE_CODE = "EXTRA_PHONE_CODE";
    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";
    public static final String EXTRA_STATUS = "EXTRA_STATUS";
    public static final String EXTRA_STATUS_DES = "EXTRA_STATUS_DES";
    public static final String EXTRA_INTRODUCTION = "EXTRA_INTRODUCTION";
    public static final String EXTRA_CURRENCY= "EXTRA_CURRENCY";
    public static final String EXTRA_COUNTRY= "EXTRA_COUNTRY";
    public static final String EXTRA_ACCOUNT_NUMBER= "EXTRA_ACCOUNT_NUMBER";

    private static final int PERMISSION_REQUEST_PHONE = 0;

    private View mLayout;

    private String mAmount;
    private String mLogo;
    private String phoneCode;
    private String mPhoneNumber;
    private String mAccountNumber;

    private LinearLayout mLlLoading;
    private String mCurrency;
    private String mCountry;

    private String mUssd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        mLayout = findViewById(R.id.main_layout);
        OptionsListFragment optionsListFragment = new OptionsListFragment();
        Bundle bundle = getIntent().getExtras();
        Request mRequest = (Request) bundle.getSerializable(PaymentActivity.EXTRA_REQUEST_OBJECT);

        mAmount = mRequest.getOrder().getAmount().toString();
        mCurrency = mRequest.getOrder().getCurrency();

        mLogo = bundle.getString(EXTRA_LOGO);
        phoneCode = bundle.getString(EXTRA_PHONE_CODE);
        mPhoneNumber = bundle.getString(EXTRA_PHONE_NUMBER);
        mAccountNumber = bundle.getString(EXTRA_ACCOUNT_NUMBER);
        mCountry = bundle.getString(EXTRA_COUNTRY);

        optionsListFragment.setArguments(bundle);

        findViewById(R.id.ivBack).setOnClickListener(this);
        mLlLoading = (LinearLayout) findViewById(R.id.llLoading);

        addFragment(R.id.containerPanel, optionsListFragment);

        startService(new Intent(this, UssdService.class));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UssdService.USSD_FILTER);
        MyPhoneReceiver myPhoneReceiver = new MyPhoneReceiver();
        myPhoneReceiver.setListener(this);
        registerReceiver(myPhoneReceiver, intentFilter);
    }

    public void dailNumber(String code) {
//        mUssd = "*" + "804*5*9345345345*50.40" + Uri.encode("#");
        mUssd = "*" + "144" + Uri.encode("#");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + mUssd)));
        } else {
            requestPhonePermission();
        }
//        String ussdCode = "*" + code + Uri.encode("#");
    }

    private void requestPhonePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Phone access is required to run ussd.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(PaymentActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_PHONE);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting phone permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_PHONE);
        }
    }

    public void replaceFragment(int pLayoutResId, Fragment pFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
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
                finish();
            }
        } else {
            finish();
        }
    }

    public void addFragment(int contentId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String nameFragment = fragment.getClass().toString();
        fragmentTransaction.setCustomAnimations(
                R.anim.anim_right_in, R.anim.anim_left_out,
                R.anim.anim_left_in, R.anim.anim_right_out).addToBackStack(nameFragment);
        fragmentTransaction.replace(contentId, fragment, nameFragment);
        fragmentTransaction.commit();
    }

    public String getAmount() {
        return mAmount;
    }


    public String getPhoneCode() {
        return phoneCode;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public String getCountry() {
        return this.mCountry;
    }

    public String getAccountNumber() {
        return this.mAccountNumber;
    }
    @Override
    public void onBackPressed() {
        if (mLlLoading.getVisibility() == View.GONE) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public void showLoading() {
        mLlLoading.setVisibility(View.VISIBLE);
    }

    public void dismissLoading() {
        mLlLoading.setVisibility(View.GONE);
    }

    @Override
    public void onUssdBack(String ussd) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.ussd_msg));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissLoading();
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_PHONE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                dailNumber(mUssd);
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Camera permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
}