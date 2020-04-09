package il.ac.afeka.tomco.a10242_cookeat.Repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;

public class RecipeListRepository {

    private static ArrayList<RecipeModel> recipeModels = new ArrayList<>();

    public static void clear() {
        recipeModels.clear();
    }

    public static void addRecipe(RecipeModel recipeModel) {
        recipeModels.add(recipeModel);
    }

    public static ArrayList<RecipeModel> getRecipes() {
        return recipeModels;
    }

    public static RecipeModel findCategoryById(String recipeId) {
        for (int i = 0; i < recipeModels.size(); i++) {
            if (recipeModels.get(i).getDocumentId().equals(recipeId))
                return recipeModels.get(i);
        }
        return null;
    }

    public static int getRecipeCount()
    {
        return recipeModels.size();
    }

}
