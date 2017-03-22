package com.flocash.sdk.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flocash.core.models.PaymentMethodInfo;
import com.flocash.sdk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${binhpd} on 8/27/2016.
 */
public class OptionsAdapter extends BaseAdapter {

    private Context context;
    private List<PaymentMethodInfo> mPaymentMethodInfos;
    private int check = -1;
//    private OnSelectOptionListener mOnSelectOptionListener;

    public OptionsAdapter(Context context, List<PaymentMethodInfo> paymentMethodInfos) {
        this.context = context;
        mPaymentMethodInfos = paymentMethodInfos;
//        this.mOnSelectOptionListener = onSelectOptionListener;
    }

    @Override
    public int getCount() {
        return mPaymentMethodInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mPaymentMethodInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HolderView holderView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_option, null);
            holderView = new HolderView();
            holderView.rlItem = (RelativeLayout) convertView.findViewById(R.id.rlItem);
            holderView.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holderView.ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
            holderView.cbTick = (AppCompatCheckBox) convertView.findViewById(R.id.cbTick);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        holderView.cbTick.setClickable(false);

        if (check == position) {
            holderView.cbTick.setChecked(true);
            holderView.rlItem.setBackgroundColor(context.getResources().getColor(R.color.bg_focus));
        } else {
            holderView.cbTick.setChecked(false);
            holderView.rlItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holderView.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = position;
                notifyDataSetChanged();

            }
        });

        PaymentMethodInfo methodInfo = mPaymentMethodInfos.get(position);
        holderView.tvName.setText(methodInfo.getDisplayName());
        Picasso.with(context).load(methodInfo.getLogo()).into(holderView.ivLogo);
        return convertView;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public PaymentMethodInfo getPaymentMethod() {
        if (check == -1) {
            return null;
        }
        return mPaymentMethodInfos.get(check);
    }
    public static class HolderView {
        RelativeLayout rlItem;
        TextView tvName;
        ImageView ivLogo;
        AppCompatCheckBox cbTick;
    }

    public interface OnSelectOptionListener {
        void onSelectOptionListener(PaymentMethodInfo paymentMethodInfo);
    }
}
