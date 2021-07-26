package vloboda.deliveryapp.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddLocation extends AppCompatActivity {

    EditText mName, mPhone, mAddress, mNote;
    CheckBox mTime;
    Button mSubmit;
    String userID;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone_number);
        mAddress = findViewById(R.id.et_location);
        mNote = findViewById(R.id.et_note);
        mTime = findViewById(R.id.cb_time);
        mSubmit = findViewById(R.id.btn_add_location);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getText().toString();
                String phone = mPhone.getText().toString();
                String address = mAddress.getText().toString();
                String note = mNote.getText().toString();


                int time;
                if(mTime.isChecked()){
                    time = 1;
                }else{time = 0;}

                DocumentReference documentReference = fStore.collection("users").document(userID).collection("orders").document();

                HashMap<String,Object> order = new HashMap<>();
                order.put("name", name);
                order.put("phone",phone);
                order.put("address",address);
                order.put("note",note);
                order.put("time",time);

                documentReference.set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddLocation.this, "Order added!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onSuccess : Order added to fireStore");
                        mName.setText("");
                        mPhone.setText("");
                        mAddress.setText("");
                        mNote.setText("");
                        mTime.setChecked(false);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("TAG", "OnFailure: failed to add order" + e.toString());
                    }
                });


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.addMenuMap:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

}