package com.example.fileexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fileexplorer.Fragments.CardFragment;
import com.example.fileexplorer.Fragments.HomeFragment;
import com.example.fileexplorer.Fragments.InternalFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private DrawerLayout drawerLayout;

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      Toast.makeText(this, "drawer", Toast.LENGTH_SHORT).show();
      drawerLayout.closeDrawer( GravityCompat.START);
    } else {
      if (getSupportFragmentManager().getBackStackEntryCount() == 0)
        super.onBackPressed ();
    }


    getSupportFragmentManager().popBackStackImmediate();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    drawerLayout = findViewById(R.id.drawer_layout);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.Open_Drawer, R.string.Close_Drawer);
    drawerLayout.addDrawerListener(toggle);//handles animation on Drawer Slide
    toggle.syncState();
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, new HomeFragment())
        .commit();
    navigationView.setCheckedItem(R.id.nav_home);//to make home button checked even  when nothing is clicked
    //        FragmentManager fragmentManager = getSupportFragmentManager();
    //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    //        // Create FragmentOne instance.
    //        FragmentOne fragmentOne = new FragmentOne();
    //        // Add fragment one with tag name.
    //        fragmentTransaction.add(R.id.fragment_back_stack_frame_layout, fragmentOne, "Fragment
    // One");
    //        fragmentTransaction.commit();

  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.nav_home:
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null).commit ();
        break;
      case R.id.nav_internal:
        InternalFragment internalFragment = new InternalFragment();
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, internalFragment)
            .addToBackStack(null)
            .commit();
        break;
      case R.id.nav_sd:
        CardFragment cardFragment = new CardFragment();
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, cardFragment)
            .addToBackStack(null)
            .commit();
        break;
    }
    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }
}
