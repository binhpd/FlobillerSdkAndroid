Android SDK and sample for Flocash Ecom API

How to use SDK lib

Add flobillersdk project to your android application.

Copy flobillersdk folder to root folder of application. 
Change build.gradle in app/build.gradle and add following line

dependencies {
....
  compile project(':flobillersdk')
....
}

Change setting.gradle in root folder of application
...
include ':flobillersdk'
...
The first  create new Request from your application:

        OrderInfo order = new OrderInfo();
        PayerInfo payer = new PayerInfo();
        order.setAmount(new BigDecimal("1.0"));
        order.setCurrency("ETB");
        order.setItem_name("Sky Bus");
        order.setItem_price("3");
        order.setOrderId("648");
        order.setQuantity("1");
        payer.setCountry("ET");
        payer.setFirstName("pham");
        payer.setLastName("binh");
        payer.setEmail("binhpd1@gmail.com");
        payer.setMobile(listPhones[0].getPhone());
        MerchantInfo merchant = new MerchantInfo();
        Request request = new Request();
        request.setOrder(order1);
        request.setPayer(payer1);
        request.setMerchant(merchant1);
        merchant.setMerchantAccount("flobiller@flocash.com");
        
step2: pass Request to PaymentActivity.class:

    Intent intent = new Intent(getActivity(), PaymentActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(PaymentActivity.EXTRA_REQUEST_OBJECT, request);
					bundle.putString(PaymentActivity.EXTRA_LOGO,    "https://flobiller.flocash.com/assets/images/upload/c63437313fd2edbb5406573e431a7cc5.png");
					bundle.putString(PaymentActivity.EXTRA_PHONE_CODE, "+233");
					bundle.putString(PaymentActivity.EXTRA_PHONE_NUMBER, "012345678");
					intent.putExtras(bundle);
					startActivityForResult(intent, PAY_INVOICE);
