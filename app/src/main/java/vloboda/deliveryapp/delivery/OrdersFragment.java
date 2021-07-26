package vloboda.deliveryapp.delivery;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    OrderAdapter myAdapter;
    ArrayList<Order> orderArrayList;

    FirebaseAuth fAuth;
    String userID;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rw_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        fStore = FirebaseFirestore.getInstance();
        fAuth     = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid().toString();


        orderArrayList = new ArrayList<Order>();
       // orderArrayList.add(new Order("String name", "String phone", "Sjenjak 39, Osijek", "String note", 1));
       // orderArrayList.add(new Order("String name", "String phone", "Vijenac petrove gore 2, Osijek", "String note", 0));



        myAdapter = new OrderAdapter(getContext(), orderArrayList);

        recyclerView.setAdapter(myAdapter);

        return view;
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



}
