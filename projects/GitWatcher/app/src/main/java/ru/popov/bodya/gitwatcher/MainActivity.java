package ru.popov.bodya.gitwatcher;

import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddNewUserFragment.OnButtonClickListener {

    private static final String ADD_USER_TAG = "ADD_FRAGMENT";

    private DrawerLayout drawerLayout;
    private EditText userNameEditText;
    private TextInputLayout loginLayout;

    private Fragment addNewUserFragment;
    private Fragment userListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userNameEditText = (EditText) findViewById(R.id.edit_text_username);
        loginLayout = (TextInputLayout) findViewById(R.id.login_layout);

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        GitWatcherApplication application = (GitWatcherApplication) getApplication();

        switch (id) {
            case R.id.add_user: {
                addNewUserFragment = AddNewUserFragment.newInstance(application.getUserStorage());
                userListFragment = UserListFragment.newInstance(application.getUserStorage());

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content_main, addNewUserFragment, ADD_USER_TAG);
                transaction.add(R.id.content_main, userListFragment);
                transaction.commit();
                break;
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(addNewUserFragment);
        transaction.remove(userListFragment);
        transaction.commit();
    }
}
