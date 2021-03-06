// Generated by view binder compiler. Do not edit!
package com.example.recipes.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.example.recipes.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddRecipeBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final Button imageAddButton;

  @NonNull
  public final RecyclerView ingredientsList;

  @NonNull
  public final EditText recipeDescription;

  @NonNull
  public final ImageView recipeImage;

  @NonNull
  public final EditText recipeInstructionsAdd;

  @NonNull
  public final EditText recipeName;

  @NonNull
  public final Toolbar toolbarAddRecipe;

  private ActivityAddRecipeBinding(@NonNull NestedScrollView rootView,
      @NonNull Button imageAddButton, @NonNull RecyclerView ingredientsList,
      @NonNull EditText recipeDescription, @NonNull ImageView recipeImage,
      @NonNull EditText recipeInstructionsAdd, @NonNull EditText recipeName,
      @NonNull Toolbar toolbarAddRecipe) {
    this.rootView = rootView;
    this.imageAddButton = imageAddButton;
    this.ingredientsList = ingredientsList;
    this.recipeDescription = recipeDescription;
    this.recipeImage = recipeImage;
    this.recipeInstructionsAdd = recipeInstructionsAdd;
    this.recipeName = recipeName;
    this.toolbarAddRecipe = toolbarAddRecipe;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddRecipeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddRecipeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_recipe, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddRecipeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.image_add_button;
      Button imageAddButton = rootView.findViewById(id);
      if (imageAddButton == null) {
        break missingId;
      }

      id = R.id.ingredients_list;
      RecyclerView ingredientsList = rootView.findViewById(id);
      if (ingredientsList == null) {
        break missingId;
      }

      id = R.id.recipe_description;
      EditText recipeDescription = rootView.findViewById(id);
      if (recipeDescription == null) {
        break missingId;
      }

      id = R.id.recipe_image;
      ImageView recipeImage = rootView.findViewById(id);
      if (recipeImage == null) {
        break missingId;
      }

      id = R.id.recipe_instructions_add;
      EditText recipeInstructionsAdd = rootView.findViewById(id);
      if (recipeInstructionsAdd == null) {
        break missingId;
      }

      id = R.id.recipe_name;
      EditText recipeName = rootView.findViewById(id);
      if (recipeName == null) {
        break missingId;
      }

      id = R.id.toolbar_add_recipe;
      Toolbar toolbarAddRecipe = rootView.findViewById(id);
      if (toolbarAddRecipe == null) {
        break missingId;
      }

      return new ActivityAddRecipeBinding((NestedScrollView) rootView, imageAddButton,
          ingredientsList, recipeDescription, recipeImage, recipeInstructionsAdd, recipeName,
          toolbarAddRecipe);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
