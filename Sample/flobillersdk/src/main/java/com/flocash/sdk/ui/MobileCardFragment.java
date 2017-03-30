package com.flocash.sdk.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flocash.core.models.PaymentMethodInfo;
import com.flocash.core.service.IService;
import com.flocash.core.service.ServiceFactory;
import com.flocash.core.service.entity.OrderInfo;
import com.flocash.core.service.entity.Request;
import com.flocash.core.service.entity.Response;
import com.flocash.core.utils.Utilities;
import com.flocash.sdk.R;
import com.flocash.sdk.common.ConfigServer;
import com.flocash.sdk.tasks.UpdatePaymentTask;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;

/**
 * Created by ${binhpd} on 8/28/2016.
 */
public class MobileCardFragment extends BaseFragment implements View.OnClickListener {

    public static final String ETHIOPIA = "ET";
    private static final String CODE = "*144#";
    private ImageView mIvLogo;
    private TextView mTvName;
    private TextView mTvAmount;
    private EditText mEdtPhoneCode;
    private EditText mEdtPhoneNumber;
    private LinearLayout mLlPay;
    private PaymentMethodInfo mChoosenPayment;
    private String mTraceNumber;
    private String mAmount;

    private LinearLayout mLlMoreOption;

//    private TextView mTvInstruction;
    private boolean mIsEthiopia = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile_card, null);
        mActivity = (PaymentActivity) getActivity();
        Bundle bundle = getArguments();
        mChoosenPayment = (PaymentMethodInfo) bundle.getSerializable(PaymentActivity.EXTRA_PAYMENT_OPTION);
        mTraceNumber = bundle.getString(PaymentActivity.EXTRA_TRACE_NUMBER);
        mAmount =  ((PaymentActivity)mActivity).getAmount();


        mIvLogo = (ImageView) view.findViewById(R.id.ivLogo);
        mTvName = (TextView) view.findViewById(R.id.tvName);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        mEdtPhoneCode = (EditText) view.findViewById(R.id.edtPhoneCode);
        mEdtPhoneNumber = (EditText) view.findViewById(R.id.edtPhoneNumber);
        mLlPay = (LinearLayout) view.findViewById(R.id.llPay);
        mLlMoreOption = (LinearLayout) view.findViewById(R.id.llMoreOption);
//        mTvInstruction = (TextView) view.findViewById(R.id.tvSendInstroduction);


        Picasso.with(mActivity).load(mChoosenPayment.getLogo()).into(mIvLogo);
        mTvName.setText(mChoosenPayment.getDisplayName());
        mTvAmount.setText(((PaymentActivity)mActivity).getCurrency() + " " + Utilities.convertAmount(mAmount));
        mEdtPhoneCode.setText(((PaymentActivity)mActivity).getPhoneCode());
        mEdtPhoneNumber.setText(((PaymentActivity)mActivity).getmPhoneNumber());
        mLlPay.setOnClickListener(this);
//        mTvInstruction.setOnClickListener(this);

        mIsEthiopia = ((PaymentActivity)(mActivity)).getCountry().equals(ETHIOPIA);
        mLlMoreOption.setVisibility(mIsEthiopia ? View.VISIBLE:View.GONE);

        return view;
    }

    @Override
    public void onClick(final View v) {
        if (valid()) {
            hideKeyboard();
            Request request = new Request();
            request.setOrder(new OrderInfo());
            request.getOrder().setTraceNumber(mTraceNumber);
            request.setPayOption(new PaymentMethodInfo());
            request.getPayOption().setId(mChoosenPayment.getId());
            showLoading();
            UpdatePaymentTask updateTask = new UpdatePaymentTask() {

                @Override
                protected void onPostExecute(Response result) {
                    if (result != null && result.isSuccess()) {

//                        if (mIsEthiopia && v == mLlPay) {
//                            ((PaymentActivity)mActivity).dailNumber("*144#");
//                            return;
//                        }

                        final Hashtable<String, String> additionHashtable = new Hashtable<String, String>();
                        additionHashtable.put("mobile", mEdtPhoneCode.getText().toString().trim() + mEdtPhoneNumber.getText().toString().trim());
                        UpdateAdditionField updateAdditionTask = new UpdateAdditionField(mTraceNumber, additionHashtable) {
                        @Override
                        protected void onPostExecute(Response result) {
                            if (isFragmentClose) {
                                return;
                            }
                            dismissLoading();
                            // update success show instruction dialog
                            if (result != null && result.isSuccess() && result.getOrder() != null) {
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
                            super.onPostExecute(result);
                        }
                        };
                        updateAdditionTask.execute();
                    } else {
                        dismissLoading();
                    }
                }
            };
            updateTask.execute(request);
        } else {
            Toast.makeText(mActivity, R.string.notify_fields_required, Toast.LENGTH_LONG).show();
        }
    }

    private class UpdateAdditionField extends AsyncTask<Void, Void, Response> {
//        private ProgressDialog pDialog;
        private String traceNumber;
        private Hashtable<String, String> hashTableAddition;

        public UpdateAdditionField(String traceNumber, Hashtable<String, String> paramsHash) {
            this.traceNumber = traceNumber;
            this.hashTableAddition = paramsHash;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response doInBackground(Void... params) {
            IService service = new ServiceFactory().getSerivce(ConfigServer.USER_NAME, ConfigServer.PASSWORD,
                    ConfigServer.BASE_URL, ConfigServer.ORDER_PATH);
            Response result = null;
            try {
                result = service.updateAdditionField(traceNumber, hashTableAddition);
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

    private boolean valid() {
        if (TextUtils.isEmpty(mEdtPhoneCode.getText().toString().trim()) ||
                TextUtils.isEmpty(mEdtPhoneNumber.getText().toString().trim())) {
            return false;
        }
        return true;
    }
}
