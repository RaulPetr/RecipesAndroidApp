package com.example.recipes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.ui.home.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddRecipeActivity extends AppCompatActivity {

    private static final String TAG = AddRecipeActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private EditText name, description, instructions;
    private Button button;
    private ImageView imageView;
    private Bitmap image;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recyclerView = findViewById(R.id.ingredients_list);
        name = findViewById(R.id.recipe_name);
        description = findViewById(R.id.recipe_description);
        instructions = findViewById(R.id.recipe_instructions_add);
        button = findViewById(R.id.image_add_button);
        imageView = findViewById(R.id.recipe_image);


        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if(getIntent().getExtras() != null)
            if(getIntent().getExtras().get("recipeName") != null)
                addInformation(getIntent().getExtras());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).
                        setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                if(r.getError() == null) {
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setImageBitmap(r.getBitmap());
                                    image = r.getBitmap();
                                }
                            }
                        }).
                        show(getSupportFragmentManager());
            }
        });

    }

    public void save(View v) {
        Log.d(TAG, "saved");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference().child("recipeImages");
        Map<String, Object> ingredientsMap = new HashMap<>();
        for(int i = 0; i < recyclerView.getChildCount(); i++) {
            RecyclerAdapter.ViewHolder holder = (RecyclerAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            String name = holder.getName().getText().toString();
            String qt = holder.getQt().getText().toString();
            String qt_type = holder.getQt_type().getSelectedItem().toString();
            if(!name.isEmpty()) {
                Log.d(TAG, "name " + name + " qt " + qt + " type" + qt_type);
                Map<String, Object> quantity = new HashMap<>();

                if (!qt.isEmpty())
                    quantity.put("qt", Integer.parseInt(qt));
                else
                    quantity.put("qt", 0);

                quantity.put("qt_type", qt_type);
                ingredientsMap.put(name, quantity);

            }
        }

        storageRef = storageRef.child(name.getText().toString() + ".jpg");
//        Log.d(TAG, "name" + storageRef.getPath());

        if (image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            UploadTask uploadTask = storageRef.putBytes(baos.toByteArray());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {

                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }

        String user = getIntent().getExtras().getString("user");

        RecipeModel recipe = new RecipeModel(name.getText().toString(),
                instructions.getText().toString(),
                description.getText().toString(),
                "gs://" + storageRef.getBucket() + storageRef.getPath(),
                ingredientsMap,
                user);

        firebaseFirestore.collection("testRecipes").document(recipe.getName()).set(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()) {
                    finish();
                }
            }
        });

    }

    private void addInformation (Bundle extras) {
        String recipeName = extras.getString("recipeName");
        Log.d(TAG, recipeName);
        if(recipeName != null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("testRecipes").whereEqualTo("name", recipeName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = Objects.requireNonNull(task.getResult()).getDocuments().get(0);
                        fillData(document);
                    } else {
                        Toast.makeText(AddRecipeActivity.this, "Could not get data", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void fillData (DocumentSnapshot documentSnapshot) {
        name.setText(documentSnapshot.getString("name"));
        description.setText(documentSnapshot.getString("description"));
//        adapter.addIngredients((Map<String, Object>)documentSnapshot.get("ingredients"));
        instructions.setText(documentSnapshot.getString("instructions"));
    }
}