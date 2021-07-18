package com.example.recipes;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static String TAG = RecyclerAdapter.class.getSimpleName();
    private int itemCount = 1;

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d(TAG, "position " + position);
        holder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "c\nStart: " + start + "\nBefore: " + before + "\nCount: " + count);
                if(count == 1 && before < count) {
                    itemCount++;
                    notifyItemInserted(itemCount-1);
                    holder.notifyChange();
                    holder.name.removeTextChangedListener(this);
                    Log.d(TAG, "inserted");
                }
                if(s.length() == 0) {
                    itemCount--;
                    notifyItemRemoved(itemCount);
//                    holder.name.removeTextChangedListener(this);
                    holder.notifyChange();
                    Log.d(TAG, "removed");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private EditText name;
        private EditText qt;
        private Spinner qt_type;
        private ArrayAdapter<CharSequence> adapter;

        public EditText getName() {
            return name;
        }

        public EditText getQt() {
            return qt;
        }

        public Spinner getQt_type() {
            return qt_type;
        }



        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredient);
            qt = itemView.findViewById(R.id.qt);
            qt_type = itemView.findViewById(R.id.qt_type);

            //adapter for spinner (quantity type)
            adapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.qt_types, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            qt_type.setAdapter(adapter);
        }

        public void notifyChange() {
            adapter.notifyDataSetChanged();
        }
    }
}
