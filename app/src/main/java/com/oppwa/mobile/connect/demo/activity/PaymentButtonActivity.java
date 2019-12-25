package com.oppwa.mobile.connect.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.oppwa.mobile.connect.checkout.dialog.PaymentButtonFragment;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.demo.R;
import com.oppwa.mobile.connect.demo.common.Constants;
import com.oppwa.mobile.connect.exception.PaymentException;


/**
 * Represents an activity for making payments via {@link PaymentButtonFragment}.
 */
public class PaymentButtonActivity extends BasePaymentActivity {

    private PaymentButtonFragment paymentButtonFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment_button);

        ((TextView) findViewById(R.id.amount_text_view))
                .setText(Constants.Config.AMOUNT + " " + Constants.Config.CURRENCY);

        initPaymentButton();
    }

    private void initPaymentButton() {
        paymentButtonFragment = (PaymentButtonFragment) getFragmentManager()
                .findFragmentById(R.id.payment_button_fragment);

        paymentButtonFragment.setPaymentBrand(Constants.Config.PAYMENT_BUTTON_BRAND);
        paymentButtonFragment.getPaymentButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCheckoutId(getString(R.string.payment_button_callback_scheme));
            }
        });

        /* customize the payment button */
        paymentButtonFragment.getPaymentButton().setBackgroundResource(R.drawable.button_base_background);
        paymentButtonFragment.getPaymentButton().setColorFilter(Color.rgb(255, 255, 255));
    }

    @Override
    public void onCheckoutIdReceived(String checkoutId) {
        super.onCheckoutIdReceived(checkoutId);

        if (checkoutId != null) {
            pay(checkoutId);
        }
    }

    private void pay(String checkoutId) {
        CheckoutSettings checkoutSettings = createCheckoutSettings(checkoutId, getString(R.string.payment_button_callback_scheme));

        try {
            paymentButtonFragment.submitTransaction(checkoutSettings);
            showProgressDialog(R.string.progress_message_processing_payment);
        } catch (PaymentException e) {
            showAlertDialog(R.string.error_message);
        }
    }
}
