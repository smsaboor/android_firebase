//package com.example.flutter_projects.main;
//
//import android.app.Activity;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.flutter_projects.R;
//import com.example.flutter_projects.fragments.HomeFragment;
//import com.example.flutter_projects.fragments.LibraryFragment;
//import com.example.flutter_projects.fragments.ShortsFragment;
//import com.example.flutter_projects.fragments.SubscriptionFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//
//public class FunctionsMainActivity {
//    BottomNavigationView bottomNavigationView;
//
//    public void bottomNavSetup(BottomNavigationView bottomNavigationView){
//        this.bottomNavigationView=bottomNavigationView;
//        this.bottomNavigationView.setBackground(null);
//        this.bottomNavigationView.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.homee) replaceFragment(new HomeFragment());
//            else if (item.getItemId() == R.id.shorts) replaceFragment(new ShortsFragment());
//            else if (item.getItemId() == R.id.subscriptions)
//                replaceFragment(new SubscriptionFragment());
//            else if (item.getItemId() == R.id.library) replaceFragment(new LibraryFragment());
//            else replaceFragment(new HomeFragment());
//            return true;
//        });
//    }
//    private void replaceFragment(Activity activity, Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }
//
//}
