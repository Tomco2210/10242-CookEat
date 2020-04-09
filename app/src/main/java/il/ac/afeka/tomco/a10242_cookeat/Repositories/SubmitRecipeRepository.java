package il.ac.afeka.tomco.a10242_cookeat.Repositories;

import android.net.Uri;

import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;

public class SubmitRecipeRepository {

    private static RecipeModel recipeModel;
    private static Uri mUri;

    public static RecipeModel getRecipeModel() {
        if (recipeModel == null)
            recipeModel = new RecipeModel();
        return recipeModel;
    }

    public SubmitRecipeRepository() {
        recipeModel = new RecipeModel();
    }

    public static void setUri(Uri uri) {
        mUri = uri;
    }

    public static Uri getUri() {
        return mUri;
    }
}
