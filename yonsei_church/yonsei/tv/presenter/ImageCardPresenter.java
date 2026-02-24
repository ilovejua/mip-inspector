package yonsei_church.yonsei.tv.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import yonsei_church.yonsei.tv.R;
import yonsei_church.yonsei.tv.api.vo.BannerItem;
import yonsei_church.yonsei.tv.view.card.BannerCardView;

public class ImageCardPresenter extends Presenter {
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        BannerCardView bannerCardView = new BannerCardView(viewGroup.getContext());
        bannerCardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        bannerCardView.setFocusable(true);
        bannerCardView.setFocusableInTouchMode(true);
        return new Presenter.ViewHolder(bannerCardView);
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        Glide.with(viewHolder.view.getContext()).asBitmap().load(((BannerItem) obj).getThumbnail()).apply(new RequestOptions().centerCrop()).into((ImageView) viewHolder.view.findViewById(R.id.extra_image));
    }
}
