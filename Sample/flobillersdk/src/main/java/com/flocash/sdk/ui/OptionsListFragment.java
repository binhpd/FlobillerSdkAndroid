package com.flocash.sdk.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flocash.core.models.PaymentMethodInfo;
import com.flocash.core.models.PaymentOptionInfo;
import com.flocash.core.service.IService;
import com.flocash.core.service.ServiceFactory;
import com.flocash.core.service.entity.Environment;
import com.flocash.core.service.entity.OrderInfo;
import com.flocash.core.service.entity.Request;
import com.flocash.core.service.entity.Response;
import com.flocash.core.utils.Utilities;
import com.flocash.sdk.R;
import com.flocash.sdk.adapters.OptionsAdapter;
import com.flocash.sdk.tasks.UpdatePaymentTask;
import com.flocash.sdk.view.ExpandedListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${binhpd} on 8/27/2016.
 */
public class OptionsListFragment extends BaseFragment implements View.OnClickListener {

    private static final String MOBILE_OPTION = "MOBILE";
    private static final String BANK_OPTION = "BANK";

    private PaymentOptionInfo receiveOptions;
    private List<PaymentMethodInfo> listData;
    private Request mRequest;
    private Response mCreateOrderResponse;
    private OptionsAdapter mOptionsAdapter;

    private ExpandedListView mLvOptions;
    private TextView mTvAmount;
    private LinearLayout mLlPay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (PaymentActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_option_list, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mRequest = (Request) bundle.getSerializable(PaymentActivity.EXTRA_REQUEST_OBJECT);
        }

        mLvOptions = (ExpandedListView) view.findViewById(R.id.lvOption);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        mLlPay = (LinearLayout) view.findViewById(R.id.llPay);

        mLlPay.setOnClickListener(this);

        CreateOrderTask task = new CreateOrderTask() {

            @Override
            protected void onPostExecute(Response result) {
                if (getActivity() == null) {
                    return;
                }
                if (getDialog() != null)
                    getDialog().cancel();

                if (result != null && result.isSuccess()) {
                    mTvAmount.setText(((PaymentActivity)mActivity).getCurrency() + " " +
                            Utilities.convertAmount(result.getOrder().getAmount().toString()));
                    getPaymentList(result);
                } else if (result != null) {
                    Toast.makeText(getActivity(), result.getErrorCode() + ": " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    mLlPay.setVisibility(View.GONE);
                }
                super.onPostExecute(result);
            }

        };
        task.execute(mRequest);

        return view;
    }


    private void getPaymentList(Response result) {
        try {
            mCreateOrderResponse = result;
            if (mCreateOrderResponse != null) {
                receiveOptions = mCreateOrderResponse.getPaymentOptions();
            }
            listData = new ArrayList<>();
            if (receiveOptions != null) {
                // bank
                if (receiveOptions.getBanks().size() > 0) {
                    listData.addAll(receiveOptions.getBanks());
                }

                // card
                if (receiveOptions.getCards().size() > 0) {
                    listData.addAll(receiveOptions.getCards());
                }

                // KeyinCards
                if (receiveOptions.getKeyinCards().size() > 0) {
                    listData.addAll(receiveOptions.getKeyinCards());
                }

                // getMobiles
                if (receiveOptions.getMobiles().size() > 0) {
                    listData.addAll(receiveOptions.getMobiles());
                }
            }

            mOptionsAdapter = new OptionsAdapter(mActivity, listData);
            mLvOptions.setAdapter(mOptionsAdapter);
            mLvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("TAG", "click");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mLlPay) {
            PaymentMethodInfo paymentMethodInfo = mOptionsAdapter.getPaymentMethod();
            if (paymentMethodInfo == null) {
                Toast.makeText(mActivity, "Please choose method payment", Toast.LENGTH_LONG).show();
                return;
            }
            if (MOBILE_OPTION.equals(paymentMethodInfo.getType())) {
                MobileCardFragment mobileCardFragment = new MobileCardFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PaymentActivity.EXTRA_PAYMENT_OPTION, paymentMethodInfo);
                bundle.putSerializable(PaymentActivity.EXTRA_TRACE_NUMBER, mCreateOrderResponse.getOrder().getTraceNumber());
                mobileCardFragment.setArguments(bundle);
                addFragment(R.id.containerPanel, mobileCardFragment);
            } else if (BANK_OPTION.equals(paymentMethodInfo.getType())) {
                loadBankPayment(paymentMethodInfo, mCreateOrderResponse.getOrder().getTraceNumber());
            } else {
                CardInfoFragment cardInfoFragment = new CardInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PaymentActivity.EXTRA_PAYMENT_OPTION, paymentMethodInfo);
                bundle.putSerializable(PaymentActivity.EXTRA_TRACE_NUMBER, mCreateOrderResponse.getOrder().getTraceNumber());

                cardInfoFragment.setArguments(bundle);
                addFragment(R.id.containerPanel, cardInfoFragment);
            }
        }
    }

    private void loadBankPayment(PaymentMethodInfo paymentMethodInfo, String traceNumber) {
        Request request = new Request();
        request.setOrder(new OrderInfo());
        request.getOrder().setTraceNumber(traceNumber);
        request.setPayOption(new PaymentMethodInfo());
        request.getPayOption().setId(paymentMethodInfo.getId());
        showLoading();
        UpdatePaymentTask updateTask = new UpdatePaymentTask() {

            @Override
            protected void onPostExecute(Response result) {
                dismissLoading();
                if (result != null && result.isSuccess()) {
                    DoneFragment doneFragment = new DoneFragment();
                    Bundle bundleExtra = new Bundle();
                    bundleExtra.putString(PaymentActivity.EXTRA_AMOUNT, result.getOrder().getAmount().toString());
                    bundleExtra.putString(PaymentActivity.EXTRA_TRACE_NUMBER, result.getOrder().getPartnerTxn());
                    bundleExtra.putString(PaymentActivity.EXTRA_STATUS, result.getOrder().getStatus());
                    bundleExtra.putString(PaymentActivity.EXTRA_STATUS_DES, result.getOrder().getStatusDesc());
                    bundleExtra.putString(PaymentActivity.EXTRA_INTRODUCTION, result.getOrder().getInstruction());

                    doneFragment.setArguments(bundleExtra);
                    addFragment(R.id.containerPanel, doneFragment);
                } else {
                    if (result != null) {
                        Toast.makeText(getActivity(), result.getErrorCode() + ": " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        updateTask.execute(request);
    }

    private class CreateOrderTask extends AsyncTask<Request, Void, Response> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            if (pDialog != null) {
                pDialog.cancel();
            }
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Creating order...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Response doInBackground(Request... params) {
            IService service = new ServiceFactory().getSerivce(Environment.SANDBOX);
            Response result = null;
            try {
                result = service.createOrder(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("hieult", "CheckoutFragment CreateOrderTask: " + e.getLocalizedMessage());

            }
            return result;
        }

        @Override
        protected void onPostExecute(Response result) {
            if (pDialog != null) {
                pDialog.cancel();
            }
            super.onPostExecute(result);
        }

        public ProgressDialog getDialog() {
            return pDialog;
        }
    }
}



