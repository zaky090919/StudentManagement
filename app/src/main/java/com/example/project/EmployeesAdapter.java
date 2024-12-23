package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeeViewHolder> {

    private ArrayList<User> employeeList;
    private Context context;
    private DatabaseHelper dbHelper;

    public EmployeesAdapter(ArrayList<User> employeeList, Context context) {
        this.employeeList = employeeList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employees, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        User user = employeeList.get(position);  // Get the User object
        holder.tvEmployeeName.setText(user.getUsername()); // Set the username text

        // Set up delete button functionality with confirmation
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Apakah Anda yakin ingin menghapus karyawan ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Delete the employee from the database
                        int userId = user.getUserId(); // Get the userId from the User object
                        dbHelper.deleteUserById(userId); // Delete user by ID

                        // Remove the item from the list and notify adapter
                        employeeList.remove(position);
                        notifyItemRemoved(position);

                        // Show a confirmation message
                        Toast.makeText(context, "Karyawan berhasil dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployeeName;
        Button btnDelete;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            btnDelete = itemView.findViewById(R.id.btnDeleteEmployee);
        }
    }
}
