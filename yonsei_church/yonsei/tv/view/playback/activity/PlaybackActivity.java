package yonsei_church.yonsei.tv.view.playback.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import yonsei_church.yonsei.tv.view.playback.fragment.PlaybackVideoFragment;

public class PlaybackActivity extends FragmentActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(16908290, new PlaybackVideoFragment()).commit();
        }
    }
}
