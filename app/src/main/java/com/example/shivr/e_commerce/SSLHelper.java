package com.example.shivr.e_commerce;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by mdls8 on 4/23/2017.
 */

public final class SSLHelper {
    // Singleton class responsible for handling app making connections to server with self signed certificate

    private static SSLHelper instance=null;
    private CertificateFactory crtFac;
    private Certificate certificate;
    private KeyStore keystore;
    private SSLContext sslContext;
    private InputStream inputStream;
    private static Context context;

    // private constructor to restrict instantiation to this class
    private SSLHelper(Context c){
        context = c;
        loadSelfSigned();
        createKeyStore();
        createTrustManager();
    }

    public static SSLHelper getInstance(Context c){
        // uses double checked locking to make sure only one thread can execute at a time and not unnecessarily locking the method without first checking if it needs to be locked
        if(instance == null){
            // we no give thread that reached this position lock, otherwise no lock is necessary
            synchronized (SSLHelper.class){
                if(instance == null){
                    instance = new SSLHelper(c);
                }
            }
        }
        context = c;

        return instance;
    }

    private void loadSelfSigned(){

        try{
            crtFac = CertificateFactory.getInstance("X.509");

            inputStream = context.getResources().getAssets().open("apache-selfsigned.crt");
            certificate = crtFac.generateCertificate(inputStream);
        }
        catch (CertificateException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                inputStream.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void createKeyStore(){
        // creates a KeyStore containing our trusted self signed certificate

        try{
            String keyStoreType = KeyStore.getDefaultType();
            keystore = KeyStore.getInstance(keyStoreType);

            keystore.load(null,null);
            keystore.setCertificateEntry("ssc", certificate);

        }
        catch (KeyStoreException e){
            e.printStackTrace();
        }
        catch (CertificateException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    private void createTrustManager() {
        // Create a TrustManager that trusts the self signed certificate in our KeyStore

        try {
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keystore);

            // create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public SSLContext getSSLContext(){
        return sslContext;
    }
}