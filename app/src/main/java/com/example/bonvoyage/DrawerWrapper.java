package com.example.bonvoyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class DrawerWrapper {

    // to implement this wrapper, add     <include layout="@layout/menu_toolbar"/>
    // in the xml file linked to the layout of the activity
    // right after the oncreate function add
    //Toolbar toolbar = findViewById(R.id.toolbar);
    // new DrawerWrapper(this,this.getApplicationContext(),toolbar);

    FirebaseUser user;
    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.nav_wallet);
    PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.nav_trips);
    PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.nav_logout);

    DrawerWrapper(Activity activity, Context context, Toolbar toolbar) {
        //create the drawer and remember the `Drawer` result object
        user = FirebaseAuth.getInstance().getCurrentUser();
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withEmail(user.getEmail())
                                .withIcon(activity.getResources().getDrawable(R.drawable.default_profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        Intent changeProfile = new Intent(context,ChangeUserProfile.class);
                        activity.startActivity(changeProfile);
                        return currentProfile;
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
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position) {
                            case 1:
                                Intent walletDisplay = new Intent(context,WalletActivity.class);
                                activity.startActivity(walletDisplay);
                            case 2:
                                Intent tripHistory = new Intent(context,TripHistoryActivity.class);
                                activity.startActivity(tripHistory);
                            case 3:
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                Intent loginPage = new Intent(context, LoginSignupActivity.class);
                                mAuth.signOut();
                                activity.startActivity(loginPage);
                        }
                        return true;
                    }
                })
                .build();


    }
}
