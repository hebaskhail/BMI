package com.hebaskhail.bmi.Adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaginationRecyclerView {
    int pastVisiblesItems = 0;
    int visibleItemCount = 0;
    int totalItemCount = 0;
    int page = 0;
    Context context;
    boolean loading = true;
    RecyclerView recyclerView;
    int currentCount;
    int firstCount;
    int pageNow;
    int total_pages;
    OnLastItem onLastItem;

    public PaginationRecyclerView(RecyclerView recyclerView, int currentCount, int firstCount, int pageNow, int total_pages, OnLastItem onLastItem) {
        this.recyclerView = recyclerView;
        this.currentCount = currentCount;
        this.firstCount = firstCount;
        this.pageNow = pageNow;
        this.total_pages = total_pages;
        this.onLastItem = onLastItem;
        page = pageNow;
        recyclerView.getAdapter().notifyItemRangeChanged(
                firstCount, currentCount
        );
        if (total_pages > pageNow) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) //check for scroll down
                    {
                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                        if (loading) {
                            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                                loading = false;
                                page += 1;
                                onLastItem.lastItem(page, pastVisiblesItems);
                                Log.v("...", "Last Item Wow !");
                            }
                        }
                    }
                }
            });
        }
    }

    public static PaginationRecyclerView getInstance(RecyclerView recyclerView, int currentCount, int firstCount, int pageNow, int total_pages, OnLastItem onLastItem) {
        return new PaginationRecyclerView(
                recyclerView,
                currentCount,
                firstCount,
                pageNow,
                total_pages,
                onLastItem
        );
    }

}

