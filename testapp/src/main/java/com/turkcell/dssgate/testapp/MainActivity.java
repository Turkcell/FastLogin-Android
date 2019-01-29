package com.turkcell.dssgate.testapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.turkcell.dssgate.DGLoginCoordinator;
import com.turkcell.dssgate.DGTheme;
import com.turkcell.dssgate.client.model.DGLanguage;
import com.turkcell.dssgate.model.DGEnv;
import com.turkcell.dssgate.model.exception.DGException;
import com.turkcell.dssgate.model.result.DGResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String NORMAL_FLOW = "NORMAL_FLOW";
    String demoFlowType;
    DGLanguage language;
    private Button buttonLogin;
    private Button buttonMCLogin;
    private Button buttonRegister;
    private Button buttonAccountChange;
    private Button buttonWidgetLogin;
    private Button buttonLogOut;
    private Spinner spinner;
    private Spinner spinnerLanguage;
    private Spinner spinnerEnv;
    private TextView textViewResult;
    private List<String> spinnerItemList;
    private List<String> spinnerLanguageItemList;
    private List<String> spinnerEnvList;
    private EditText appId;
    private EditText transferToken;
    private CheckBox disableCellLogin;
    private CheckBox autoLoginOnly;
    private CheckBox disableAutoLogin;
    private DGEnv env;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_main);
        spinner = findViewById(R.id.spinner);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        spinnerEnv = findViewById(R.id.spinnerEnv);
        textViewResult = findViewById(R.id.textViewResult);
        buttonLogin = findViewById(R.id.button);
        buttonMCLogin = findViewById(R.id.buttonMCLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonAccountChange = findViewById(R.id.buttonAccountChange);
        buttonWidgetLogin = findViewById(R.id.buttonWidgetLogin);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        appId = findViewById(R.id.appId);
        transferToken = findViewById(R.id.transferToken);
        disableCellLogin = findViewById(R.id.disableCellLogin);
        autoLoginOnly = findViewById(R.id.autoLoginOnly);
        disableAutoLogin = findViewById(R.id.disableAutoLogin);

        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 666);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(true);
                openLoginSdkForStart();
            }
        });

        buttonMCLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(true);
                openLoginSdkForMCLogin();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(true);
                openLoginSdkForRegister();
            }
        });

        buttonAccountChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(true);
                openLoginSdkForAccounChange();
            }
        });

        buttonWidgetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnvironment(false);
                openLoginSdkForWidgetLogin();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnvironment(false);
                DGLoginCoordinator.logout(MainActivity.this, getAppId());
                textViewResult.setText(String.format(" Result -> Client : Logged out - Server : Unknown "));
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(DGLoginCoordinator.DG_WIDGET_BROADCAST_RESULT);

        WidgetReceiver myReceiver = new WidgetReceiver(this);
        registerReceiver(myReceiver, filter);


        spinnerItemList = new ArrayList<>();
        spinnerItemList.add(NORMAL_FLOW);
        spinnerItemList.add("ACTIVE_REMEMBERME_LOGIN");
        spinnerItemList.add("ACTIVE_REMEMBERME_LOGIN_SHOW_DIGITAL_ID_ENTRY");
        spinnerItemList.add("ACTIVE_REMEMBERME_LOGIN_SHOW_EMAIL_ENTRY");
        spinnerItemList.add("ACTIVE_REMEMBERME_LOGIN_SHOW_GSM_ENTRY");
        spinnerItemList.add("ACCOUNT_PASSWORD_LOGIN");
        spinnerItemList.add("SHOW_LOGIN_REGISTERREQUIRED");
        spinnerItemList.add("MSISDN_LOGIN_REQUIRED");
        spinnerItemList.add("DIGITAL_ID_REGISTERREQUIRED");
        spinnerItemList.add("DIGITAL_ID_VERIFYEMAIL_WARN");
        spinnerItemList.add("DIGITAL_ID_VERIFYEMAIL_ERROR");
        spinnerItemList.add("SHOW_LOGIN_REGISTERREQUIRED");
        spinnerItemList.add("MC_LOGIN");
        spinnerItemList.add("SHOW_SELECT_PAGE");

        spinnerLanguageItemList = new ArrayList<>();
        spinnerLanguageItemList.add(DGLanguage.TR.name());
        spinnerLanguageItemList.add(DGLanguage.EN.name());
        spinnerLanguageItemList.add(DGLanguage.UK.name());
        spinnerLanguageItemList.add(DGLanguage.RU.name());
        spinnerLanguageItemList.add(DGLanguage.AR.name());
        spinnerLanguageItemList.add(DGLanguage.SK.name());
        spinnerLanguageItemList.add(DGLanguage.HE.name());
        spinnerLanguageItemList.add(DGLanguage.FR.name());

        spinnerEnvList = new ArrayList<>();
        spinnerEnvList.add(DGEnv.TEST.name());
        spinnerEnvList.add(DGEnv.PROD.name());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItemList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerLanguageItemList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerLanguage.setAdapter(adapterLanguage);

        ArrayAdapter<String> adapterEnv = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerEnvList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerEnv.setAdapter(adapterEnv);

        appId.setText("202");
    }

    private void setEnvironment(boolean setDemoFlow) {
        if (setDemoFlow) {
            demoFlowType = spinnerItemList.get(spinner.getSelectedItemPosition());
            if (demoFlowType.equals(NORMAL_FLOW))
                demoFlowType = null;
        }

        env = DGEnv.valueOf(spinnerEnvList.get(spinnerEnv.getSelectedItemPosition()));
        language = DGLanguage.valueOf(spinnerLanguageItemList.get(spinnerLanguage.getSelectedItemPosition()));
    }

    private void openLoginSdkForRegister() {
        DGLoginCoordinator dg = new DGLoginCoordinator.Builder().theme(null).appId(getAppId()).environment(env).language(language).build();

        try {
            dg.startForRegister(this, demoFlowType);
        } catch (DGException e) {
            e.printStackTrace();
        }
    }

    private void openLoginSdkForStart() {
        DGTheme digitalGateTheme = getDigitalGateTheme();
        DGLoginCoordinator dg = new DGLoginCoordinator.Builder().theme(digitalGateTheme).appId(getAppId()).environment(env).language(language).build();

        try {
            dg.startForLoginWithTransferToken(this, disableCellLogin.isChecked(), autoLoginOnly.isChecked(), disableAutoLogin.isChecked(), false, demoFlowType, getTransferToken());
        } catch (DGException e) {
            e.printStackTrace();
        }
    }

    private void openLoginSdkForMCLogin() {
        DGLoginCoordinator loginCoordinator = new DGLoginCoordinator.Builder().theme(getDigitalGateTheme()).appId(getAppId()).environment(env).language(language).build();

        try {
            loginCoordinator.startForMCLogin(this, demoFlowType);
        } catch (DGException e) {
            e.printStackTrace();
        }
    }

    private DGTheme getDigitalGateTheme() {
        DGTheme dgTheme = new DGTheme.Builder().setPopUpBottomColor(android.R.color.black).setPopUpTopColor(R.color.dg_c_fedf32).setPopupTitleLabelColor(R.color.dg_c_1ca1e4).setPopupDescriptionTextColor(android.R.color.holo_red_dark).setRoundedFillButtonBackgroundColor(android.R.color.holo_green_dark).setRoundedFillButtonTextColor(android.R.color.black).setRoundedTransparentButtonBorderColor(android.R.color.black).setRoundedTransparentButtonTextColor(android.R.color.holo_red_light).setBackgroundColor(android.R.color.holo_green_light).setTitleLabelColor(android.R.color.holo_red_dark).setDescriptionTextColor(android.R.color.holo_orange_dark).setCheckBoxPassiveIcon(R.drawable.dg_checkbox_normal).setPositiveButtonBackgroundColor(android.R.color.darker_gray).setPositiveButtonTextColor(android.R.color.black).setRegisterIcon(R.drawable.dg_icon_bin).build();
        return dgTheme;
    }

    private void openLoginSdkForWidgetLogin() {
        DGLoginCoordinator dg = new DGLoginCoordinator.Builder().theme(null).appId(getAppId()).environment(env).language(language).build();

        try {
            dg.startForWidgetLogin(getApplicationContext());
        } catch (DGException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    private Integer getAppId() {
        Integer appId;
        if (!TextUtils.isEmpty(this.appId.getText().toString())) {
            appId = Integer.valueOf(this.appId.getText().toString());
        } else {
            appId = 2;
        }
        return appId;
    }

    @NonNull
    private String getTransferToken() {
        String transferToken;
        if (!TextUtils.isEmpty(this.transferToken.getText().toString())) {
            transferToken = this.transferToken.getText().toString();
        } else {
            transferToken = null;
        }
        return transferToken;
    }

    private void openLoginSdkForAccounChange() {


        DGTheme dgTheme = new DGTheme.Builder()
                .setBackgroundColor(android.R.color.holo_blue_light)
                .setTitleLabelColor(android.R.color.holo_red_dark)
                .setDescriptionTextColor(android.R.color.holo_orange_dark)
                .setCheckBoxPassiveIcon(R.drawable.dg_checkbox_normal)
                .setPositiveButtonBackgroundColor(android.R.color.darker_gray)
                .setPositiveButtonTextColor(android.R.color.black)
                .setRegionSelectIcon(R.drawable.dg_checkbox_active).build();




        DGLoginCoordinator dg = new DGLoginCoordinator.Builder().theme(dgTheme).appId(getAppId()).environment(env).language(language).build();

        try {
            dg.startForSwitchAccount(this, demoFlowType);
        } catch (DGException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DGLoginCoordinator.DG_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "activtyResultError", Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_OK) {
                DGResult dgResult = DGLoginCoordinator.getDGResult(data);
                textViewResult.setText(String.format(" Result :  %s", dgResult.toString()));
                Toast.makeText(this, dgResult.getDgResultType().getResultMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showWidgetResult(Intent data) {
        DGResult dgResult = DGLoginCoordinator.getDGResult(data);
        textViewResult.setText(String.format(" Result :  %s", dgResult.toString()));
        Toast.makeText(this, dgResult.getDgResultType().getResultMessage(), Toast.LENGTH_LONG).show();
    }

}
