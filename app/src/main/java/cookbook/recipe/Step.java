package cookbook.recipe;

import java.util.List;

public class Step {
    private String description;
    private List<Ingredient> ingredients;

    public String getDescription() { return description; }
    public List<Ingredient> getIngredients() { return ingredients; }
}