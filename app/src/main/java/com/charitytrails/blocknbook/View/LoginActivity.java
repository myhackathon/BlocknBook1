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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.charitytrails.blocknbook.Controller.CharityApplication;
import com.charitytrails.blocknbook.MainActivity;
import com.charitytrails.blocknbook.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Scope;

public class LoginActivity extends AppCompatActivity implements
        OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";

    public static final int LOGIN = 2000;
    public static final int LOGOUT = 2001;

    public static final int REQUEST_CODE_RESOLVE_ERR = 1005;
    private static final int REQUEST_CODE_SIGN_IN = 1006;
    private static final String WALLET_SCOPE =
            "https://www.googleapis.com/auth/payments.make_payments";

    private GoogleApiClient mGoogleApiClient;
    private int mLoginAction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
            mLoginAction = LOGIN;


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(WALLET_SCOPE))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        findViewById(R.id.button_login_bikestore).setOnClickListener(this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.button_login_bikestore:
                Toast.makeText(this, "Logged In", Toast.LENGTH_LONG).show();
                Intent bookingIntent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(bookingIntent);
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mLoginAction == LOGOUT) {
            logOut();
        } else {
            silentSignIn();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed:" + result.getErrorMessage());
    }

    private void onSignInClicked() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(TAG, "googleSignIn:SUCCESS");
            handleSignInSuccess(result.getSignInAccount());
        } else {
            Log.d(TAG, "googleSignIn:FAILURE:" + result.getStatus());
            Toast.makeText(this, "network_error", Toast.LENGTH_LONG).show();

        }
    }

    private void handleSignInSuccess(GoogleSignInAccount account) {
        Toast.makeText(this, "welcome_user", Toast.LENGTH_LONG).show();

        ((CharityApplication) this.getApplication()).login(account.getEmail());
        this.setResult(Activity.RESULT_OK);
        this.finish();
    }

    private void silentSignIn() {
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            handleSignInResult(opr.get());
        }
    }

    private void logOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

            ((CharityApplication) this.getApplication()).logout();
            Toast.makeText(this, "Logged_out", Toast.LENGTH_LONG).show();
            this.setResult(Activity.RESULT_OK);
            this.finish();
        } else {
            mLoginAction = LOGOUT;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // nothing specifically required here, onConnected will be called when connection resumes
    }
}
