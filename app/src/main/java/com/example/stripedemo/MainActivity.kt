package com.example.stripedemo

import android.R.attr.button
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheet.Builder
import com.stripe.android.paymentsheet.PaymentSheetResult

class MainActivity : AppCompatActivity() {
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private lateinit var paymentIntentClientSecret: String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        paymentSheet = Builder(::onPaymentSheetResult).build(this)



        val button= findViewById<Button>(R.id.button)
            button .setOnClickListener {
                validateFromServer()
            }
    }

    private fun validateFromServer() {
        paymentIntentClientSecret = "pi_3SpMLdDNAJIRUKlg1iQjSwOI_secret_n2tk2u9qosPPtmwOnrXfK0HxB"
        val customerId = "cus_TmwE2wybQprX7x"
        val ephemeralKeySecret = "ek_test_YWNjdF8xUFZWOVhETkFKSVJVS2xnLFFBMWxqQlB6R1VCUXF4dDZDT3JCaERCVFFnU3pKYXE_00AdmiPCrC"
        customerConfig = PaymentSheet.CustomerConfiguration(customerId, ephemeralKeySecret)
        val publishableKey = "pk_test_51PVV9XDNAJIRUKlguIitC5fGvprT2KNprE91Dh9Gjyz7Ox9GtdrIUnIQyIPPsYFASPWwo4XecpFke13Jr11ZmXLi00I4TiHH7A"
        PaymentConfiguration.init(this, publishableKey)
        presentPaymentSheet()


    }



    private fun presentPaymentSheet() {

        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret, PaymentSheet.Configuration(
                merchantDisplayName = "My Business Merch",
                customer = customerConfig,
                allowsDelayedPaymentMethods = true
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                showAlert("Payment cancelled")
            }
            is PaymentSheetResult.Failed -> {
                showAlert("Payment failed ${paymentSheetResult.error.message}")
            }
            is PaymentSheetResult.Completed -> {
                showAlert("Payment completed successfully")
            }
        }
    }

    fun showAlert(message: String) {
        AlertDialog.Builder(this).setTitle("Alert").setMessage(message)
            .setPositiveButton("OK", null).show()
    }
}