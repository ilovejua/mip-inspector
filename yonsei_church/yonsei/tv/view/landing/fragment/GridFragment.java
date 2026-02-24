package yonsei_church.yonsei.tv.view.landing.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.transition.TransitionHelper;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnChildLaidOutListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import yonsei_church.yonsei.tv.R;

public class GridFragment extends Fragment implements BrowseFragment.MainFragmentAdapterProvider {
    /* access modifiers changed from: private */
    public static boolean DEBUG = false;
    private static final String TAG = "VerticalGridFragment";
    private ObjectAdapter mAdapter;
    private final OnChildLaidOutListener mChildLaidOutListener = new OnChildLaidOutListener() {
        public void onChildLaidOut(ViewGroup viewGroup, View view, int i, long j) {
            if (i == 0) {
                GridFragment.this.showOrHideTitle();
            }
        }
    };
    private VerticalGridPresenter mGridPresenter;
    /* access modifiers changed from: private */
    public VerticalGridPresenter.ViewHolder mGridViewHolder;
    private BrowseFragment.MainFragmentAdapter mMainFragmentAdapter = new BrowseFragment.MainFragmentAdapter(this) {
        public void setEntranceTransitionState(boolean z) {
            GridFragment.this.setEntranceTransitionState(z);
        }
    };
    private OnItemViewClickedListener mOnItemViewClickedListener;
    /* access modifiers changed from: private */
    public OnItemViewSelectedListener mOnItemViewSelectedListener;
    private Object mSceneAfterEntranceTransition;
    private int mSelectedPosition = -1;
    private final OnItemViewSelectedListener mViewSelectedListener = new OnItemViewSelectedListener() {
        public void onItemSelected(Presenter.ViewHolder viewHolder, Object obj, RowPresenter.ViewHolder viewHolder2, Row row) {
            int selectedPosition = GridFragment.this.mGridViewHolder.getGridView().getSelectedPosition();
            if (GridFragment.DEBUG) {
                Log.v(GridFragment.TAG, "grid selected position " + selectedPosition);
            }
            GridFragment.this.gridOnItemSelected(selectedPosition);
            if (GridFragment.this.mOnItemViewSelectedListener != null) {
                GridFragment.this.mOnItemViewSelectedListener.onItemSelected(viewHolder, obj, viewHolder2, row);
            }
        }
    };

    public void setGridPresenter(VerticalGridPresenter verticalGridPresenter) {
        if (verticalGridPresenter == null) {
            throw new IllegalArgumentException("Grid presenter may not be null");
        }
        this.mGridPresenter = verticalGridPresenter;
        this.mGridPresenter.setOnItemViewSelectedListener(this.mViewSelectedListener);
        if (this.mOnItemViewClickedListener != null) {
            this.mGridPresenter.setOnItemViewClickedListener(this.mOnItemViewClickedListener);
        }
    }

    public VerticalGridPresenter getGridPresenter() {
        return this.mGridPresenter;
    }

    public void setAdapter(ObjectAdapter objectAdapter) {
        this.mAdapter = objectAdapter;
        updateAdapter();
    }

    public ObjectAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener) {
        this.mOnItemViewSelectedListener = onItemViewSelectedListener;
    }

    /* access modifiers changed from: private */
    public void gridOnItemSelected(int i) {
        if (i != this.mSelectedPosition) {
            this.mSelectedPosition = i;
            showOrHideTitle();
        }
    }

    /* access modifiers changed from: private */
    public void showOrHideTitle() {
        if (this.mGridViewHolder.getGridView().findViewHolderForAdapterPosition(this.mSelectedPosition) != null) {
            if (!this.mGridViewHolder.getGridView().hasPreviousViewInSameRow(this.mSelectedPosition)) {
                this.mMainFragmentAdapter.getFragmentHost().showTitleView(true);
            } else {
                this.mMainFragmentAdapter.getFragmentHost().showTitleView(false);
            }
        }
    }

    public void setOnItemViewClickedListener(OnItemViewClickedListener onItemViewClickedListener) {
        this.mOnItemViewClickedListener = onItemViewClickedListener;
        if (this.mGridPresenter != null) {
            this.mGridPresenter.setOnItemViewClickedListener(this.mOnItemViewClickedListener);
        }
    }

    public OnItemViewClickedListener getOnItemViewClickedListener() {
        return this.mOnItemViewClickedListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.grid_fragment, viewGroup, false);
    }

    @SuppressLint({"RestrictedApi"})
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.browse_grid_dock);
        this.mGridViewHolder = this.mGridPresenter.onCreateViewHolder(viewGroup);
        viewGroup.addView(this.mGridViewHolder.view);
        this.mGridViewHolder.getGridView().setOnChildLaidOutListener(this.mChildLaidOutListener);
        this.mSceneAfterEntranceTransition = TransitionHelper.createScene(viewGroup, new Runnable() {
            public void run() {
                GridFragment.this.setEntranceTransitionState(true);
            }
        });
        getMainFragmentAdapter().getFragmentHost().notifyViewCreated(this.mMainFragmentAdapter);
        updateAdapter();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mGridViewHolder = null;
    }

    public BrowseFragment.MainFragmentAdapter getMainFragmentAdapter() {
        return this.mMainFragmentAdapter;
    }

    public void setSelectedPosition(int i) {
        this.mSelectedPosition = i;
        if (this.mGridViewHolder != null && this.mGridViewHolder.getGridView().getAdapter() != null) {
            this.mGridViewHolder.getGridView().setSelectedPositionSmooth(i);
        }
    }

    private void updateAdapter() {
        if (this.mGridViewHolder != null) {
            this.mGridPresenter.onBindViewHolder(this.mGridViewHolder, this.mAdapter);
            if (this.mSelectedPosition != -1) {
                this.mGridViewHolder.getGridView().setSelectedPosition(this.mSelectedPosition);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setEntranceTransitionState(boolean z) {
        this.mGridPresenter.setEntranceTransitionState(this.mGridViewHolder, z);
    }
}
