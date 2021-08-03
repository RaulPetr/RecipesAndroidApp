package com.example.recipes.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipes.R;
import com.example.recipes.SingleRecipeActivity;
import com.example.recipes.ui.home.HomeFragment;
import com.example.recipes.ui.home.RecipeModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class SearchFragment extends Fragment {

    private EditText search;
    private RadioButton name;
    private RadioButton desc;
    private RadioButton inst;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private Query query;
    private static String TAG = SearchFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recipesListSearch);
        search = view.findViewById(R.id.search_text);
        name = view.findViewById(R.id.radioButtName);
        desc = view.findViewById(R.id.radioButtDesc);
        inst = view.findViewById(R.id.radioButtInst);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        query = getQuery(false, false, false, firebaseFirestore);
        FirestoreRecyclerOptions<RecipeModel> options = new FirestoreRecyclerOptions.Builder<RecipeModel>()
                .setQuery(query, RecipeModel.class).build();

        adapter = new FirestoreRecyclerAdapter<RecipeModel, RecipeViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
                return new RecipeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull RecipeViewHolder holder, int position, @NonNull @NotNull RecipeModel model) {
                if(model.getImageLink() != null && !model.getImageLink().isEmpty()) {
                    StorageReference storageRef = storage.getReferenceFromUrl(model.getImageLink());

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(view.getContext())
                            .load(storageRef)
                            .apply(options)
                            .into(holder.image);
                }

                holder.name.setText(model.getName());
                holder.description.setText(model.getDescription());
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d(TAG, "enter pressed");

                    query = getQuery(name.isChecked(), desc.isChecked(), inst.isChecked(), firebaseFirestore);
                    FirestoreRecyclerOptions<RecipeModel> options = new FirestoreRecyclerOptions.Builder<RecipeModel>()
                            .setQuery(query, RecipeModel.class).build();
                    adapter.updateOptions(options);
                    return true;
                }
                return false;
            }
        });

        return view;
    }


//    private void doSearch (boolean name, boolean desc, boolean inst, View view) {
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        Query query = getQuery(name, desc, inst, firebaseFirestore);
//        Log.d(TAG, "search");
//        if(query == null)
//            return;
//        FirestoreRecyclerOptions<RecipeModel> options = new FirestoreRecyclerOptions.Builder<RecipeModel>()
//                .setQuery(query, RecipeModel.class).build();
//        adapter = new FirestoreRecyclerAdapter<RecipeModel, RecipeViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull RecipeModel model) {
//                if(model.getImageLink() != null && !model.getImageLink().isEmpty()) {
//                    StorageReference storageRef = storage.getReferenceFromUrl(model.getImageLink());
//
//                    RequestOptions options = new RequestOptions()
//                            .centerCrop()
//                            .placeholder(R.mipmap.ic_launcher_round)
//                            .error(R.mipmap.ic_launcher_round);
//                    Glide.with(view.getContext())
//                            .load(storageRef)
//                            .apply(options)
//                            .into(holder.image);
//                }
//
//                holder.name.setText(model.getName());
//                holder.description.setText(model.getDescription());
//            }
//
//            @NonNull
//            @Override
//            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
//                return new RecipeViewHolder(view);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                return position;
//            }
//        };
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setAdapter(adapter);
////        recyclerView.setVisibility(View.VISIBLE);
//        Log.d(TAG, "done");
//
//
//
//    }

    private Query getQuery(boolean name, boolean desc, boolean inst, FirebaseFirestore fstore) {
        Query query = fstore.collection("testRecipes");
        if(name) {
            query = fstore.collection("testRecipes").whereEqualTo("name", search.getText().toString());//.whereEqualTo("name", search.getText().toString());
        }
        if(desc){
            // Algolia search
            //2 indexex, name-description and name-instructions
        }
        if(inst){

        }

        return query;
    }

    private class RecipeViewHolder  extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private ImageView image;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipeName);
            description = itemView.findViewById(R.id.recipeDescription);
            image = itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText() != null) {
                        Intent intent = new Intent(itemView.getContext(), SingleRecipeActivity.class);
                        intent.putExtra("recipeName", name.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
