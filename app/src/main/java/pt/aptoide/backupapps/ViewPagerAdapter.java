package pt.aptoide.backupapps;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class ViewPagerAdapter extends PagerAdapter{

    ArrayList<View> views;
    private CharSequence[] titles = new String[]{"Backup", "Restore"};

    public ViewPagerAdapter(Context context,ArrayList<View> views ){

        this.views = views;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), position);

        return views.get(position);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return views.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
