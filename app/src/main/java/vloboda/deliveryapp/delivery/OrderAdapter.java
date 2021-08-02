package vloboda.deliveryapp.delivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
/*
    Context context;

    public void setList(ArrayList<Order> list) {
        this.list = list;
    }

    ArrayList<Order> list;

    public OrderAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    public OrderAdapter(ArrayList<Order> dataholder){
        this.list = dataholder;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_card, parent, false);
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_card, parent, false));
       }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderAdapter.MyViewHolder holder, int position) {
        Order order = list.get(position);

        holder.name.setText(order.name);
        holder.phone.setText(order.phone);
        holder.address.setText(order.address);
        holder.note.setText(order.note);

        if(order.time == 1){
            holder.time.setText("after 4PM");
        }else{
            holder.time.setText("before 4PM");
        }
    }

    @Override
    public int getItemCount() {return list.size();}

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        EditText name, phone, address, note;
        CheckBox time;
        Order order;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            phone = itemView.findViewById(R.id.tvPhone);
            address = itemView.findViewById(R.id.tvAddress);
            note = itemView.findViewById(R.id.tvNote);

            time = itemView.findViewById(R.id.tvTime);

            itemView.findViewById(R.id.RW_deletebtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Do you want to delete this tarantula?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DELETE FROM FIRESTORE AND FIRESTORAGE

                            FirebaseFirestore.getInstance().collection("users").document(order.getOrderID()).delete();

                            itemView.getContext().startActivity(new Intent(itemView.getContext(), MainActivity.class));
                            //startActivity(new Intent(getApplicationContext(),Tarantulas.class));
                            Toast.makeText(itemView.getContext(), "Succesfully deleted order" , Toast.LENGTH_SHORT).show();

                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            });

        }
    }

    public void UpdateAdapter(ArrayList<Order> mDataList) {
        this.list = mDataList;
        notifyDataSetChanged();
    }*/

    Context context;
    ArrayList<Order> list;

    public OrderAdapter(ArrayList<Order> list) {
        this.list = list;
    }


    public OrderAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = list.get(position);

        holder.name.setText(order.name);
        holder.phone.setText(order.phone);
        holder.address.setText(order.address);
        holder.note.setText(order.note);

        if (order.time == 1) {
            holder.time.setText("after 4PM");
        } else {
            holder.time.setText("before 4PM");
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        EditText name, phone, address, note;
        CheckBox time;
        Order order;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.tvName);
            phone = itemView.findViewById(R.id.tvPhone);
            address = itemView.findViewById(R.id.tvAddress);
            note = itemView.findViewById(R.id.tvNote);

            time = itemView.findViewById(R.id.tvTime);


/*
            itemView.findViewById(R.id.RW_deletebtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Do you want to delete this tarantula?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DELETE FROM FIRESTORE
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                            FirebaseFirestore.getInstance().collection("orders").document(userID)
                                    .collection("orders").document(order.getOrderID()).delete();


                            itemView.getContext().startActivity(new Intent(itemView.getContext(), MainActivity.class));
                            //startActivity(new Intent(getApplicationContext(),Tarantulas.class));
                            Toast.makeText(itemView.getContext(), "Succesfully deleted tarantula" , Toast.LENGTH_SHORT).show();

                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            });
*/
        }


    }

    public void UpdateAdapter(ArrayList<Order> mDataList) {
        this.list = mDataList;
        notifyDataSetChanged();
    }
}