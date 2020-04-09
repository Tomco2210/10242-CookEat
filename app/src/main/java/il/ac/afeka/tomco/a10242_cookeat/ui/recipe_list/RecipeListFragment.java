package il.ac.afeka.tomco.a10242_cookeat.ui.recipe_list;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Adapters.OnRecipeClickListener;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.RecipeRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Models.CategoryModel;
import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;
import il.ac.afeka.tomco.a10242_cookeat.Network.Constants;
import il.ac.afeka.tomco.a10242_cookeat.R;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.CategoriesRepository;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.RecipeListRepository;

public class RecipeListFragment extends Fragment implements OnRecipeClickListener {

    private RecyclerView mRecyclerView;
    private RecipeRecyclerViewAdapter mAdapter;

    private boolean inFavFregmant = false;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mAdapter = new RecipeRecyclerViewAdapter(getContext(), RecipeListRepository.getRecipes());
        mRecyclerView = view.findViewById(R.id.recipeListRV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        Log.d("title", toolbar.getTitle().toString());
        if (toolbar.getTitle().toString().compareTo(getResources().getString(R.string.menu_favorites)) == 0)
            inFavFregmant = true;
        if (!inFavFregmant)
            toolbar.setTitle(CategoriesRepository.findCategoryById(getArguments().getString(Constants.ARG_CUISINE_ID)).getTitle());
        createRepository();
        mAdapter.setOnRecipeClickListener(this);
        return view;
    }

    public void createRepository() {
        RecipeListRepository.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (!inFavFregmant) {
            String cuisineToRetrieve = getArguments().getString(Constants.ARG_CUISINE_ID);
            db.collection("Recipes").whereEqualTo(Constants.ARG_CUISINE_ID, cuisineToRetrieve).get()
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
        } else {
            FirebaseDatabase favDB = FirebaseDatabase.getInstance();
            DatabaseReference cuFavRef = favDB.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favorites");

            cuFavRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren();
                    for (DataSnapshot document : dataSnapshots) {
                        String recipeId = document.getKey();
                        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        RecipeModel r = doc.toObject(RecipeModel.class);
                                        r.setDocumentId(doc.getId());
                                        RecipeListRepository.addRecipe(r);
                                        Log.d("", RecipeListRepository.getRecipes().get(RecipeListRepository.getRecipeCount() - 1).toString());
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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
