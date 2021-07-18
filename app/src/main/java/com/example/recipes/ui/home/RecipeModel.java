package com.example.recipes.ui.home;

import java.util.Map;

public class RecipeModel {
    private String name;
    private String instructions;
    private String description;
    private String imageLink;
    private Map<String, Object> ingredients;
    private String addedBy;

    private RecipeModel() {}

    public RecipeModel(String name, String instructions, String description, String imageLink, Map<String, Object> ingredients, String addedBy) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.description = description;
        this.imageLink = imageLink;
        this.addedBy = addedBy;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Map<String, Object> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Object> ingredients) {
        this.ingredients = ingredients;
    }
}
