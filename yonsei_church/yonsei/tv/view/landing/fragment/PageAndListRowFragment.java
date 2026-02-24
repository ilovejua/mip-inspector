package yonsei_church.yonsei.tv.view.landing.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.api.ApiProvider;
import yonsei_church.yonsei.tv.api.ITvAPI;
import yonsei_church.yonsei.tv.api.model.HomeModel;
import yonsei_church.yonsei.tv.api.model.IconHeaderItem;
import yonsei_church.yonsei.tv.api.model.SearchSubMenuModel;
import yonsei_church.yonsei.tv.api.vo.BannerItem;
import yonsei_church.yonsei.tv.api.vo.LiveUrlItem;
import yonsei_church.yonsei.tv.api.vo.SubMenuItem;
import yonsei_church.yonsei.tv.api.vo.VideoItem;
import yonsei_church.yonsei.tv.presenter.IconHeaderItemPresenter;
import yonsei_church.yonsei.tv.presenter.ImageCardPresenter;
import yonsei_church.yonsei.tv.presenter.TextCardPresenter;
import yonsei_church.yonsei.tv.presenter.VideoCardPresenter;
import yonsei_church.yonsei.tv.utils.LogUtil;
import yonsei_church.yonsei.tv.utils.PreferenceManager;
import yonsei_church.yonsei.tv.utils.WaitingDialog;
import yonsei_church.yonsei.tv.view.landing.activity.VerticalGridActivity;
import yonsei_church.yonsei.tv.view.playback.activity.PlaybackActivity;
import yonsei_church.yonsei.tv.view.search.activity.SearchActivity;
import yonsei_church.yonsei.tv.view.webview.LiveWebInterface;

public class PageAndListRowFragment extends BrowseFragment {
    private static final long HEADER_ID_1 = 1;
    private static final long HEADER_ID_2 = 2;
    private static final long HEADER_ID_3 = 3;
    private static final long HEADER_ID_4 = 4;
    private static final long HEADER_ID_5 = 5;
    private static final String HEADER_NAME_1 = "홈";
    private static final String HEADER_NAME_2 = "탐색하기";
    private static final String HEADER_NAME_3 = "기도음악";
    private static final String HEADER_NAME_4 = "라이브생방송";
    private static final String HEADER_NAME_5 = "설정";
    /* access modifiers changed from: private */
    public static ApiProvider apiProvider;
    private static BackgroundManager mBackgroundManager;
    /* access modifiers changed from: private */
    public static PreferenceManager mPref;
    private static ArrayObjectAdapter mRowsAdapter;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setupUi();
        loadData();
        mPref = PreferenceManager.getInstance(getActivity());
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        getMainFragmentRegistry().registerFragment(PageRow.class, new PageRowFragmentFactory(mBackgroundManager));
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onStop() {
        mBackgroundManager.release();
        super.onStop();
    }

