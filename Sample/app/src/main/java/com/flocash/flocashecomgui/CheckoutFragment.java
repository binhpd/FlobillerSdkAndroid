package com.flocash.flocashecomgui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.flocash.core.service.entity.MerchantInfo;
import com.flocash.core.service.entity.OrderInfo;
import com.flocash.core.service.entity.PayerInfo;
import com.flocash.core.service.entity.Request;
import com.flocash.sdk.ui.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {
	public static final int PAY_INVOICE = 1010;
	private Button btnNext;
	private ListView mListViewCheckout;
	private List<Request> listRequest;
	private Request mChoosenRequest;
	private CheckoutAdapter adapter;

	private Phone[] listPhones;
	private int mIndex = 0;

	public class Phone {
		public String phoneCode;
		public String phoneNumber;

		public Phone(String phoneCode, String phoneNumber) {
			this.phoneCode = phoneCode;
			this.phoneNumber = phoneNumber;
		}

		public String getPhone() {
			return phoneCode + phoneNumber;
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);
		prepareData();
		fragmentGettingStarted(rootView);
		return rootView;
	}
	
	private void fragmentGettingStarted(View view){
		try {
			btnNext = (Button) view.findViewById(R.id.btnNext);
			btnNext.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mChoosenRequest = listRequest.get(mIndex);
					Intent intent = new Intent(getActivity(), PaymentActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(PaymentActivity.EXTRA_REQUEST_OBJECT,mChoosenRequest);
					bundle.putString(PaymentActivity.EXTRA_LOGO, "https://flobiller.flocash.com/assets/images/upload/c63437313fd2edbb5406573e431a7cc5.png");
					bundle.putString(PaymentActivity.EXTRA_PHONE_CODE, listPhones[mIndex].phoneCode);
					bundle.putString(PaymentActivity.EXTRA_PHONE_NUMBER, listPhones[mIndex].phoneNumber);
					intent.putExtras(bundle);
					startActivityForResult(intent, PAY_INVOICE);
				}
			});
			
			mListViewCheckout = (ListView) view.findViewById(R.id.listCheckoutItem);
			adapter = new CheckoutAdapter(getActivity(), android.R.layout.select_dialog_singlechoice, listRequest);
			mChoosenRequest = listRequest.get(0);
			mListViewCheckout.setAdapter(adapter);
			mListViewCheckout.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					mIndex = position;
					adapter.updateChecked(position);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("hieult", "CheckoutFragment fragmentGettingStarted: "+e.getLocalizedMessage());
		}
	}
	
	private class CheckoutAdapter extends ArrayAdapter<Request>{
	
		private int mChecked;
		
		public CheckoutAdapter(Context context, int resource,
				List<Request> objects) {
			super(context, resource, objects);
		}
		
		public void updateChecked(int position)
		{
			mChecked = position;
			notifyDataSetChanged();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView ==  null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_checkout_list, null);
			}
			Request request = getItem(position);
			TextView tvName = (TextView) convertView.findViewById(R.id.item_name);
			TextView tvPrice = (TextView) convertView.findViewById(R.id.item_price);
			CheckedTextView checked = (CheckedTextView) convertView.findViewById(R.id.item_payment_text);
			checked.setChecked(mChecked == position ? true:false);
			tvName.setText(request.getOrder().getItem_name());
			tvPrice.setText("Price: "+request.getOrder().getItem_price());
			return convertView;
		}
	}
	
	private void prepareData()
	{

		listPhones = new Phone[]{new Phone("+251", "87016637251"), new Phone("+233", "87016637251")} ;

		listRequest = new ArrayList<Request>();
		
		// card payment
		OrderInfo order1 = new OrderInfo();
        PayerInfo payer1 = new PayerInfo();
        order1.setAmount(new BigDecimal("1.0"));
        order1.setCurrency("ETB");
        order1.setItem_name("Sky Bus");
        order1.setItem_price("3");
        order1.setOrderId("648");
        order1.setQuantity("1");
        payer1.setCountry("ET");
        payer1.setFirstName("pham");
        payer1.setLastName("binh");
        payer1.setEmail("binhpd1@gmail.com");
        payer1.setMobile(listPhones[0].getPhone());
        MerchantInfo merchant1 = new MerchantInfo();
        Request request1 = new Request();
        request1.setOrder(order1);
        request1.setPayer(payer1);
        request1.setMerchant(merchant1);
        merchant1.setMerchantAccount("flobiller@flocash.com");
        
        // mobile payment
        OrderInfo order2 = new OrderInfo();
        PayerInfo payer2 = new PayerInfo();
        order2.setAmount(new BigDecimal("1"));
        order2.setCurrency("GHS");
        order2.setItem_name("DSTV");
        order2.setItem_price("1");
        order2.setOrderId("645");
        order2.setQuantity("1");
        payer2.setCountry("GH");
        payer2.setFirstName("pham");
        payer2.setLastName("binh");
        payer2.setEmail("binhpd1@gmail.com");
        payer2.setMobile(listPhones[1].getPhone());
        MerchantInfo merchant2 = new MerchantInfo();
        Request request2 = new Request();
        request2.setOrder(order2);
        request2.setPayer(payer2);
        request2.setMerchant(merchant2);
        merchant2.setMerchantAccount("flobiller@flocash.com");
        
        listRequest.add(request1);
        listRequest.add(request2);
	}
}