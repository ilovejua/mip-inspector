package yonsei_church.yonsei.tv.view.Intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.api.ApiProvider;
import yonsei_church.yonsei.tv.api.ITvAPI;
import yonsei_church.yonsei.tv.utils.LogUtil;
import yonsei_church.yonsei.tv.utils.PreferenceManager;
import yonsei_church.yonsei.tv.utils.Utils;
import yonsei_church.yonsei.tv.utils.WaitingDialog;
import yonsei_church.yonsei.tv.view.landing.activity.PageAndListRowActivity;

public class IntroActivity extends Activity {
    private ApiProvider apiProvider;
    private PreferenceManager pref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_intro);
        this.pref = PreferenceManager.getInstance(this);
        LogUtil.e("pref : " + this.pref.getString("userId", "") + " id : " + Utils.getAndroidId(this) + " version : " + Utils.getAppVersion(this));
        if (this.pref.getString("userId", "") == null || this.pref.getString("userId", "").isEmpty()) {
            getAppInfo(Utils.getAndroidId(this), "T", Utils.getAppVersion(this), "");
        } else {
            getAppInfo(Utils.getAndroidId(this), "T", Utils.getAppVersion(this), this.pref.getString("userId", ""));
        }
    }

    /* access modifiers changed from: private */
    public void goMainActivity() {
        startActivity(new Intent(this, PageAndListRowActivity.class));
        finish();
    }

    private void getAppInfo(String str, String str2, String str3, String str4) {
        WaitingDialog.showWaitingDialog(this);
        ApiProvider apiProvider2 = this.apiProvider;
        ((ITvAPI) ApiProvider.createService(ITvAPI.class, this)).getAppInfo(str, str2, str3, str4).enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                WaitingDialog.cancelWaitingDialog();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        IntroActivity.this.goMainActivity();
                    }
                }, 3000);
            }

            public void onFailure(Call<Void> call, Throwable th) {
                WaitingDialog.cancelWaitingDialog();
            }
        });
    }
}
