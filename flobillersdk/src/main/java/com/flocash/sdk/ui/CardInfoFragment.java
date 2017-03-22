package com.flocash.sdk.ui;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flocash.core.models.PaymentMethodInfo;
import com.flocash.core.service.IService;
import com.flocash.core.service.ServiceFactory;
import com.flocash.core.service.entity.CardInfo;
import com.flocash.core.service.entity.Environment;
import com.flocash.core.service.entity.OrderInfo;
import com.flocash.core.service.entity.Request;
import com.flocash.core.service.entity.Response;
import com.flocash.core.utils.Utilities;
import com.flocash.sdk.R;
import com.flocash.sdk.quickaction.QuickAction;
import com.flocash.sdk.tasks.UpdatePaymentTask;

import java.util.Calendar;

/**
 * Created by lion on 8/2/16.
 */
public class CardInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final int MIN_LENGTH_CARD_NUMBER = 16;
    private TextView mTvAmount;

    private EditText edtNameOnCard;

    private EditText edtCardNumber;

    private TextView tvExpiryDate;

    private EditText edtSecurityCode;

    private  LinearLayout llPay;

    private TextView mTvError;

    private TextView mTvNoticeInput;

    private ImageView mIvQuestion;

    private CharSequence mExpiryDate;
    private String mMonth, mYear;
    private PaymentMethodInfo mChoosenPayment;
    private String mTraceNumber;
    private String mAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (PaymentActivity) getActivity();

        Bundle bundle = getArguments();
        mChoosenPayment = (PaymentMethodInfo) bundle.getSerializable(PaymentActivity.EXTRA_PAYMENT_OPTION);
        mTraceNumber = bundle.getString(PaymentActivity.EXTRA_TRACE_NUMBER);
        mAmount =  ((PaymentActivity)mActivity).getAmount();

        View view  = inflater.inflate(R.layout.fragment_input_card_info, null);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        edtNameOnCard = (EditText) view.findViewById(R.id.edtNameOnCard);
        edtCardNumber = (EditText) view.findViewById(R.id.tvCardNumber);
        tvExpiryDate = (TextView) view.findViewById(R.id.tvExpiryDate);
        edtSecurityCode = (EditText) view.findViewById(R.id.tvSecurityCode);
        mTvNoticeInput = (TextView) view.findViewById(R.id.tvNoticeInput);
        mIvQuestion = (ImageView) view.findViewById(R.id.ivQuestion);
        llPay = (LinearLayout) view.findViewById(R.id.llPay);
        mTvError = (TextView) view.findViewById(R.id.tvError);

        mTvAmount.setText(((PaymentActivity)mActivity).getCurrency() + " " + Utilities.convertAmount(mAmount));
        llPay.setOnClickListener(this);
        tvExpiryDate.setOnClickListener(this);
        mIvQuestion.setOnClickListener(this);
        edtCardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false && edtCardNumber.getText().toString().length() < MIN_LENGTH_CARD_NUMBER) {
                    mTvNoticeInput.setVisibility(View.VISIBLE);
                } else {
                    mTvNoticeInput.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    public void onClick(View view){
        if (view == tvExpiryDate) {
            showExpiryDate(tvExpiryDate);
        } else if (view == llPay) {
            if (valid()) {
                hideKeyboard();
                updateOrder(edtNameOnCard.getText().toString().trim(),
                        edtCardNumber.getText().toString().trim(),
                        mMonth,
                        mYear,
                        edtSecurityCode.getText().toString().trim());
            } else {
                Toast.makeText(mActivity, R.string.notify_fields_required, Toast.LENGTH_LONG).show();
            }
        } else if (view == mIvQuestion) {
            RelativeLayout customLayout = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.layout_rounded_border, null);
            int color = getResources().getColor(R.color.gray);
            QuickAction quickAction = new QuickAction(mActivity, color, R.style.PopupAnimation, customLayout, 200);
            quickAction.show(view);
        }
    }

    private void showExpiryDate(final TextView tvExpiryDate) {
        final Dialog dialog = new Dialog(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.content_input_date, null);

        final NumberPicker numberPickerMonth = (NumberPicker) view.findViewById(R.id.npMonth);
        final NumberPicker numberPickerYear = (NumberPicker) view.findViewById(R.id.npYear);
        String[] arrMonth = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setWrapSelectorWheel(false);
        numberPickerMonth.setDisplayedValues(arrMonth);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int min = currentYear-10;
        int max = currentYear+10;

        numberPickerYear.setMinValue(min);
        numberPickerYear.setMaxValue(max);
        numberPickerYear.setValue(currentYear);
        numberPickerYear.setWrapSelectorWheel(false);

        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valMonth = numberPickerMonth.getValue();
                mMonth = valMonth < 10 ? "0" + valMonth : String.valueOf(valMonth);
                mYear = String.valueOf(numberPickerYear.getValue()).substring(2,4);
                tvExpiryDate.setText(mMonth + "/" + mYear);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private boolean valid() {
        if (TextUtils.isEmpty(edtNameOnCard.getText().toString().trim()) ||
                TextUtils.isEmpty(edtCardNumber.getText().toString().trim()) ||
                TextUtils.isEmpty(tvExpiryDate.getText().toString()) ||
                TextUtils.isEmpty(edtSecurityCode.getText().toString())) {
            return false;
        }

        if (edtCardNumber.getText().length() < MIN_LENGTH_CARD_NUMBER) {
            return false;
        }
        return true;
    }

    private void updateOrder(String holder, String cardNumber, String month, String year, String cvv) {
        if (mChoosenPayment != null) {
            Request request = new Request();
            request.setOrder(new OrderInfo());
            request.getOrder().setTraceNumber(mTraceNumber);

            request.setPayOption(new PaymentMethodInfo());
            request.getPayOption().setId(mChoosenPayment.getId());

            CardInfo cardInfo = new CardInfo(holder, cardNumber, month, year, cvv);
            request.setCardInfo(cardInfo);

            UpdatePaymentTask updateTask = new UpdatePaymentTask() {

                @Override
                protected void onPostExecute(Response result) {
                    if (result != null && result.isSuccess()) {
                        String traceNumber = result.getOrder().getTraceNumber();
                        new GetOrderTask().execute(new String[] {traceNumber});
                    } else {
                        dismissLoading();
                        if (result != null) {
                            Toast.makeText(getActivity(), result.getErrorCode() + ": " + result.getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    super.onPostExecute(result);
                }

            };

            updateTask.execute(request);
        }
    }

    private class GetOrderTask extends AsyncTask<String, Void, Response> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response doInBackground(String... params) {
            IService service = new ServiceFactory().getSerivce(Environment.SANDBOX);
            Response result = null;
            try {
                result = service.getOrder(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Response result) {
            if (isFragmentClose) {
                return;
            }
            if(result != null && result.isSuccess() && result.getOrder() != null)
            {
                StringBuilder message = new StringBuilder();
                message.append("Order number: "+ result.getOrder().getTraceNumber()+"\n");
                message.append("Amount: "+ result.getOrder().getAmount()+"\n");
                message.append("Status: "+ result.getOrder().getStatus()+"\n");
                message.append("Currency: "+ result.getOrder().getCurrency());
                Log.d("Payment result", message.toString());

                DoneFragment doneFragment = new DoneFragment();
                Bundle bundleExtra = new Bundle();
                bundleExtra.putString(PaymentActivity.EXTRA_AMOUNT, result.getOrder().getAmount().toString());
                bundleExtra.putString(PaymentActivity.EXTRA_TRACE_NUMBER, result.getOrder().getPartnerTxn());
                bundleExtra.putString(PaymentActivity.EXTRA_STATUS, result.getOrder().getStatus());
                bundleExtra.putString(PaymentActivity.EXTRA_STATUS_DES, result.getOrder().getStatusDesc());
                bundleExtra.putString(PaymentActivity.EXTRA_INTRODUCTION, result.getOrder().getInstruction());

                doneFragment.setArguments(bundleExtra);
                addFragment(R.id.containerPanel, doneFragment);
            } else if(result != null && !result.isSuccess()){
                edtCardNumber.setText("");
                edtNameOnCard.setText("");
                edtSecurityCode.setText("");
                mMonth = mYear = "";
                mTvNoticeInput.setVisibility(View.GONE);
                mTvError.setVisibility(View.GONE);
                Toast.makeText(getActivity(), result.getErrorCode() + ": " + result.getErrorMessage(), Toast.LENGTH_LONG).show();
            }

            dismissLoading();
            super.onPostExecute(result);
        }

    }
}
