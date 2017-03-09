package umaahi.pravik.constructionnoteplus;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import fragments.MyProfileFragment;
import fragments.SiteFragment;
import fragments.UpdateExpenseFragment;
import fragments.ViewExpenseFragment;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String userId,name,email,img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.addsite);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId=user.getUid();
        name=user.getDisplayName();
        if (user != null) {



            View headerView = navigationView.getHeaderView(0);
            //ImageView drawerImage = (ImageView) headerView.findViewById(imageView);
            TextView drawerUsername = (TextView) headerView.findViewById(R.id.name);
            //TextView drawerAccount = (TextView) headerView.findViewById(R.id.email);
            // drawerImage.setImageDrawable(R.drawable.ic_user);
            drawerUsername.setText("Welcome "+name);


            System.out.println("#####user is not null");
        } else {
            System.out.println("#####user is null");

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object


        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.addsite:
                fragment = new SiteFragment();
                break;
            case R.id.viewexpense:
                fragment = new ViewExpenseFragment();
                break;
            case R.id.updateexpense:
                fragment = new UpdateExpenseFragment();
                break;
            case R.id.myprofile:
                fragment = new MyProfileFragment();
                break;
            case R.id.share:
               // fragment = new
                break;
            case R.id.sendfeedback:
               // fragment = new Menu3();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}
