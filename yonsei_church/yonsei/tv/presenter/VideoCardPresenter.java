package yonsei_church.yonsei.tv.presenter;

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.api.vo.VideoItem;

public class VideoCardPresenter extends Presenter {
    private static final int CARD_HEIGHT = 176;
    private static final int CARD_WIDTH = 313;
    private static int sDefaultBackgroundColor;
    private static int sSelectedBackgroundColor;
    private Drawable mDefaultCardImage;

    /* access modifiers changed from: private */
    public static void updateCardBackgroundColor(ImageCardView imageCardView, boolean z) {
        int i = z ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        imageCardView.setBackgroundColor(i);
        imageCardView.findViewById(R.id.info_field).setBackgroundColor(i);
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        sDefaultBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.selected_background);
        this.mDefaultCardImage = ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.movie);
        AnonymousClass1 r0 = new ImageCardView(viewGroup.getContext()) {
            public void setSelected(boolean z) {
                VideoCardPresenter.updateCardBackgroundColor(this, z);
                super.setSelected(z);
            }
        };
        r0.setFocusable(true);
        r0.setFocusableInTouchMode(true);
        updateCardBackgroundColor(r0, false);
        return new Presenter.ViewHolder(r0);
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        VideoItem videoItem = (VideoItem) obj;
        ImageCardView imageCardView = (ImageCardView) viewHolder.view;
        if (videoItem.getThumbnail() != null) {
            imageCardView.setTitleText(videoItem.getTitle());
            imageCardView.setContentText(videoItem.getDate());
            imageCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            Glide.with(viewHolder.view.getContext()).asBitmap().load(videoItem.getThumbnail()).apply(new RequestOptions().centerCrop().error(this.mDefaultCardImage)).into(imageCardView.getMainImageView());
        }
    }

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ImageCardView imageCardView = (ImageCardView) viewHolder.view;
        imageCardView.setBadgeImage((Drawable) null);
        imageCardView.setMainImage((Drawable) null);
    }
}
