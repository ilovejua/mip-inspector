package yonsei_church.yonsei.tv;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

public class ErrorFragment extends android.support.v17.leanback.app.ErrorFragment {
    private static final String TAG = "ErrorFragment";
    private static final boolean TRANSLUCENT = true;

    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate");
        super.onCreate(bundle);
        setTitle(getResources().getString(R.string.app_name));
    }

    /* access modifiers changed from: package-private */
    public void setErrorContent() {
        setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.lb_ic_sad_cloud));
        setMessage(getResources().getString(R.string.error_fragment_message));
        setDefaultBackground(true);
        setButtonText(getResources().getString(R.string.dismiss_error));
        setButtonClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ErrorFragment.this.getFragmentManager().beginTransaction().remove(ErrorFragment.this).commit();
            }
        });
    }
}
