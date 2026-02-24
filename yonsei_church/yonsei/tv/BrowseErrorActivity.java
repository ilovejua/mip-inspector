package yonsei_church.yonsei.tv;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class BrowseErrorActivity extends Activity {
    private static final int SPINNER_HEIGHT = 100;
    private static final int SPINNER_WIDTH = 100;
    private static final int TIMER_DELAY = 3000;
    /* access modifiers changed from: private */
    public ErrorFragment mErrorFragment;
    /* access modifiers changed from: private */
    public SpinnerFragment mSpinnerFragment;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        testError();
    }

    private void testError() {
        this.mErrorFragment = new ErrorFragment();
        getFragmentManager().beginTransaction().add(R.id.main_browse_fragment, this.mErrorFragment).commit();
        this.mSpinnerFragment = new SpinnerFragment();
        getFragmentManager().beginTransaction().add(R.id.main_browse_fragment, this.mSpinnerFragment).commit();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                BrowseErrorActivity.this.getFragmentManager().beginTransaction().remove(BrowseErrorActivity.this.mSpinnerFragment).commit();
                BrowseErrorActivity.this.mErrorFragment.setErrorContent();
            }
        }, 3000);
    }

    public static class SpinnerFragment extends Fragment {
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            ProgressBar progressBar = new ProgressBar(viewGroup.getContext());
            if (viewGroup instanceof FrameLayout) {
                progressBar.setLayoutParams(new FrameLayout.LayoutParams(100, 100, 17));
            }
            return progressBar;
        }
    }
}
