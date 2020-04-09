package il.ac.afeka.tomco.a10242_cookeat.ui.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import il.ac.afeka.tomco.a10242_cookeat.Adapters.CategoriesRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.OnCategoryClickListener;
import il.ac.afeka.tomco.a10242_cookeat.Models.CategoryModel;
import il.ac.afeka.tomco.a10242_cookeat.Network.Constants;
import il.ac.afeka.tomco.a10242_cookeat.R;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.CategoriesRepository;
import il.ac.afeka.tomco.a10242_cookeat.ui.recipe_list.RecipeListFragment;

public class CategoriesFragment extends Fragment implements OnCategoryClickListener {

    private CategoriesRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_categories, container, false);

        adapter = new CategoriesRecyclerViewAdapter(getContext(), CategoriesRepository.getCategories());
        RecyclerView recyclerView = root.findViewById(R.id.categoriesRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);
        createRepository();
        adapter.setOnCategoryClickListener(this);
        return root;
    }

    @Override
    public void onCategoryClicked(CategoryModel category) {
        String categoryId = category.getDocumentId();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_CUISINE_ID, categoryId);

        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_recipe_list,args);

    }

    public void createRepository() {
        CategoriesRepository.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cuisines").orderBy("title", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("", document.getId() + "=>" + document.getData().get("title") + "->" + document.getData().get("ImagePath"));
                            CategoryModel c = document.toObject(CategoryModel.class);
                            c.setDocumentId(document.getId());
                            CategoriesRepository.addCategory(c);
//                            Log.d("", CategoriesRepository.getCategories().get(CategoriesRepository.getCategoriesCount()-1).toString());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("", "Error getting documents.", task.getException());
                    }
                });
    }
}