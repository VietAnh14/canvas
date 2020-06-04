package vianh.nva.canvas;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.PageViewHolder> {
    private List<String> listUrl;

    public MangaAdapter(List<String> listUrl) {
        this.listUrl = listUrl;
    }
    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.page_viewer, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.onBind(listUrl.get(position));
    }

    @Override
    public int getItemCount() {
        return listUrl == null ? 0 : listUrl.size();
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        SubsamplingScaleImageView page;
        PageViewHolder(@NonNull View itemView) {
            super(itemView);
            page = itemView.findViewById(R.id.page);
        }

        void onBind(String url) {
            Glide.with(page.getContext())
                    .download(new GlideUrl(url))
                    .into(new CustomViewTarget<SubsamplingScaleImageView, File>(page) {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {

                        }

                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            view.setImage(ImageSource.uri(Uri.fromFile(resource)));
                        }

                        @Override
                        protected void onResourceCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }
}