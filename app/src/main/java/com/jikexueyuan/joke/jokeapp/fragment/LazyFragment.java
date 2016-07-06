package com.jikexueyuan.joke.jokeapp.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/7/5.
 */
public abstract class LazyFragment extends Fragment {
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {

    }

    private void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();
}
