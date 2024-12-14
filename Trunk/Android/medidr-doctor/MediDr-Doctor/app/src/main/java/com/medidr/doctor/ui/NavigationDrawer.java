package com.medidr.doctor.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medidr.doctor.R;

import java.util.ArrayList;

public class NavigationDrawer extends AppCompatActivity {

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        mNavItems.add(new NavItem("Edit Profile", R.drawable.time_picker));
        mNavItems.add(new NavItem("Edit Schedule", R.drawable.time_picker));
        mNavItems.add(new NavItem("Report Request",  R.drawable.time_picker));
        mNavItems.add(new NavItem("About Us",  R.drawable.time_picker));
        mNavItems.add(new NavItem("Suggestions",  R.drawable.time_picker));
        mNavItems.add(new NavItem("FAQ",  R.drawable.time_picker));
        mNavItems.add(new NavItem("Sign Out",  R.drawable.time_picker));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

    }


    private void selectItemFromDrawer(int position) {

       switch(position){

           case 1:
                 Intent intent = new Intent(this,RegisterActivity.class);
                 startActivity(intent);
                 finish();
                   break;
           case 2:
               Intent intent_two = new Intent(this,WeeklySchedule.class);
               startActivity(intent_two);
               finish();

               break;
           case 3:
               Intent intent_three = new Intent(this,ReportRequest.class);
               startActivity(intent_three);
               finish();

               break;
           case 4:
               Intent intent_four = new Intent(this,SuggestionActivity.class);
               startActivity(intent_four);
               finish();

               break;
           case 5:
               Intent intent_five = new Intent(this,RegisterActivity.class);
               startActivity(intent_five);
               finish();

               break;
           case 6:

               Intent intent_six = new Intent(this,RegisterActivity.class);
               startActivity(intent_six);
               finish();
               break;
           case 7:
               Intent intent_seven = new Intent(this,RegisterActivity.class);
               startActivity(intent_seven);
               finish();

               break;

               default:
                   break;


       }


        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
    class NavItem {
        String mTitle;

        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
       //     mSubtitle = subtitle;
            mIcon = icon;
        }
    }


    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);

            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );

            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }

}
