package com.jikexueyuan.joke.jokeapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.jikexueyuan.joke.jokeapp.fragment.ChickenSoupFragment;
import com.jikexueyuan.joke.jokeapp.fragment.CoderFragment;
import com.jikexueyuan.joke.jokeapp.fragment.JokeFragment;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments = {new JokeFragment(),new CoderFragment(),new ChickenSoupFragment()};
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
