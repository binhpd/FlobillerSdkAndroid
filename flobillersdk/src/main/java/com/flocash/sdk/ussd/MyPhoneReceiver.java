package com.flocash.sdk.ussd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.flocash.sdk.ui.IReceiverUssd;

/**
 * Created by lion on 2/7/17.
 */

public class MyPhoneReceiver extends BroadcastReceiver {

    IReceiverUssd iReceiverUssd;

    public void setListener(IReceiverUssd iReceiverUssd) {
        this.iReceiverUssd = iReceiverUssd;
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            iReceiverUssd.onUssdBack(extras.getString(UssdService.USSD_EXTRA));
        }
    }
}
