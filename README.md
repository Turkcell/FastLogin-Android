# FASTLOGIN (formerly DSSGate)

## 1. Overview

FastLogin provides integration to Turkcell login  systems. We have developed an SDK that is highly robust, secure, lightweight, configurable and very simple to embed.

You can initialize login , register, switch account flows.

The FastLogin Android SDK is compatible with Android 4.3 (API 18), Support Library 26 and above.  

## 2. Quick Start
### 2.1 Adding SDK to your Project

The simplest way to integrate the SDK into your project is by using Gradle’s Dependency Management.

Adding FastLogin’s Android SDK Dependency:
1.	Open your project and then open your_app | build.gradle
2.	Add this to Module-level /app/build.gradle before dependencies:

```
    repositories {
        maven {
            url 'https://mymavenrepo.com/repo/ukAiuNSVkftQiB4kKUPH/'
            name 'FastLogin Repo'
        }
    }
```

Add the compile dependency with the latest version of the FastLogin SDK in the build.gradle file:

```
    compile ('com.turkcell.dssgate:dssgate-aar:1.+')
    implementation 'com.turkcell.dssgate:dssgate-client:1.0.2'
```

### 2.2 Setting the Required Permissions

The AndroidManifest.xml should include the following permissions:

```
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
```

## 3. SDK Initialization

Initialization of the SDK is completed in two stages. In the first stage the DGLoginCoordinator is created by using Builder Pattern. When building  DGLoginCoordinator, appId is required where theme and language parameters are optional. In the second stage the call to one of the main flows(e.g. call startForLogin, startForSwitchAccount, startForRegister) is needed. 

Optional Parameters;
* theme: default theme of the application exists. See Section 10.
* language: default language is TR
* environment: default environment is PROD


To initialize the SDK, add the following code in your Application activity or fragment:

```Java
    DGLoginCoordinator dg = new DGLoginCoordinator.Builder().appId(your_app_id).build();
```

## 4. SDK START LOGIN

Having initialized the SDK, start login one of the main flows to call. It takes three boolean parameters;
* disableCellLogin: if true, cellular login functionality won’t work.
* autoLoginOnly: if true, only cellular login and remember me will work
* disableAutoLogin: if true, login process is forced to user.
* dismissCloseButton: if true, close button will be disabled for all screens.

```Java
    try {
            dg.startForLogin(this, disableCellLogin, autoLoginOnly, disableAutoLogin, dismissCloseButton);
    } catch (DGException e) {
            //application error handling, e.g. required appId
    }
```

For getting result, see Section 10.

## 5. SDK Start Register

Having initialized the SDK, start register one of the main flows to call. It runs without parameters;

```Java
    try {
        dg.startForRegister(this);
    } catch (DGException e) {
        //application error handling, e.g. required appId
    }
```

For getting result, see Section 10.

## 6. SDK Switch Account

Having initialized the SDK, switch account one of the main flows to call. It runs without parameters;

```Java
    try {
        dg.startForSwitchAccount(this);
    } catch (DGException e) {
        //application error handling, e.g. required appId
    }
```

For getting result, see Section 10.

## 7. SDK START LOGIN WITH TRANSFER TOKEN

Having initialized the SDK, start login with transfer token one of the main flows to call. It takes three boolean parameters and one string parameter;

* disableCellLogin: if true, cellular login functionality won’t work.
* autoLoginOnly: if true, only cellular login and remember me will work
* disableAutoLogin: if true, login process is forced to user.
* dismissCloseButton: if true, close button will be disabled for all screens.
* transferToken: transfer token that taken from the backend must be passed.

```Java
    try {
            dg.startForLoginWithTransferToken(this, disableCellLogin, autoLoginOnly, disableAutoLogin, dismissCloseButton, transferToken);
    } catch (DGException e) {
            //application error handling, e.g. required appId
    }

```

For getting result, see Section 10.

## 8. SDK WIDGET LOGIN

Having initialized the SDK, widget login one of the main flows to call. It doesn’t take any parameters. However, it only works if there is an active rememberme login in the relative application.

```Java
    try {
        dg.startForWidgetLogin(getApplicationContext());
    } catch (DGException e) {
        //application error handling, e.g. required appId
    }


```

For getting result, see Section 9.

## 9. SDK WIDGET RESULT

To get the result from SDK, a class needed that is extended from BroadcastReceiver and relative action must be registered. See below example code:

```Java
    IntentFilter filter = new IntentFilter();
    filter.addAction(DGLoginCoordinator.DG_WIDGET_BROADCAST_RESULT);

    WidgetReceiver myReceiver = new WidgetReceiver(this);
    registerReceiver(myReceiver, filter);

```

