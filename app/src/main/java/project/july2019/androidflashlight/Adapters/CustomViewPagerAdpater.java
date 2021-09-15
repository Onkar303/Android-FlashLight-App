package project.july2019.androidflashlight.Adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class CustomViewPagerAdpater extends FragmentPagerAdapter {

    private List<Fragment> list;
    private Context context;

    public CustomViewPagerAdpater(FragmentManager fm,List<Fragment> list,Context context)
    {
        super(fm);
        this.list=list;
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
