package com.example.recipes.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.recipes.AddRecipeActivity;
import com.example.recipes.R;
import com.example.recipes.SingleRecipeActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    //de vazut
    //https://proandroiddev.com/glideing-your-way-into-recyclerview-state-invalidation-5632fdb9be85

    private RecyclerView recipesList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private static String TAG = HomeFragment.class.getSimpleName();
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recipesList = view.findViewById(R.id.recipesList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        fab = view.findViewById(R.id.fab_recipes);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //check if logged in, then go to add recipe
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser == null)
                    Toast.makeText(getContext(), "You must be logged in to add a new recipe", Toast.LENGTH_LONG).show();
                else
                    startActivity(new Intent(view.getContext(), AddRecipeActivity.class).putExtra("user", currentUser.getEmail()));
            }
        });


        //Query
        Query query = firebaseFirestore.collection("testRecipes");
        //RecyclerOptions
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

        recipesList.setHasFixedSize(true);
        recipesList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recipesList.setAdapter(adapter);


        return view;
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

    private class RecipeViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView description;
        private ImageView image;

        public RecipeViewHolder(@NonNull @NotNull View itemView) {
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


}