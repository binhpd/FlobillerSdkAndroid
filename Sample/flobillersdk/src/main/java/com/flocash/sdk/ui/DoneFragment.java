package com.flocash.sdk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flocash.core.utils.Utilities;
import com.flocash.sdk.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * Created by ${binhpd} on 8/28/2016.
 */
public class DoneFragment extends BaseFragment implements View.OnClickListener {

    private static final String STATUS_SUCCESS = "0000";
    private static final String STATUS_PENDING = "0004";
    private static final String STATUS_DECINE = "0003";
    private TextView mTvAmount;
    private TextView mTvVerification;
    private TextView mTvViewReceipt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, null);
        mActivity = (PaymentActivity) getActivity();
        Bundle bundle = getArguments();
        String status = bundle.getString(PaymentActivity.EXTRA_STATUS);
        String statusDes = bundle.getString(PaymentActivity.EXTRA_STATUS_DES);
        String introduction = bundle.getString(PaymentActivity.EXTRA_INTRODUCTION);

        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        mTvVerification = (TextView) view.findViewById(R.id.tvVerificationCode);
        mTvViewReceipt = (TextView) view.findViewById(R.id.tvViewReceipt);
        mTvAmount.setText(((PaymentActivity)mActivity).getCurrency() +
                Utilities.convertAmount(bundle.getString(PaymentActivity.EXTRA_AMOUNT)));

        LinearLayout llSucess = (LinearLayout) view.findViewById(R.id.llSuccess);
        LinearLayout llError = (LinearLayout) view.findViewById(R.id.llError);

        ImageView ivStatus = (ImageView) view.findViewById(R.id.ivStatus);
        TextView tvStatusDes = (TextView) view.findViewById(R.id.tvStatusDes);
//        TextView tvIntroduction = (TextView) view.findViewById(R.id.tvIntroduction);
        ExpandableTextView expTv1 = (ExpandableTextView) view.findViewById(R.id.expand_text_view)
                .findViewById(R.id.expand_text_view);

        if (status.equals(STATUS_SUCCESS)) {
            llSucess.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            ivStatus.setBackgroundResource(R.drawable.ic_success);

            mTvVerification.setText(bundle.getString(PaymentActivity.EXTRA_TRACE_NUMBER));
        } else {
            llSucess.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            if (STATUS_PENDING.equals(status)) {
                ivStatus.setBackgroundResource(R.drawable.ic_warning);
                tvStatusDes.setText("PAYMENT IS PENDING!");
                expTv1.setText(Html.fromHtml(introduction));
//                tvIntroduction.setText(Html.fromHtml(introduction));
            } else if (STATUS_DECINE.equals(status)) {
                ivStatus.setBackgroundResource(R.drawable.ic_cancel);
                tvStatusDes.setText("PAYMENT IS DECLINED!");
//                tvIntroduction.setText("Contact your financial institution for more details");
            }
        }
        mTvViewReceipt.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mTvViewReceipt) {
            getActivity().onBackPressed();
        }
    }
}
