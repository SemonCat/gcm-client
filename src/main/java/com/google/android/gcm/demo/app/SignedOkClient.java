package com.google.android.gcm.demo.app;

import java.io.IOException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;

public class SignedOkClient extends OkClient {

    private RetrofitHttpOAuthConsumer mOAuthConsumer;

    private String Key;
    private String Secret;

    public SignedOkClient(OkHttpClient client) {
        super(client);
    }

    public SignedOkClient(OkHttpClient client, RetrofitHttpOAuthConsumer consumer) {
        super(client);
        mOAuthConsumer = consumer;
    }

    public void setTokenWithSecret(String Key, String Secret) {
        this.Key = Key;
        this.Secret = Secret;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Request requestToSend = request;
        try {
            if (!TextUtils.isEmpty(Key) && !TextUtils.isEmpty(Secret)) {
                mOAuthConsumer.setTokenWithSecret(Key, Secret);
            }
            HttpRequestAdapter signedAdapter = (HttpRequestAdapter) mOAuthConsumer.sign(request);
            requestToSend = (Request) signedAdapter.unwrap();
        } catch (OAuthMessageSignerException e) {
            // Fail to sign, ignore
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            // Fail to sign, ignore
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            // Fail to sign, ignore
            e.printStackTrace();
        }


        return super.execute(requestToSend);


    }
}