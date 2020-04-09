package il.ac.afeka.tomco.a10242_cookeat.Repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Models.CategoryModel;

public class CategoriesRepository {

    private static List<CategoryModel> categoryModels = new ArrayList<>();

    public static CategoryModel findCategoryById(String cuisineId) {
        for (int i = 0; i < categoryModels.size(); i++) {
            if (categoryModels.get(i).getDocumentId().equals(cuisineId))
                return categoryModels.get(i);
        }
        return null;
    }

    public static List<CategoryModel> getCategories() {
      return categoryModels;
    }

    public static void clear() {
        categoryModels.clear();
    }

    public static void addCategory(CategoryModel categoryModel) {categoryModels.add(categoryModel);}

    public static int getCategoriesCount(){
        return categoryModels.size();
    }

}
