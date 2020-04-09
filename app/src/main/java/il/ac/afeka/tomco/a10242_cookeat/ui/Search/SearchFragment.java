package il.ac.afeka.tomco.a10242_cookeat.ui.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Collections;
import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Adapters.OnRecipeClickListener;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.RecipeRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;
import il.ac.afeka.tomco.a10242_cookeat.Network.Constants;
import il.ac.afeka.tomco.a10242_cookeat.R;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.CategoriesRepository;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.RecipeListRepository;
import il.ac.afeka.tomco.a10242_cookeat.ui.recipe_list.RecipeListFragment;

public class SearchFragment extends Fragment implements OnRecipeClickListener {

    private RecyclerView mRecyclerView;
    private RecipeRecyclerViewAdapter mAdapter;
    private SearchView mSearchView;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mAdapter = new RecipeRecyclerViewAdapter(getContext(), RecipeListRepository.getRecipes());
        mSearchView = view.findViewById(R.id.searchSV);
        mRecyclerView = view.findViewById(R.id.searchResultsRV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                createRepository();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                createRepository();
                return true;
            }
        });
        createRepository();
        mAdapter.setOnRecipeClickListener(this);
        return view;
    }

    public void createRepository() {
        RecipeListRepository.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String searchTerm = mSearchView.getQuery().toString();
        db.collection("Recipes").whereArrayContainsAny(Constants.ARG_RECIPE_INGREDIENT_TITLE, Collections.singletonList(searchTerm)).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("", document.getId() + "=>" + document.getData().get("title") + "->" + (List<String>) document.get("ingredientsList"));
                            RecipeModel r = document.toObject(RecipeModel.class);
                            r.setDocumentId(document.getId());
                            RecipeListRepository.addRecipe(r);
                            Log.d("", RecipeListRepository.getRecipes().get(RecipeListRepository.getRecipeCount() - 1).toString());
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.w("", "Error getting documents.", task.getException());
                    }
                });
        db.collection("Recipes").whereIn(Constants.ARG_RECIPE_TITLE, Collections.singletonList(searchTerm)).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("", document.getId() + "=>" + document.getData().get("title") + "->" + (List<String>) document.get("ingredientsList"));
                            RecipeModel r = document.toObject(RecipeModel.class);
                            r.setDocumentId(document.getId());
                            RecipeListRepository.addRecipe(r);
                            Log.d("", RecipeListRepository.getRecipes().get(RecipeListRepository.getRecipeCount() - 1).toString());
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.w("", "Error getting documents.", task.getException());
                    }
                });
        db.collection("Recipes").whereArrayContainsAny(Constants.ARG_RECIPE_INSTRUCTION_TITLE, Collections.singletonList(searchTerm)).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("", document.getId() + "=>" + document.getData().get("title") + "->" + (List<String>) document.get("ingredientsList"));
                            RecipeModel r = document.toObject(RecipeModel.class);
                            r.setDocumentId(document.getId());
                            RecipeListRepository.addRecipe(r);
                            Log.d("", RecipeListRepository.getRecipes().get(RecipeListRepository.getRecipeCount() - 1).toString());
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.w("", "Error getting documents.", task.getException());
                    }
                });
        db.collection("Recipes").whereArrayContainsAny(Constants.ARG_RECIPE_EQUIPMENT_TITLE, Collections.singletonList(searchTerm)).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("", document.getId() + "=>" + document.getData().get("title") + "->" + (List<String>) document.get("ingredientsList"));
                            RecipeModel r = document.toObject(RecipeModel.class);
                            r.setDocumentId(document.getId());
                            RecipeListRepository.addRecipe(r);
                            Log.d("", RecipeListRepository.getRecipes().get(RecipeListRepository.getRecipeCount() - 1).toString());
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.w("", "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void onRecipeClick(int position) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RETRIEVE_TYPE, Constants.ARG_RECIPE);
        args.putParcelable(Constants.ARG_RECIPE, RecipeListRepository.getRecipes().get(position));
        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_recipe, args);
    }


}