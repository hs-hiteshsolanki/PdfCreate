package com.g.pdfcreate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList NAME, NUMBER, CREDIT, DEBIT;
    DBHandler dbHandler;
    private int selectedItemPosition = RecyclerView.NO_POSITION;


    public MyAdapter(Context context, ArrayList NAME, ArrayList NUMBER, ArrayList CREDIT, ArrayList DEBIT) {
        this.context = context;
        this.NAME = NAME;
        this.NUMBER = NUMBER;
        this.CREDIT = CREDIT;
        this.DEBIT = DEBIT;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custem_layout, parent, false);
        return new MyViewHolder(v);
    }

    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.NAME.setText(String.valueOf(NAME.get(position)));
        holder.NUMBER.setText(String.valueOf(NUMBER.get(position)));
        holder.CREDIT.setText(String.valueOf(CREDIT.get(position)));
        holder.DEBIT.setText(String.valueOf(DEBIT.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                setSelectedItemPosition(position);

                // Handle item click (you can perform additional actions if needed)
                if (position != RecyclerView.NO_POSITION) {
                    String selectedItem = NAME.get(position).toString();
                    Toast.makeText(context, "Selected item: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_layout);
                EditText edtName = dialog.findViewById(R.id.etName);
                EditText edtNumber = dialog.findViewById(R.id.etNumber);
                EditText edtCredit = dialog.findViewById(R.id.etCredit);
                EditText edtDebit = dialog.findViewById(R.id.etDebit);

                Button edtButton = dialog.findViewById(R.id.updateBtn);

                edtName.setText(String.valueOf(NAME.get(position)));
                edtNumber.setText(String.valueOf(NUMBER.get(position)));
                edtCredit.setText(String.valueOf(CREDIT.get(position)));
                edtDebit.setText(String.valueOf(DEBIT.get(position)));

                edtButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "", number = "", credit = "", debit = "";
                        if (!edtName.getText().toString().equals("") && !edtNumber.getText().toString().equals("") && !edtCredit.getText().toString().equals("") && !edtDebit.getText().toString().equals("")) {
                            name = edtName.getText().toString();
                            number = edtNumber.getText().toString();
                            credit = edtCredit.getText().toString();
                            debit = edtDebit.getText().toString();
                        } else {
                            Toast.makeText(context, "Enter a Data", Toast.LENGTH_SHORT).show();
                        }
                        dbHandler.updateData1(name, number, credit, debit);

                        // Update the corresponding item in the data lists refresh data
                        NAME.set(position, name);
                        NUMBER.set(position, number);
                        CREDIT.set(position, credit);
                        DEBIT.set(position, debit);
                        notifyItemChanged(position);

                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(NAME.get(position));
                boolean isDeleted = dbHandler.delData(name);
                if (isDeleted) {
                    // Remove the item from the data lists refresh data
                    NAME.remove(position);
                    NUMBER.remove(position);
                    CREDIT.remove(position);
                    DEBIT.remove(position);

                    // Notify the adapter about the data change
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());

                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return NAME.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView NAME, NUMBER, CREDIT, DEBIT;
        Button btnUpdate, btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            NAME = itemView.findViewById(R.id.txtCustomerName);
            NUMBER = itemView.findViewById(R.id.txtCustomerNumber);
            CREDIT = itemView.findViewById(R.id.txtCustomerCredit);
            DEBIT = itemView.findViewById(R.id.txtCustomerDebit);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