After registration,  DGResult can be taken like below example code;

```Java
    
    if (DGLoginCoordinator.DG_WIDGET_BROADCAST_RESULT.equals(intent.getAction())) {
        DGResult dgResult = DGLoginCoordinator.getDGResult(intent);

        //dgResult has the result, take action according to result
    }

```

Result Type | Description | Action
--- | --- | ---
DGResultType.SUCCESS_LOGIN | Successful Login | You can take loginToken and continue
DGResultType.SUCCESS_NO_LOGIN | (ONLY FOR WIDGET) Login needed in the application | For successful login to widget, there must be a logged in user. You can force user for application login
DGResultType.ERROR_APPLICATON | There is a system error in fastlogin | You can continue Non-Login or force user to login again
DGResultType.ERROR_SESSION_LOST | Session lost during fastlogin processes. | You can continue Non-Login or force user to login by starting fastlogin again

## 10. SDK Result

To get the result from SDK, onActivityResult method must be overridden in the application’s Activity or Fragment.

Add the following code in your Application’s fragment or activity:

```Java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DGLoginCoordinator.DG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Unexpected Error Case
            }
            if (resultCode == Activity.RESULT_OK) {
                DGResult dgResult = DGLoginCoordinator.getDGResult(data);
                //dgResult has the result, take action according to result
            }
        }
    }
```

These are fastlogin result types;


Result Type | Description | Action
--- | --- | ---
DGResultType.SUCCESS_LOGIN | Successful Login | You can take loginToken and continue
DGResultType.ERROR_USER_QUIT | User intentionaly quits from fastlogin | You can continue Non-Login or force user to login again by starting fastlogin again
DGResultType.ERROR_APPLICATON | There is a system error in fastlogin | You can continue Non-Login or force user to login again
DGResultType.ERROR_SESSION_LOST | Session lost during fastlogin processes. | You can continue Non-Login or force user to login by starting fastlogin again


## 11. SDK Logout
To logout from the system, there is a static method to call. Re-initialization of DGLoginCoordinator is not needed. Add the following code in your Application:

```Java
    DGLoginCoordinator.logout(this, your_app_id);
```

For logout, there is no result. Having called the logout method is enough.

## 12. SDK Style Configuration
The configuration of the sdk can be achieved by creating DGTheme and passing DGTheme to DGLoginCoordinator. Builder pattern is used for creating the DGTheme. 

Sample code for configuring the style:

```Java
    DGTheme dgTheme = new DGTheme.Builder()
            .setBackgroundColor(android.R.color.holo_green_light)
            .setTitleLabelColor(android.R.color.holo_red_dark)
            .setDescriptionTextColor(android.R.color.holo_orange_dark)
            .setCheckBoxPassiveIcon(R.drawable.dg_checkbox_normal)
            .setPositiveButtonTextColor(android.R.color.black)
            .build();

    DGLoginCoordinator dg = new DGLoginCoordinator.Builder().theme(dgTheme).appId(your_app_id).build();
```

For detailed description of customizing styles see Fastlogin Android documentation.

## 13. PROGUARD CONFIGURATION
If you are using ProGuard in your project add the following lines to your configuration:

```
-keep class com.turkcell.dssgate.client.** { *; }
```

## 14. FAQ

**Q: Do we need to set theme in the DGLoginCoordinator?**

A: No you don’t. There is a default theme in Fastlogin, if you don’t need a special theme then you can continue with the default one by not setting or just setting null for theme method in DGLoginCoordinator.

**Q: Is there a test Environment for Fastlogin?**

A: Yes there are two environments for Fastlogin. One for prod, other for test. Default environment is prod. For test initialization use following code:
```Java
 DGLoginCoordinator dg = new DGLoginCoordinator.Builder().appId(app_id).environment(DGEnv.TEST).build();

```

**Q: Repo url starts with “http://mymavenrepo…”, is this real repo?**

A: Yes it is a real repository. Our need was a free public repo. This is the reason we are using it.


# Changelog

**v1.1.8**

Language related npe bug fixed.

**v1.1.7**

Android 6 webview keypad input bug fixed.

**v1.1.6**

New error detail added for 'no response from server' scenarios.

**v1.1.5**

New error detail added for 'server down' scenarios.

**v1.1.3**

DGUtil.getGADeviceID(context) method renamed to FastLoginUtil.getGADeviceID(context).

**v1.1.2**

Google Analytics integration key changed.

**v1.1.1 - Deprecated**

Unique key generator, DGUtil.getGADeviceID(context), added for optional Google Analytics integration.

