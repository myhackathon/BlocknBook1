/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.charitytrails.blocknbook.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.charitytrails.blocknbook.Controller.CharityApplication;
import com.charitytrails.blocknbook.Model.Hotel;
import com.charitytrails.blocknbook.R;
import com.charitytrails.blocknbook.Utils.Constants;
import com.charitytrails.blocknbook.Utils.WalletUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

/**
 * This is a fragment that handles the creating and sending of a {@link FullWalletRequest} using
 * {@link Wallet#loadFullWallet(GoogleApiClient, FullWalletRequest, int)}. This fragment renders
 * a button which hides the complexity of managing Google Play Services connection states,
 * creation and sending of requests and handling responses. Applications may use this fragment as
 * a drop in replacement of a confirmation button in case the user has chosen to use Google Wallet.
 */
public class FullWalletConfirmationButtonActivity extends AppCompatActivity implements
        OnConnectionFailedListener, OnClickListener {

    private static final String TAG = "FullWallet";

    /**
     * Request code used when loading a full wallet. Only use this request code when calling
     * {@link Wallet#loadFullWallet(GoogleApiClient, FullWalletRequest, int)}.
     */
    public static final int REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET = 1004;

    protected GoogleApiClient mGoogleApiClient;
    protected ProgressDialog mProgressDialog;
    protected int mItemId;
    private Hotel mItemInfo ;

    private Button mConfirmButton;
    private MaskedWallet mMaskedWallet;
    private Intent mActivityLaunchIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        mMaskedWallet = mActivityLaunchIntent.getParcelableExtra(Constants.EXTRA_MASKED_WALLET);
        mItemInfo = Constants.ITEMS_FOR_SALE[0];


        String accountName = ((CharityApplication)getApplication()).getAccountName();


        // [START build_google_api_client]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* onConnectionFailedListener */)
                .setAccountName(accountName) // optional
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(Constants.WALLET_ENVIRONMENT)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();
        // [END build_google_api_client]
        mConfirmButton = (Button) findViewById(R.id.bookButton);
        mConfirmButton.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        confirmPurchase();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "Google Play Services Error: " + result.getErrorMessage());
        handleError(result.getErrorCode());

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    // [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProgressDialog.hide();

        // retrieve the error code, if available
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        }

        switch (requestCode) {
            case REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null && data.hasExtra(WalletConstants.EXTRA_FULL_WALLET)) {
                            FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                            // the full wallet can now be used to process the customer's payment
                            // send the wallet info up to server to process, and to get the result
                            // for sending a transaction status
                            fetchTransactionStatus(fullWallet);
                        } else if (data != null && data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {
                            // re-launch the activity with new masked wallet information
                            mMaskedWallet =  data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                            mActivityLaunchIntent.putExtra(Constants.EXTRA_MASKED_WALLET,
                                    mMaskedWallet);
                            startActivity(mActivityLaunchIntent);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // nothing to do here
                        break;
                    default:
                        handleError(errorCode);
                        break;
                }
                break;
        }
    }
    // [END on_activity_result]

    public void updateMaskedWallet(MaskedWallet maskedWallet) {
        mMaskedWallet = maskedWallet;
    }

    /**
     * For unrecoverable Google Wallet errors, send the user back to the checkout page to handle the
     * problem.
     *
     * @param errorCode
     */
    protected void handleUnrecoverableGoogleWalletError(int errorCode) {
//        Intent intent = new Intent(this, CheckoutActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(WalletConstants.EXTRA_ERROR_CODE, errorCode);
//        intent.putExtra(Constants.EXTRA_ITEM_ID, mItemId);
//        startActivity(intent);
    }

    private void handleError(int errorCode) {
        switch (errorCode) {
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                // may be recoverable if the user tries to lower their charge
                // take the user back to the checkout page to try to handle
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                // unrecoverable error
                // take the user back to the checkout page to handle these errors
                handleUnrecoverableGoogleWalletError(errorCode);
        }
    }

    private void confirmPurchase() {
        getFullWallet();
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void getFullWallet() {
        FullWalletRequest fullWalletRequest = WalletUtil.createFullWalletRequest(mItemInfo,
                mMaskedWallet.getGoogleTransactionId());

        // [START load_full_wallet]
        Wallet.Payments.loadFullWallet(mGoogleApiClient, fullWalletRequest,
                REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET);
        // [END load_full_wallet]
    }

    /**
     * Here the client should connect to their server, process the credit card/instrument
     * and get back a status indicating whether charging the card was successful or not
     */
    private void fetchTransactionStatus(FullWallet fullWallet) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        // Log payment method token, if it exists. This token will either be a direct integration
        // token or a Stripe token, depending on the method used when making the MaskedWalletRequest
        PaymentMethodToken token = fullWallet.getPaymentMethodToken();
        if (token != null) {
            // getToken returns a JSON object as a String.
            //
            // For a Stripe token, the 'id' field of the object contains the necessary token.
            //
            // For a Direct Integration token, the object will have the following format:
            // {
            //    encryptedMessage: <string,base64>
            //    ephemeralPublicKey: <string,base64>
            //    tag: <string,base64>
            // }
            // See the Android Pay documentation for more information on how to decrypt the token.

            // Pretty-print the token to LogCat (newlines replaced with spaces).
            Log.d(TAG, "PaymentMethodToken:" + token.getToken().replace('\n', ' '));
        }

        // TODO: Send details such as fullWallet.getProxyCard() or fullWallet.getBillingAddress()
        //       to your server and get back success or failure. If you used Stripe for processing,
        //       you can get the token from fullWallet.getPaymentMethodToken()

        Intent intent = new Intent(this, OrderCompleteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.EXTRA_FULL_WALLET, fullWallet);
        startActivity(intent);
    }

    protected void initializeProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("loading");
        mProgressDialog.setIndeterminate(true);
    }
}
