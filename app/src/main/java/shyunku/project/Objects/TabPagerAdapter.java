package shyunku.project.Objects;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import shyunku.project.Activities.Tab_Views.TabMastery;
import shyunku.project.Activities.Tab_Views.TabRecentPvpList;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                TabRecentPvpList tab1 = new TabRecentPvpList();
                return tab1;
            case 1:
                TabMastery tab2 = new TabMastery();
                return tab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
