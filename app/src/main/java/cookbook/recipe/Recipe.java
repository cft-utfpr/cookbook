package cookbook.recipe;

import java.util.List;

public class Recipe {
    private String name;
    private int duration; // in minutes
    private List<String> steps;
    private List<Ingredient> ingredients;

    public String getName() { return name; }
    public int getDuration() { return duration; }
    public List<String> getSteps() { return steps; }
    public List<Ingredient> getIngredients() { return ingredients; }
}
