package com.muhib.restaurant.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonElement;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.muhib.restaurant.NetUtils;
import com.muhib.restaurant.R;
import com.muhib.restaurant.retrofit.RetrofitApiClient;
import com.muhib.restaurant.utils.MySheardPreference;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.CategoryModel;
import model.Products;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText;
    Button _loginButton;
    TextView _signupLink;
    EditText _passwordText;
    EditText websiteUrl;
    private Button scan_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        websiteUrl = (EditText) findViewById(R.id.url_field);
        _loginButton = (Button) findViewById(R.id.btn_login);
        final Activity activity =this;
        scan_btn = (Button)findViewById(R.id.scan);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();

            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
    }

    ProgressDialog progressDialog;

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        //_loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String ck = _emailText.getText().toString();
        String cs = _passwordText.getText().toString();
        String siteUrl = websiteUrl.getText().toString();

        String defaultSiteUrl = "http://woocom.endix.net";
        String defaultKey = "ck_119af3964b19a5d9b4ccbc435b428ab8a91c6b18";
        String defaultSecret = "cs_681801f5d8fe6f94e39fb2c15f88253cc50f63f3";



        // TODO: Implement your own authentication logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        //onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }, 3000);
        //callLoginApi(defaultSiteUrl,defaultKey, defaultSecret);

        callLoginApi(siteUrl,ck, cs);


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

//    public void onLoginSuccess() {
//        //_loginButton.setEnabled(true);
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String siteUrl = websiteUrl.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
//            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("User name should not be empty");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (siteUrl.isEmpty()|| !URLUtil.isValidUrl(siteUrl) ) {
//            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            websiteUrl.setError("Website URL should not be empty and must be valid url");
            valid = false;
        }
        else {
            websiteUrl.setError(null);
        }

//        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
        if (password.isEmpty() || password.length() < 4 ) {
            _passwordText.setError("atleast 4 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    private void callLoginApi(final String siteUrl, final String userId, final String password) {

        if(!NetUtils.isNetworkAvailable(this)) {
            NetUtils.noInternetWarning(_passwordText, getApplicationContext());
            progressDialog.dismiss();
            return;
        }

        MySheardPreference.setUserSiteUrl(siteUrl);
        RetrofitApiClient.getApiInterface(userId, password).getLogedIn()
//        RetrofitApiClient.getLoginApiInterface(userId, password).getLogedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Products>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response value) {
                        progressDialog.dismiss();

                        if (value.code() == 200) {
                            MySheardPreference.setUserIdAndPassword(userId, password);
                            callforToken(userId, password);
//                            Headers headers = value.headers();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();

                        } else
                            Toast.makeText(getApplicationContext(), value.message(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onError(Throwable e) {
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });


    }

    private void callforToken(String userId, String password) {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        RetrofitApiClient.getApiInterface(userId, password).getServerToken("android", "webdev.nislam@gmail.com", refreshedToken)
//        RetrofitApiClient.getLoginApiInterface(userId, password).getLogedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonElement value) {
                        progressDialog.dismiss();
                        Log.d(TAG, "Refreshed token Login: " + refreshedToken);

//                        if (value.code() == 200) {
//
//
//                            Headers headers = value.headers();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
//
//                        } else
                           // Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onError(Throwable e) {
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() ==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                String qrString = result.getContents();
                if(qrString.contains("|")) {
                    String[] parts = qrString.split("\\|");
                    if(!parts[0].isEmpty() && parts[0]!=null)
                    {
                        _emailText.setText(parts[0]);
                    }
                    if(!parts[1].isEmpty() && parts[1]!=null)
                    {
                        _passwordText.setText(parts[1]);
                    }
                }
            }
        }


        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
