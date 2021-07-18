package com.example.recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipes.databinding.ActivitySingleRecipeBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SingleRecipeActivity extends AppCompatActivity {

    private ActivitySingleRecipeBinding binding;
    private static final String TAG = SingleRecipeActivity.class.getSimpleName();
    ImageView image;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    String recipeName;
    Toolbar toolbar;
    CollapsingToolbarLayout toolBarLayout;

    TextView description;
    TextView ingredients;
    TextView instructions;

    Button gotoLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingleRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

//        FloatingActionButton fab = binding.fab;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        image = findViewById(R.id.imageRecipeSingle);
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        recipeName = getIntent().getStringExtra("recipeName");
        description = findViewById(R.id.recipe_description_single);
        ingredients = findViewById(R.id.recipe_ingredients);
        instructions = findViewById(R.id.recipe_instructions);
        gotoLink = findViewById(R.id.goto_web);

        if(recipeName != null) {
            CollectionReference recipes = firestore.collection("testRecipes");
            Query query = recipes.whereEqualTo("name", recipeName);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = Objects.requireNonNull(task.getResult()).getDocuments().get(0);
                        showData(document);
                    } else {
                        Toast.makeText(SingleRecipeActivity.this, "Could not get data", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private void showData(DocumentSnapshot document) {

        toolBarLayout.setTitle(document.getString("name"));

        if(document.contains("imageLink") && !document.get("imageLink").toString().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(SingleRecipeActivity.this)
                    .load(storage.getReferenceFromUrl(Objects.requireNonNull(document.getString("imageLink"))))
                    .apply(options)
                    .into(image);
        }

        if(document.contains("description")) {
            description.setText(document.getString("description"));
        }

        if(document.contains("ingredients")) {
            String ingredients_string = null;
            Map<String, Object> ingredients_map = (Map<String, Object>) document.get("ingredients");
            if(ingredients_map != null)
                ingredients_string = parse_ingredients(ingredients_map);
            if(ingredients_string != null)
                ingredients.setText(ingredients_string);
        }

        if(document.contains("instructions")) {
            instructions.setText(document.getString("instructions"));
            URLButton(document.getString("instructions"));
        }
    }

    private String parse_ingredients(Map<String, Object> ingredients_map) {
        Set<String> keyset = ingredients_map.keySet();
        StringBuilder result = new StringBuilder();
        for(String i : keyset) {
            Map<String, Object> quantity = (Map<String, Object>) ingredients_map.get(i);
            if(quantity != null) {
                if (quantity.containsKey("qt")) {
                    result.append(quantity.get("qt"));
                    result.append(" ");
                }
                if (quantity.containsKey("qt_type")) {
                    result.append(quantity.get("qt_type"));
                    result.append(" ");
                }
            }
            result.append(i);
            result.append("\n");
        }
        return result.toString();
    }

    private void URLButton (String instructions) {
        if(URLUtil.isValidUrl(instructions)) {
            gotoLink.setVisibility(View.VISIBLE);
            gotoLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(instructions)));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deleteRecipe();
                return true;
            case R.id.menu_edit:
                editRecipe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteRecipe() {
        //check user credentials
        firestore.collection("testRecipes").document(recipeName).delete();
        finish();
    }

    private void editRecipe() {
        //check user credentials
        Intent intent = new Intent(SingleRecipeActivity.this, AddRecipeActivity.class);
        intent.putExtra("recipeName", recipeName);
        startActivity(intent);
    }
}