    private void setupUi() {
        setHeadersState(1);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.tv_logo_white_small));
        setHeaderPresenterSelector(new PresenterSelector() {
            public Presenter getPresenter(Object obj) {
                return new IconHeaderItemPresenter();
            }
        });
        setOnSearchClickedListener(new View.OnClickListener() {
            public void onClick(View view) {
                PageAndListRowFragment.this.startActivity(new Intent(PageAndListRowFragment.this.getActivity(), SearchActivity.class));
            }
        });
        prepareEntranceTransition();
    }

    private void loadData() {
        mRowsAdapter = new ArrayObjectAdapter((Presenter) new ListRowPresenter());
        setAdapter(mRowsAdapter);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                PageAndListRowFragment.this.createRows();
                PageAndListRowFragment.this.startEntranceTransition();
            }
        }, 2000);
    }

    /* access modifiers changed from: private */
    public void createRows() {
        mRowsAdapter.add(new PageRow(new IconHeaderItem(1, HEADER_NAME_1, R.drawable.baseline_home_white_48dp)));
        mRowsAdapter.add(new PageRow(new IconHeaderItem(2, HEADER_NAME_2, R.drawable.baseline_apps_white_48dp)));
        mRowsAdapter.add(new PageRow(new IconHeaderItem(3, HEADER_NAME_3, R.drawable.baseline_music_note_white_48dp)));
        mRowsAdapter.add(new PageRow(new IconHeaderItem(4, HEADER_NAME_4, R.drawable.baseline_videocam_white_48dp)));
        mRowsAdapter.add(new PageRow(new IconHeaderItem(5, HEADER_NAME_5, R.drawable.baseline_settings_white_48dp)));
    }

    private static class PageRowFragmentFactory extends BrowseFragment.FragmentFactory {
        private final BackgroundManager mBackgroundManager;

        PageRowFragmentFactory(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        public Fragment createFragment(Object obj) {
            Row row = (Row) obj;
            this.mBackgroundManager.setDrawable((Drawable) null);
            if (row.getHeaderItem().getId() == 1) {
                return new HomeFragment();
            }
            if (row.getHeaderItem().getId() == 2) {
                return new SearchSubMenuFragment();
            }
            if (row.getHeaderItem().getId() == 3) {
                return new PrayerMusicFragment();
            }
            if (row.getHeaderItem().getId() == 4) {
                return new LiveStreamingFragment();
            }
            if (row.getHeaderItem().getId() == 5) {
                return new SettingFragment();
            }
            throw new IllegalArgumentException(String.format("Invalid row %s", new Object[]{obj}));
        }
    }

    public static class HomeFragment extends RowsFragment {
        private int curPage;
        private ArrayObjectAdapter mCategoryListRowAdapter;
        private String mKey;
        private String mPageNumber = "1";
        private ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter((Presenter) new ListRowPresenter());

        public HomeFragment() {
            setAdapter(this.mRowsAdapter);
            PreferenceManager unused = PageAndListRowFragment.mPref = PreferenceManager.getInstance(getActivity());
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                public void onItemClicked(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
                    LogUtil.e("Row : " + row.getHeaderItem());
                    if (obj instanceof VideoItem) {
                        Intent intent = new Intent(HomeFragment.this.getActivity(), PlaybackActivity.class);
                        intent.putExtra("Video", (VideoItem) obj);
                        if (row.getHeaderItem().getName().equals("예배")) {
                            intent.putExtra("play_key", "B139");
                        } else if (row.getHeaderItem().getName().equals("찬양")) {
                            intent.putExtra("play_key", "B140");
                        } else if (row.getHeaderItem().getName().equals("다시보기")) {
                            intent.putExtra("play_key", "B143");
                        }
                        HomeFragment.this.getActivity().startActivity(intent);
                    } else if (obj instanceof BannerItem) {
                        BannerItem bannerItem = (BannerItem) obj;
                        if (bannerItem.getUrl() != null && !bannerItem.getUrl().isEmpty()) {
                            Intent intent2 = new Intent(HomeFragment.this.getActivity(), PlaybackActivity.class);
                            intent2.putExtra("bannerUrl", bannerItem.getUrl());
                            HomeFragment.this.getActivity().startActivity(intent2);
                        }
                    }
                }
            });
            setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
                public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
                }
            });
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            if (PageAndListRowFragment.mPref.getString("userId", "") == null || PageAndListRowFragment.mPref.getString("userId", "").isEmpty()) {
                getHomeList("", "1", 50);
            } else {
                getHomeList(PageAndListRowFragment.mPref.getString("userId", ""), "1", 50);
            }
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        /* access modifiers changed from: private */
        public void loadHomeRows(List<HomeModel> list) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getBannerimg() != null) {
                    ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter((Presenter) new ImageCardPresenter());
                    for (int i2 = 0; i2 < list.get(i).getBannerimg().size(); i2++) {
                        arrayObjectAdapter.add(list.get(i).getBannerimg().get(i2));
                    }
                    if (this.mPageNumber.equals("1")) {
                        this.mRowsAdapter.add(new ListRow(new HeaderItem((long) i, PageAndListRowFragment.HEADER_NAME_1), arrayObjectAdapter));
                    }
                }
                if (list.get(i).getItems() != null) {
                    this.mCategoryListRowAdapter = new ArrayObjectAdapter((Presenter) new VideoCardPresenter());
                    for (int i3 = 0; i3 < list.get(i).getItems().size(); i3++) {
                        for (int i4 = 0; i4 < list.get(i).getItems().get(i3).getVideos().size(); i4++) {
                            this.mCategoryListRowAdapter.add(list.get(i).getItems().get(i3).getVideos().get(i4));
                        }
                    }
                    this.mRowsAdapter.add(new ListRow(new HeaderItem((long) i, list.get(i).getCategory()), this.mCategoryListRowAdapter));
                }
            }
        }

        private void getHomeList(String str, String str2, int i) {
            WaitingDialog.showWaitingDialog(getActivity());
            ApiProvider unused = PageAndListRowFragment.apiProvider;
            ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getHomeList(str, str2, i).enqueue(new Callback<List<HomeModel>>() {
                public void onResponse(Call<List<HomeModel>> call, Response<List<HomeModel>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    List body = response.body();
                    if (body != null && body.size() > 0) {
                        HomeFragment.this.loadHomeRows(body);
                    }
                }

                public void onFailure(Call<List<HomeModel>> call, Throwable th) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }
    }

    public static class SearchSubMenuFragment extends RowsFragment {
        private final ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter((Presenter) new ListRowPresenter());

        public SearchSubMenuFragment() {
            setAdapter(this.mRowsAdapter);
            PreferenceManager unused = PageAndListRowFragment.mPref = PreferenceManager.getInstance(getActivity());
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                public void onItemClicked(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
                    SubMenuItem subMenuItem = (SubMenuItem) obj;
                    Intent intent = new Intent(SearchSubMenuFragment.this.getActivity(), VerticalGridActivity.class);
                    intent.putExtra("category2", subMenuItem.getCategory2());
                    intent.putExtra("key2", subMenuItem.getKey2());
                    SearchSubMenuFragment.this.startActivity(intent);
                }
            });
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            if (PageAndListRowFragment.mPref.getString("userId", "") == null || PageAndListRowFragment.mPref.getString("userId", "").isEmpty()) {
                getSubMenuList("");
            } else {
                getSubMenuList(PageAndListRowFragment.mPref.getString("userId", ""));
            }
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        /* access modifiers changed from: private */
        public void loadSearchSubRows(List<SearchSubMenuModel> list) {
            TextCardPresenter textCardPresenter = new TextCardPresenter();
            for (int i = 0; i < list.size(); i++) {
                ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter((Presenter) textCardPresenter);
                if (list.get(i).getItems() != null) {
                    for (int i2 = 0; i2 < list.get(i).getItems().size(); i2++) {
                        arrayObjectAdapter.add(list.get(i).getItems().get(i2));
                    }
                    this.mRowsAdapter.add(new ListRow(new HeaderItem((long) i, list.get(i).getCategory()), arrayObjectAdapter));
                }
            }
        }

        private void getSubMenuList(String str) {
            WaitingDialog.showWaitingDialog(getActivity());
            ApiProvider unused = PageAndListRowFragment.apiProvider;
            ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getSearchSubMenuList(str).enqueue(new Callback<List<SearchSubMenuModel>>() {
                public void onResponse(Call<List<SearchSubMenuModel>> call, Response<List<SearchSubMenuModel>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    List body = response.body();
                    if (body != null) {
                        SearchSubMenuFragment.this.loadSearchSubRows(body);
                    }
                }

                public void onFailure(Call<List<SearchSubMenuModel>> call, Throwable th) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }
    }

    public static class PrayerMusicFragment extends GridFragment {
        private static final int COLUMNS = 5;
        private final int ZOOM_FACTOR = 1;
        /* access modifiers changed from: private */
        public int curPage;
        final Handler getPrayerMusicListHandler = new Handler() {
            public void handleMessage(Message message) {
                int unused = PrayerMusicFragment.this.curPage = Integer.parseInt(PrayerMusicFragment.this.mPageNumber);
                PrayerMusicFragment.access$608(PrayerMusicFragment.this);
                if (PageAndListRowFragment.mPref.getString("userId", "") == null || PageAndListRowFragment.mPref.getString("userId", "").isEmpty()) {
                    PrayerMusicFragment.this.getPrayerMusicList("", String.valueOf(PrayerMusicFragment.this.curPage), "P000");
                } else {
                    PrayerMusicFragment.this.getPrayerMusicList(PageAndListRowFragment.mPref.getString("userId", ""), String.valueOf(PrayerMusicFragment.this.curPage), "P000");
                }
                String unused2 = PrayerMusicFragment.this.mPageNumber = String.valueOf(PrayerMusicFragment.this.curPage);
            }
        };
        private List<VideoItem> mList;
        /* access modifiers changed from: private */
        public String mPageNumber = "1";
        /* access modifiers changed from: private */
        public ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter((Presenter) new ListRowPresenter());

        static /* synthetic */ int access$608(PrayerMusicFragment prayerMusicFragment) {
            int i = prayerMusicFragment.curPage;
            prayerMusicFragment.curPage = i + 1;
            return i;
        }

        public PrayerMusicFragment() {
            PreferenceManager unused = PageAndListRowFragment.mPref = PreferenceManager.getInstance(getActivity());
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setupAdapter();
            if (PageAndListRowFragment.mPref.getString("userId", "") == null || PageAndListRowFragment.mPref.getString("userId", "").isEmpty()) {
                getPrayerMusicList("", this.mPageNumber, "P000");
            } else {
                getPrayerMusicList(PageAndListRowFragment.mPref.getString("userId", ""), this.mPageNumber, "P000");
            }
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        private void setupAdapter() {
            VerticalGridPresenter verticalGridPresenter = new VerticalGridPresenter(1);
            verticalGridPresenter.setNumberOfColumns(5);
            setGridPresenter(verticalGridPresenter);
            this.mRowsAdapter = new ArrayObjectAdapter((Presenter) new VideoCardPresenter());
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                public void onItemClicked(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
                    if (obj instanceof VideoItem) {
                        Intent intent = new Intent(PrayerMusicFragment.this.getActivity(), PlaybackActivity.class);
                        intent.putExtra("Video", (VideoItem) obj);
                        intent.putExtra("play_key", "P000");
                        PrayerMusicFragment.this.getActivity().startActivity(intent);
                    }
                }
            });
            setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
                public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
                    int indexOf;
                    if ((obj instanceof VideoItem) && (indexOf = PrayerMusicFragment.this.mRowsAdapter.indexOf(obj)) != -1 && PrayerMusicFragment.this.mRowsAdapter.size() - 1 == indexOf) {
                        PrayerMusicFragment.this.getPrayerMusicListHandler.sendMessage(PrayerMusicFragment.this.getPrayerMusicListHandler.obtainMessage());
                    }
                }
            });
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
        public void getPrayerMusicList(String str, String str2, String str3) {
            WaitingDialog.showWaitingDialog(getActivity());
            ApiProvider unused = PageAndListRowFragment.apiProvider;
            ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getContentList(str, str2, str3).enqueue(new Callback<List<VideoItem>>() {
                public void onResponse(Call<List<VideoItem>> call, Response<List<VideoItem>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    List body = response.body();
                    if (body != null && body.size() > 0) {
                        PrayerMusicFragment.this.loadLists(body);
                    }
                }

                public void onFailure(Call<List<VideoItem>> call, Throwable th) {
                    WaitingDialog.cancelWaitingDialog();
                    LogUtil.e("onFailure : " + th.toString());
                }
            });
        }
    }

    public static class LiveStreamingFragment extends Fragment implements BrowseFragment.MainFragmentAdapterProvider {
        private BrowseFragment.MainFragmentAdapter mMainFragmentAdapter = new BrowseFragment.MainFragmentAdapter(this);

        public BrowseFragment.MainFragmentAdapter getMainFragmentAdapter() {
            return this.mMainFragmentAdapter;
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            PreferenceManager unused = PageAndListRowFragment.mPref = PreferenceManager.getInstance(getActivity());
            getMainFragmentAdapter().getFragmentHost().showTitleView(false);
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            FrameLayout frameLayout = new FrameLayout(getActivity());
            new FrameLayout.LayoutParams(-1, -1);
            if (PageAndListRowFragment.mPref.getString("userId", "") == null || PageAndListRowFragment.mPref.getString("userId", "").isEmpty()) {
                getLiveUrl("");
            } else {
                getLiveUrl(PageAndListRowFragment.mPref.getString("userId", ""));
            }
            return frameLayout;
        }

        public void onResume() {
            super.onResume();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        private void getLiveUrl(String str) {
            WaitingDialog.showWaitingDialog(getActivity());
            ApiProvider unused = PageAndListRowFragment.apiProvider;
            ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getLiveUrl(str).enqueue(new Callback<LiveUrlItem>() {
                public void onResponse(Call<LiveUrlItem> call, Response<LiveUrlItem> response) {
                    WaitingDialog.cancelWaitingDialog();
                    LiveUrlItem body = response.body();
                    if (body.getLiveflag().equals("Y")) {
                        if (body.getUrl() != null && !body.getUrl().isEmpty()) {
                            Intent intent = new Intent(LiveStreamingFragment.this.getActivity(), PlaybackActivity.class);
                            intent.putExtra("liveUrl", body.getUrl());
                            intent.putExtra("liveTime", body.getFinishTime());
                            LiveStreamingFragment.this.getActivity().startActivity(intent);
                        }
                    } else if (body.getLiveflag().equals("N") && body.getLivebr() != null && !body.getLivebr().isEmpty()) {
                        Toast.makeText(LiveStreamingFragment.this.getActivity(), body.getLivebr().toString(), 1).show();
                    }
                }

                public void onFailure(Call<LiveUrlItem> call, Throwable th) {
                    WaitingDialog.cancelWaitingDialog();
                    LogUtil.e("onFailure : " + th.toString());
                }
            });
        }
    }

    public static class SettingFragment extends Fragment implements BrowseFragment.MainFragmentAdapterProvider, LiveWebInterface.OnJavascriptListener {
        private BrowseFragment.MainFragmentAdapter mMainFragmentAdapter = new BrowseFragment.MainFragmentAdapter(this);
        private WebView mWebview;

        public BrowseFragment.MainFragmentAdapter getMainFragmentAdapter() {
            return this.mMainFragmentAdapter;
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            PreferenceManager unused = PageAndListRowFragment.mPref = PreferenceManager.getInstance(getActivity());
            getMainFragmentAdapter().getFragmentHost().showTitleView(false);
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            FrameLayout frameLayout = new FrameLayout(getActivity());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.setMarginStart(32);
            this.mWebview = new WebView(getActivity());
            this.mWebview.setWebViewClient(new WebViewClient());
            this.mWebview.getSettings().setJavaScriptEnabled(true);
            this.mWebview.addJavascriptInterface(new LiveWebInterface(this), LiveWebInterface.TAG);
            frameLayout.addView(this.mWebview, layoutParams);
            return frameLayout;
        }

        public void onResume() {
            super.onResume();
            this.mWebview.loadUrl("http://app.yonsei.or.kr/pro/login.php");
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        public void getLiveUrl(String str, String str2) {
            PageAndListRowFragment.mPref.putString("userId", str);
        }
    }
}
