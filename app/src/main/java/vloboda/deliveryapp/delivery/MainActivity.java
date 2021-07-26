package vloboda.deliveryapp.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment selectedFragment = null;

            switch(item.getItemId()){
                case R.id.nav_map:
                    selectedFragment = new MapFragment();
                    break;
                case R.id.nav_orders:
                    selectedFragment = new OrdersFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.addMenu:
                startActivity(new Intent(getApplicationContext(),AddLocation.class));
                finish();
                return true;
            case R.id.logOutMenu:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                return true;
            case R.id.deleteAccount:

                FirebaseFirestore.getInstance().collection("users")
                        .document(FirebaseAuth.getInstance().getUid().toString()).delete();
                FirebaseAuth.getInstance().getCurrentUser().delete();

                Toast.makeText(MainActivity.this, "Succesfully deleted your account" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Login.class));

                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }


}

