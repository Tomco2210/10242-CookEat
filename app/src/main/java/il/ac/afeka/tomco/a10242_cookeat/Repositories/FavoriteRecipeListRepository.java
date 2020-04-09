package il.ac.afeka.tomco.a10242_cookeat.Repositories;

import java.util.ArrayList;

import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;

public class FavoriteRecipeListRepository {

    private static ArrayList<RecipeModel> favoriteRecipeModels = new ArrayList<>();

    public static void clear() {
        favoriteRecipeModels.clear();
    }

    public static void addRecipe(RecipeModel recipeModel) {
        favoriteRecipeModels.add(recipeModel);
    }

    public static void removeRecipe(String documentId) {
        favoriteRecipeModels.remove(getRecipeIndexById(documentId));
    }

    public static int getRecipeIndexById(String documentId) {
        for (RecipeModel r : favoriteRecipeModels
        ) {
            return favoriteRecipeModels.indexOf(r);
        }
        return -1;
    }

    public static ArrayList<RecipeModel> getRecipes() {
        return favoriteRecipeModels;
    }

    public static boolean isFavorite(String documentId) {
        for (RecipeModel r : favoriteRecipeModels
        ) {
            if (r.getDocumentId().equals(documentId))
                return true;
        }
        return false;
    }
}
