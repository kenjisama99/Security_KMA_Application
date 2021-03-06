package com.example.kma_application.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kma_application.AsyncTask.LoginTask;
import com.example.kma_application.R;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECGenParameterSpec;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    EditText txtPhone,txtPassword;
    Button btLogin;
    ImageView bt_fingerprint;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Init view
        txtPhone = (EditText)findViewById(R.id.editTextPhone);
        txtPassword = (EditText)findViewById(R.id.editTextTextPassword);
        btLogin = (Button)findViewById(R.id.buttonLogin);
        bt_fingerprint = findViewById(R.id.bt_fingerprint);

//        pref = getApplicationContext().getSharedPreferences("KMA_App_Pref", MODE_PRIVATE);

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            pref = EncryptedSharedPreferences.create(
                    "Secret_KMA_App_Pref",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor = pref.edit();

        //Event handle
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtLogin();
            }
        });
        bt_fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtFingerprint();
            }
        });
    }

    private void onClickBtFingerprint() {
        //ki???m tra ???? l??u m???t kh???u ch??a
        String storedPassword = pref.getString("password", "");
        if (TextUtils.isEmpty(storedPassword)){
            Toast.makeText(this,"B???n c???n ????ng nh???p b???ng m???t kh???u ??t nh???t m???t l???n tr?????c khi s??? d???ng t??nh n??ng n??y!",Toast.LENGTH_LONG).show();
            return;
        }

        BiometricManager biometricManager = BiometricManager.from(this);
        //ki???m tra c???m bi???n
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this,"Thi???t b??? kh??ng c?? c???m bi???n v??n tay!",Toast.LENGTH_LONG).show();
                return;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this,"B???n ch??a l??u v??n tay n??o, h??y ki???m tra trong c??i ?????t b???o m???t!",Toast.LENGTH_LONG).show();
                return;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this,"C???m bi???n v??n tay hi???n kh??ng ho???t ?????ng!",Toast.LENGTH_LONG).show();
                return;
        }

        //t???o box check v??n tay
        Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }
            //khi x??c th???c v??n tay h???p l???
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                onAuthFingerprintSucceeded();
            }
            //khi x??c th???c v??n tay KH??NG h???p l???
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("????ng nh???p")
                .setDescription("Ch???m v??o c???m bi???n v??n tay ????? ????ng nh???p")
                .setNegativeButtonText("H???y b???")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    //khi x??c th???c v??n tay h???p l???
    private void onAuthFingerprintSucceeded() {
        String storedPassword = pref.getString("password", "");
        String storedPhone = pref.getString("phone", "");
        new LoginTask(
                this,
                storedPhone,
                storedPassword,
                pref,
                editor
        ).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String storedPhone = pref.getString("phone", "");
        if (!TextUtils.isEmpty(storedPhone))
            txtPhone.setText(storedPhone);
    }

    private void onClickBtLogin() {
        loginUser(txtPhone.getText().toString().trim(),
                txtPassword.getText().toString().trim());
    }


    private void loginUser(String phone, String password) {
        if (TextUtils.isEmpty(phone)){

            Toast.makeText(this,"S??? ??i???n tho???i kh??ng ???????c ????? tr???ng",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){

            Toast.makeText(this,"M???t kh???u kh??ng ???????c ????? tr???ng",Toast.LENGTH_LONG).show();
            return;
        }
        new LoginTask(
                this,
                phone,
                password,
                pref,
                editor
        ).execute();
    }

}