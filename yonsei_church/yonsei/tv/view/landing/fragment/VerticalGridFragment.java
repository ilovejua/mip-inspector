package yonsei_church.yonsei.tv.view.landing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yonsei_church.yonsei.tv.api.ApiProvider;
import yonsei_church.yonsei.tv.api.ITvAPI;
import yonsei_church.yonsei.tv.api.vo.VideoItem;
import yonsei_church.yonsei.tv.presenter.VideoCardPresenter;
import yonsei_church.yonsei.tv.utils.LogUtil;
import yonsei_church.yonsei.tv.utils.PreferenceManager;
import yonsei_church.yonsei.tv.utils.WaitingDialog;
import yonsei_church.yonsei.tv.view.playback.activity.PlaybackActivity;

public class VerticalGridFragment extends android.support.v17.leanback.app.VerticalGridFragment {
    private static final int NUM_COLUMNS = 5;
    private static final String TAG = "VerticalGridFragment";
    /* access modifiers changed from: private */
    public static PreferenceManager mPref;
    private ApiProvider apiProvider;
    /* access modifiers changed from: private */
    public int curPage;
    final Handler getVideoListHandler = new Handler() {
        public void handleMessage(Message message) {
            int unused = VerticalGridFragment.this.curPage = Integer.parseInt(VerticalGridFragment.this.mPageNumber);
            VerticalGridFragment.access$408(VerticalGridFragment.this);
            if (VerticalGridFragment.mPref.getString("userId", "") == null || VerticalGridFragment.mPref.getString("userId", "").isEmpty()) {
                VerticalGridFragment.this.getVideoList("", String.valueOf(VerticalGridFragment.this.curPage), VerticalGridFragment.this.mKey);
            } else {
                VerticalGridFragment.this.getVideoList(VerticalGridFragment.mPref.getString("userId", ""), String.valueOf(VerticalGridFragment.this.curPage), VerticalGridFragment.this.mKey);
            }
            String unused2 = VerticalGridFragment.this.mPageNumber = String.valueOf(VerticalGridFragment.this.curPage);
        }
    };
    private String mCategory;
    /* access modifiers changed from: private */
    public String mKey;
    private List<VideoItem> mList;
    /* access modifiers changed from: private */
    public String mPageNumber = "1";
    /* access modifiers changed from: private */
    public ArrayObjectAdapter mRowsAdapter;

    static /* synthetic */ int access$408(VerticalGridFragment verticalGridFragment) {
        int i = verticalGridFragment.curPage;
        verticalGridFragment.curPage = i + 1;
        return i;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPref = PreferenceManager.getInstance(getActivity());
        this.mCategory = getActivity().getIntent().getStringExtra("category2");
        this.mKey = getActivity().getIntent().getStringExtra("key2");
        if (mPref.getString("userId", "") == null || mPref.getString("userId", "").isEmpty()) {
            getVideoList("", "1", this.mKey);
        } else {
            getVideoList(mPref.getString("userId", ""), "1", this.mKey);
        }
        setTitle(this.mCategory);
        setupFragment();
        setupEventListeners();
    }

    private void setupFragment() {
        VerticalGridPresenter verticalGridPresenter = new VerticalGridPresenter();
        verticalGridPresenter.setNumberOfColumns(5);
        setGridPresenter(verticalGridPresenter);
        this.mRowsAdapter = new ArrayObjectAdapter((Presenter) new VideoCardPresenter());
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        private ItemViewClickedListener() {
        }

        public void onItemClicked(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
            if (obj instanceof VideoItem) {
                Intent intent = new Intent(VerticalGridFragment.this.getActivity(), PlaybackActivity.class);
                intent.putExtra("Video", (VideoItem) obj);
                intent.putExtra("play_key", VerticalGridFragment.this.mKey);
                VerticalGridFragment.this.getActivity().startActivity(intent);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        private ItemViewSelectedListener() {
        }

        public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
            int indexOf;
            if ((obj instanceof VideoItem) && (indexOf = VerticalGridFragment.this.mRowsAdapter.indexOf(obj)) != -1 && VerticalGridFragment.this.mRowsAdapter.size() - 5 <= indexOf) {
                VerticalGridFragment.this.getVideoListHandler.sendMessage(VerticalGridFragment.this.getVideoListHandler.obtainMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public void loadLists(List<VideoItem> list) {
        if (list != null && list.size() > 0) {
            if (this.curPage <= 0) {
                setList(list);
                this.mRowsAdapter.addAll(0, list);
            } else {
                List<VideoItem> list2 = getList();
                for (int i = 0; i < list.size(); i++) {
                    list2.add(list.get(i));
                }
                this.mRowsAdapter.addAll(list2.size() - list.size(), list);
            }
        }
        setAdapter(this.mRowsAdapter);
    }

    private void setList(List<VideoItem> list) {
        this.mList = list;
    }

    public List<VideoItem> getList() {
        return this.mList;
    }

    /* access modifiers changed from: private */
    public void getVideoList(String str, String str2, String str3) {
        WaitingDialog.showWaitingDialog(getActivity());
        ApiProvider apiProvider2 = this.apiProvider;
        ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getContentList(str, str2, str3).enqueue(new Callback<List<VideoItem>>() {
            public void onResponse(Call<List<VideoItem>> call, Response<List<VideoItem>> response) {
                WaitingDialog.cancelWaitingDialog();
                List body = response.body();
                if (body != null && body.size() > 0) {
                    VerticalGridFragment.this.loadLists(body);
                }
            }

            public void onFailure(Call<List<VideoItem>> call, Throwable th) {
                WaitingDialog.cancelWaitingDialog();
                LogUtil.e("onFailure : " + th.toString());
            }
        });
    }
}
