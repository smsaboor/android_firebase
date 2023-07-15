package com.example.flutter_projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.flutter_projects.firebase.FirebaseHelper;
import com.example.flutter_projects.fragments.AboutFragment;
import com.example.flutter_projects.fragments.HomeFragment;
import com.example.flutter_projects.fragments.LibraryFragment;
import com.example.flutter_projects.fragments.SettingsFragment;
import com.example.flutter_projects.fragments.ShortsFragment;
import com.example.flutter_projects.fragments.SubscriptionFragment;
import com.example.flutter_projects.login.DemoActivity;
import com.example.flutter_projects.login.LoginActivity;
import com.example.flutter_projects.login.LoginActivityEmail;
import com.example.flutter_projects.login.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

// https://www.youtube.com/watch?v=ahNruIZX130
public class MainActivity extends AppCompatActivity {


    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    loadFragment(new HomeFragment());
                    Toast.makeText(MainActivity.this, "Logout fail", Toast.LENGTH_SHORT).show();
                    navigationView.setCheckedItem(R.id.nav_home);
                } else if (id == R.id.nav_settings) {
                    loadFragment(new SettingsFragment());
//                    navigationView.setCheckedItem(R.id.nav_settings);
                } else if (id == R.id.nav_share) {
                    loadFragment(new SettingsFragment());
//                    navigationView.setCheckedItem(R.id.nav_share);
                } else if (id == R.id.nav_about) {
                    loadFragment(new AboutFragment());
//                    navigationView.setCheckedItem(R.id.nav_about);
                } else if (id == R.id.nav_logout) {
                    if (FirebaseHelper.logout() == 1) {
                        //User is Logged in
                        Toast.makeText(MainActivity.this, "Logout Successful !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivityEmail.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Logout fail", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loadFragment(new HomeFragment());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homee) replaceFragment(new HomeFragment());
            else if (item.getItemId() == R.id.shorts) replaceFragment(new ShortsFragment());
            else if (item.getItemId() == R.id.subscriptions)
                replaceFragment(new SubscriptionFragment());
            else if (item.getItemId() == R.id.profile) {
                Intent intent=new Intent(this, ProfileActivity.class);
                startActivity(intent);
            }
            else replaceFragment(new HomeFragment());
            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Upload a Video is clicked", Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Create a short is Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Go live is Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    public void loadFragment(Fragment fragment) {
        Toast.makeText(MainActivity.this, "Logout fail", Toast.LENGTH_SHORT).show();
        Log.e("loadFragment", "loading");
        // Managing Data Passing to fragment
        // we can use if condition for different argument to different frgments
//        Bundle bundle=new Bundle();
//        bundle.putString("arg1","Saboor");
//        bundle.putInt("arg2",3);

        Intent intent=new Intent(this, DemoActivity.class);
        startActivity(intent);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
    }


    public void callFromFragment() {
        Log.d("Call from fragment", "Name is:" + ",Roll No is:");

    }
}
