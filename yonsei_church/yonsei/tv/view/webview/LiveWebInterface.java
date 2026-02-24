package yonsei_church.yonsei.tv.view.webview;

import android.webkit.JavascriptInterface;
import yonsei_church.yonsei.tv.utils.LogUtil;

public class LiveWebInterface {
    public static final String TAG = "LiveWebInterface";
    private OnJavascriptListener mListener;

    public interface OnJavascriptListener {
        void getLiveUrl(String str, String str2);
    }

    public LiveWebInterface(OnJavascriptListener onJavascriptListener) {
        this.mListener = onJavascriptListener;
    }

    @JavascriptInterface
    public void getLiveUrl(String str, String str2) {
        if (this.mListener != null) {
            LogUtil.e("getLiveUrl interface id : " + str + " url : " + str2);
            this.mListener.getLiveUrl(str, str2);
        }
    }
}
