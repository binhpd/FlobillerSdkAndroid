package com.flocash.sdk.tasks;

import android.os.AsyncTask;

import com.flocash.core.service.IService;
import com.flocash.core.service.ServiceFactory;
import com.flocash.core.service.entity.Environment;
import com.flocash.core.service.entity.Request;
import com.flocash.core.service.entity.Response;

/**
 * Created by lion on 9/28/16.
 */

public class UpdatePaymentTask extends AsyncTask<Request, Void, Response> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Response doInBackground(Request... params) {
        IService service = new ServiceFactory().getSerivce(Environment.SANDBOX);
        Response result = null;
        try {
            result = service.updatePaymentOption(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Response result) {
        super.onPostExecute(result);
    }
}
