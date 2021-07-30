package vloboda.deliveryapp.delivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersTempFragmentSubstitute extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    OrderAdapter myAdapter;
    ArrayList<Order> orderArrayList;

    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_temp_fragment_substitute);


        recyclerView = (RecyclerView) findViewById(R.id.rw_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fStore = FirebaseFirestore.getInstance();
        fAuth     = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid().toString();


        orderArrayList = new ArrayList<Order>();

        //orderArrayList.add(new Order("String name", "String phone", "Sjenjak 39, Osijek", "String note", 1));
        //orderArrayList.add(new Order("String name", "String phone", "Vijenac petrove gore 2, Osijek", "String note", 0));



        myAdapter = new OrderAdapter(this, orderArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();
    }


    private void EventChangeListener()
    {
        fStore.collection("users").document(userID).collection("orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error",error.getMessage());
                        }

                        for(DocumentChange fStore : value.getDocumentChanges()){

                            if(fStore.getType() == DocumentChange.Type.ADDED){
                                Order order =  fStore.getDocument().toObject(Order.class);

                                order.setOrderID(fStore.getDocument().getId());
                                orderArrayList.add(order);

                            }
                            myAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }


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
            case R.id.menu_orders:
                startActivity(new Intent(getApplicationContext(), OrdersTempFragmentSubstitute.class));
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

                Toast.makeText(OrdersTempFragmentSubstitute.this, "Succesfully deleted your account" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Login.class));

                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }


}