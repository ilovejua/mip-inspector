package yonsei_church.yonsei.tv.view.search.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yonsei_church.yonsei.tv.api.ApiProvider;
import yonsei_church.yonsei.tv.api.ITvAPI;
import yonsei_church.yonsei.tv.api.model.CustomListRow;
import yonsei_church.yonsei.tv.api.vo.VideoItem;
import yonsei_church.yonsei.tv.presenter.CustomListRowPresenter;
import yonsei_church.yonsei.tv.presenter.VideoCardPresenter;
import yonsei_church.yonsei.tv.utils.LogUtil;
import yonsei_church.yonsei.tv.utils.PreferenceManager;
import yonsei_church.yonsei.tv.utils.Utils;
import yonsei_church.yonsei.tv.utils.WaitingDialog;
import yonsei_church.yonsei.tv.view.playback.activity.PlaybackActivity;

public class SearchFragment extends android.support.v17.leanback.app.SearchFragment implements SearchFragment.SearchResultProvider {
    private static final int REQUEST_SPEECH = 16;
    private static final String TAG = "SearchFragment";
    /* access modifiers changed from: private */
    public static PreferenceManager mPref;
    private ApiProvider apiProvider;
    /* access modifiers changed from: private */
    public int curPage;
    final Handler getSearchListHandler = new Handler() {
        public void handleMessage(Message message) {
            int unused = SearchFragment.this.curPage = Integer.parseInt(SearchFragment.this.mPageNumber);
            SearchFragment.access$408(SearchFragment.this);
            if (SearchFragment.mPref.getString("userId", "") == null || SearchFragment.mPref.getString("userId", "").isEmpty()) {
                SearchFragment.this.getSearchList("", String.valueOf(SearchFragment.this.curPage), SearchFragment.this.mKey, SearchFragment.this.mQuery, 10);
            } else {
                SearchFragment.this.getSearchList(SearchFragment.mPref.getString("userId", ""), String.valueOf(SearchFragment.this.curPage), SearchFragment.this.mKey, SearchFragment.this.mQuery, 10);
            }
            String unused2 = SearchFragment.this.mPageNumber = String.valueOf(SearchFragment.this.curPage);
        }
    };
    /* access modifiers changed from: private */
    public ArrayObjectAdapter listRowAdapter;
    /* access modifiers changed from: private */
    public String mKey = "S000";
    /* access modifiers changed from: private */
    public String mPageNumber = "1";
    /* access modifiers changed from: private */
    public String mQuery;
    private ArrayObjectAdapter mRowsAdapter;

    static /* synthetic */ int access$408(SearchFragment searchFragment) {
        int i = searchFragment.curPage;
        searchFragment.curPage = i + 1;
        return i;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPref = PreferenceManager.getInstance(getActivity());
        this.mRowsAdapter = new ArrayObjectAdapter((Presenter) new CustomListRowPresenter());
        setSearchResultProvider(this);
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
        if (!Utils.hasPermission(getActivity(), "android.permission.RECORD_AUDIO")) {
            LogUtil.d("no permission RECORD_AUDIO");
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                public void recognizeSpeech() {
                    LogUtil.d("recognizeSpeech");
                    try {
                        SearchFragment.this.startActivityForResult(SearchFragment.this.getRecognizerIntent(), 16);
                    } catch (ActivityNotFoundException e) {
                        LogUtil.d("Cannot find activity for speech recognizer" + e);
                    }
                }
            });
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        LogUtil.e("onActivityResult requestCode=" + i + " resultCode=" + i2 + " data=" + intent);
        if (i == 16 && i2 == -1) {
            setSearchQuery(intent, true);
        }
    }

    public ObjectAdapter getResultsAdapter() {
        LogUtil.e("getResultsAdapter");
        return this.mRowsAdapter;
    }

    public boolean onQueryTextChange(String str) {
        LogUtil.e(String.format("Search Query Text Change %s", new Object[]{str}));
        return true;
    }

    public boolean onQueryTextSubmit(String str) {
        LogUtil.e(String.format("Search Query Text Submit %s", new Object[]{str}));
        if (this.mRowsAdapter.size() > 0) {
            this.mRowsAdapter.clear();
            this.mPageNumber = "1";
        }
        this.mQuery = str;
        if (mPref.getString("userId", "") == null || mPref.getString("userId", "").isEmpty()) {
            getSearchList("", this.mPageNumber, this.mKey, this.mQuery, 10);
        } else {
            getSearchList(mPref.getString("userId", ""), this.mPageNumber, this.mKey, this.mQuery, 10);
        }
        return true;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        private ItemViewClickedListener() {
        }

        public void onItemClicked(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
            if (obj instanceof VideoItem) {
                Intent intent = new Intent(SearchFragment.this.getActivity(), PlaybackActivity.class);
                intent.putExtra("Video", (VideoItem) obj);
                intent.putExtra("play_key", SearchFragment.this.mKey);
                SearchFragment.this.getActivity().startActivity(intent);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        private ItemViewSelectedListener() {
        }

        public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
            int indexOf;
            if ((obj instanceof VideoItem) && (indexOf = SearchFragment.this.listRowAdapter.indexOf(obj)) != -1 && SearchFragment.this.listRowAdapter.size() - 1 == indexOf) {
                SearchFragment.this.getSearchListHandler.sendMessage(SearchFragment.this.getSearchListHandler.obtainMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public void loadLists(List<VideoItem> list) {
        this.listRowAdapter = new ArrayObjectAdapter((Presenter) new VideoCardPresenter());
        if (list != null) {
            this.listRowAdapter.addAll(0, list);
        }
        if (this.mPageNumber.equals("1")) {
            CustomListRow customListRow = new CustomListRow(new HeaderItem("Search results : " + this.mQuery), this.listRowAdapter);
            customListRow.setNumRows(1);
            this.mRowsAdapter.add(customListRow);
            return;
        }
        CustomListRow customListRow2 = new CustomListRow(this.listRowAdapter);
        customListRow2.setNumRows(1);
        this.mRowsAdapter.add(customListRow2);
    }

    /* access modifiers changed from: private */
    public void getSearchList(String str, String str2, String str3, String str4, int i) {
        WaitingDialog.showWaitingDialog(getActivity());
        ApiProvider apiProvider2 = this.apiProvider;
        ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getSearchList(str, str2, str3, str4, i).enqueue(new Callback<List<VideoItem>>() {
            public void onResponse(Call<List<VideoItem>> call, Response<List<VideoItem>> response) {
                WaitingDialog.cancelWaitingDialog();
                List body = response.body();
                if (body != null && body.size() > 0) {
                    SearchFragment.this.loadLists(body);
                }
            }

            public void onFailure(Call<List<VideoItem>> call, Throwable th) {
                WaitingDialog.cancelWaitingDialog();
                LogUtil.e("onFailure : " + th.toString());
            }
        });
    }
}
