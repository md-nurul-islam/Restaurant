package com.muhib.restaurant.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.woocommerse.OAuth1.OauthConstants.ParameterList;
import com.woocommerse.OAuth1.services.HMACSha1SignatureService;
import com.woocommerse.OAuth1.services.TimestampServiceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by RR on 27-Dec-17.
 */

public class RetrofitApiClientWithInterceptor implements Interceptor{

    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_VERSION_VALUE = "1.0";

    private final String consumerKey="";
    private final String consumerSecret="";
//    www.champs21.com/wp-json/wp/v2/

    //public static final String BASE_URL = "http://champs21.com/wp-json/wp/v2/";
    public static final String BASE_URL = "http://woocom.endix.net/wc-auth/v1/";
    //public static final String BASE_URL = " https://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitApiClientWithInterceptor() {} // So that nobody can create an object with constructor

    public static synchronized Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getApiInterface(){
        return  RetrofitApiClientWithInterceptor.getClient().create(ApiInterface.class);
    }




    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        Log.d("URL", original.url().toString());
        Log.d("URL", original.url().scheme());
        Log.d("encodedpath", original.url().encodedPath());
        Log.d("query", ""+original.url().query());
        Log.d("path", ""+original.url().host());
        Log.d("encodedQuery", ""+original.url().encodedQuery());
        ;
        Log.d("method", ""+original.method());

        ////////////////////////////////////////////////////////////

        final String nonce = new TimestampServiceImpl().getNonce();
        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
        Log.d("nonce", nonce);
        Log.d("time", timestamp);

        String dynamicStructureUrl = original.url().scheme() + "://" + original.url().host() + original.url().encodedPath();

        Log.d("ENCODED PATH", ""+dynamicStructureUrl);
        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
        Log.d("firstBaseString", firstBaseString);
        String generatedBaseString = "";


        if(original.url().encodedQuery()!=null) {
            generatedBaseString = original.url().encodedQuery() + "&oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
        }
        else
        {
            generatedBaseString = "oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";

        }

        ParameterList result = new ParameterList();
        result.addQuerystring(generatedBaseString);
        generatedBaseString=result.sort().asOauthBaseString();
        Log.d("Sorted","00--"+result.sort().asOauthBaseString());

        String secoundBaseString = "&" + generatedBaseString;

        if (firstBaseString.contains("%3F")) {
            Log.d("iff","yess iff");
            secoundBaseString = "%26" + urlEncoded(generatedBaseString);
        }

        String baseString = firstBaseString + secoundBaseString;

        String signature = new HMACSha1SignatureService().getSignature(baseString, consumerSecret, "");
        Log.d("Signature", signature);

        HttpUrl url = originalHttpUrl.newBuilder()

                .addQueryParameter(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE)
                .addQueryParameter(OAUTH_CONSUMER_KEY, consumerKey)
                .addQueryParameter(OAUTH_VERSION, OAUTH_VERSION_VALUE)
                .addQueryParameter(OAUTH_TIMESTAMP, timestamp)
                .addQueryParameter(OAUTH_NONCE, nonce)
                .addQueryParameter(OAUTH_SIGNATURE, signature)


                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public String urlEncoded(String url) {
        String encodedurl = "";
        try {

            encodedurl = URLEncoder.encode(url, "UTF-8");
            Log.d("TEST", encodedurl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedurl;
    }

}

