package com.example.bonvoyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class DrawerWrapper {

    // to implement this wrapper, add     <include layout="@layout/menu_toolbar"/>
    // in the xml file linked to the layout of the activity
    // right after the oncreate function add
    //Toolbar toolbar = findViewById(R.id.toolbar);
    // new DrawerWrapper(this,this.getApplicationContext(),toolbar);


    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.nav_home);
    PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.nav_settings);
    PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.nav_wallet);
    PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.nav_trips);
    PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.nav_logout);

    DrawerWrapper(Activity activity, Context context, Toolbar toolbar) {
        //create the drawer and remember the `Drawer` result object

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Jane Doe")
                                .withEmail("testrider@gmail.com")
                                .withIcon(activity.getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        Intent changeProfile = new Intent();

                        return false;
                    }
                })
                .build();



        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Toast.makeText(context, "Clicked " + position, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .build();


    }
}
