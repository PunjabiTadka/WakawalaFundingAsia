package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eghl.sdk.EGHL;
import com.eghl.sdk.Utils;
import com.eghl.sdk.params.PaymentParams;
import com.wakawala.R;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityPaymentBinding;
import com.wakawala.util.Util;

import java.util.Date;

public class PaymentActivity extends BaseActivity {

    private ActivityPaymentBinding mBinding;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, PaymentActivity.class));


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_payment);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.app_name), true);
    }

    public void startPayment(String amount) {

        EGHL eghl = EGHL.getInstance();
        final String paymentID = eghl.generateId("DEMO");
        final PaymentParams.Builder params = new PaymentParams.Builder()
                .setMerchantReturnUrl("SDK")
                .setServiceId("SIT")
                .setPassword("sit12345")
                .setMerchantName("GHL ePayment Testing")
                .setAmount(amount)
                .setPaymentDesc("eGHL Payment testing")
                .setCustName(new Date().getTime() + "Somebody2")
                .setCustEmail(new Date().getTime() + "")
                .setCustPhone(new Date().getTime() + "")
                .setPaymentId(paymentID)
                .setOrderNumber(paymentID)
                .setCurrencyCode(selectedCurrency)
                .setLanguageCode("EN")
                .setPageTimeout("500")
                .setTransactionType("SALE")
                .setPaymentMethod("ANY")
                .setPaymentGateway("https://test2pay.ghl.com/IPGSG/Payment.aspx");

        Bundle paymentParams = params.build();
        eghl.executePayment(paymentParams, PaymentActivity.this);
    }

    String selectedCurrency = "MYR";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (Util.getLocalValue(this, "currency") != null)
                selectedCurrency = Util.getLocalValue(this, "currency");
            mBinding.currency.setText(selectedCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {


        if (Util.getLocalValue(this, "currency") != null)
            selectedCurrency = Util.getLocalValue(this, "currency");
        mBinding.currency.setText(selectedCurrency);

        mBinding.currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, SelectCurrencyActivity.class));
            }
        });
        mBinding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBinding.amountEditText.getText().toString().trim().length() != 0)
                    startPayment(mBinding.amountEditText.getText().toString().trim());
                else
                    Toast.makeText(mContext, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String TAG = "paytest";

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == EGHL.REQUEST_PAYMENT) {
            switch (resultCode) {
                case EGHL.TRANSACTION_SUCCESS:
                    Log.d(TAG, "onActivityResult: payment successful");
                    String status = data.getStringExtra(EGHL.TXN_STATUS);
                    String message = data.getStringExtra(EGHL.TXN_MESSAGE);
                    Log.v(TAG, message + status);
                    String raw = "TxnStatus = " + data.getIntExtra(EGHL.TXN_STATUS, EGHL.TRANSACTION_NO_STATUS) + "\n" + "TxnMessage = " + data.getStringExtra(EGHL.TXN_MESSAGE) + "\nRaw Response:\n" + data.getStringExtra(EGHL.RAW_RESPONSE);
                    Log.v("paytest", raw);
                    break;
                case EGHL.TRANSACTION_FAILED:
                    Log.d(TAG, "onActivityResult: payment failure");
                    break;
                default:
                    Log.d(TAG, "onActivityResult: " + resultCode);
                    break;
            }
        }

    }
}
