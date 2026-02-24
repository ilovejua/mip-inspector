package yonsei_church.yonsei.tv.view.search.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.view.search.fragment.SearchFragment;

public class SearchActivity extends Activity {
    private static final String TAG = "SearchActivity";
    public static final String VIDEO = "Video";
    private SearchFragment mSearchFragment;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_search);
        this.mSearchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
    }

    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}
