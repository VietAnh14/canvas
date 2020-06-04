package vianh.nva.canvas;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InfinityLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int LOADING_ITEM = 0;
    private static final int VIEW_ITEM = 1;
    private List<String> items;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private OnLoadMore onLoadMore;

    public InfinityLoadAdapter(RecyclerView recyclerView, List<String> items) {
        this.items = items;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                lastVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                Log.d("data ", "totalItemCount: " + totalItemCount + " lastVisibleItem " + lastVisibleItem);
                if(!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (onLoadMore != null)
                        onLoadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == LOADING_ITEM) {
            View itemView = layoutInflater.inflate(R.layout.rc_loading_item, parent, false);
            return new LoadingViewHolder(itemView);
        } else {
            View itemView = layoutInflater.inflate(R.layout.recycler_item, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? LOADING_ITEM : VIEW_ITEM;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadingProgressBar);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void addAll(List<String> newItems) {
        int index = items.size();
        items.addAll(newItems);
        notifyItemRangeInserted(index, index + newItems.size() -1);
    }

    public void removeLast() {
        if (!items.isEmpty()) {
            int index = items.size();
            items.remove(items.size() - 1);
        }
    }

    public void addLast(String item) {
        int index = items.size();
        items.add(item);
        notifyItemInserted(index);
    }


    public interface OnLoadMore {
        void onLoadMore();
    }

    public void setOnLoadMore (OnLoadMore loadMoreCallback) {
        this.onLoadMore = loadMoreCallback;
    }

    public void setLoaded() {
        isLoading = false;
    }
}
