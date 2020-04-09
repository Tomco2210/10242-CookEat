package il.ac.afeka.tomco.a10242_cookeat.ui.RecipeFragment;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import il.ac.afeka.tomco.a10242_cookeat.Adapters.EquipmentRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.IngredientRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.InstructionRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.RecipeRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;
import il.ac.afeka.tomco.a10242_cookeat.Network.Constants;
import il.ac.afeka.tomco.a10242_cookeat.R;

import static il.ac.afeka.tomco.a10242_cookeat.Network.Constants.AND;
import static il.ac.afeka.tomco.a10242_cookeat.Network.Constants.HOURS;
import static il.ac.afeka.tomco.a10242_cookeat.Network.Constants.MINUTES;
import static il.ac.afeka.tomco.a10242_cookeat.Network.Constants.READY_IN;

public class RecipeFragment extends Fragment {
    private RecipeModel recipeModel;
    private TextView rTitleTv;
    private ImageView rImageIV;
    private CheckBox rLikeCB;
    private TextView rTimeTV;
    private TextView rSubmitterTV;
    private RecyclerView rIngredientsLV;
    private RecyclerView rEquipmentLV;
    private RecyclerView rInstructionsLV;
    FirebaseDatabase favDB = FirebaseDatabase.getInstance();
    DatabaseReference cuFavRef = favDB.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favorites");
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            recipeModel = getArguments().getParcelable(Constants.ARG_RECIPE);
            toolbar.setTitle(recipeModel.getTitle());
        }
        Log.d(Constants.ARG_RECIPE, "recipeModel: " + recipeModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rTitleTv = view.findViewById(R.id.rTitleTV);
        rImageIV = view.findViewById(R.id.rImageIV);
        rLikeCB = view.findViewById(R.id.rLikeCB);
        rTimeTV = view.findViewById(R.id.rTimeTV);
        rSubmitterTV = view.findViewById(R.id.rSubmitterTV);
        rIngredientsLV = view.findViewById(R.id.rIngredientsLV);
        rEquipmentLV = view.findViewById(R.id.rEquipmentLV);
        rInstructionsLV = view.findViewById(R.id.rInstructionsLV);

        setRecipe();
    }

    private void setRecipe() {
        if (recipeModel == null) return;

        Picasso.get()
                .load(recipeModel.getImageUrl())
                .fit()
                .placeholder(R.drawable.food)
                .into(rImageIV);
        rTitleTv.setText(recipeModel.getTitle());
        rTimeTV.setText(READY_IN + (recipeModel.getReadyInMinutes() / 60) + HOURS + AND + (recipeModel.getReadyInMinutes() % 60) + MINUTES);
        rSubmitterTV.setText(recipeModel.getUploadingUserID());
        IngredientRecyclerViewAdapter ingredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(getContext(), recipeModel.getIngredientsList());
        EquipmentRecyclerViewAdapter equipmentRecyclerViewAdapter = new EquipmentRecyclerViewAdapter(getContext(), recipeModel.getEquipmentList());
        InstructionRecyclerViewAdapter instructionRecyclerViewAdapter = new InstructionRecyclerViewAdapter(getContext(), recipeModel.getInstructionList());
        rIngredientsLV.setAdapter(ingredientRecyclerViewAdapter);
        rEquipmentLV.setAdapter(equipmentRecyclerViewAdapter);
        rInstructionsLV.setAdapter(instructionRecyclerViewAdapter);
        rIngredientsLV.setHasFixedSize(true);
        rIngredientsLV.setLayoutManager(new LinearLayoutManager(getContext()));
        rEquipmentLV.setHasFixedSize(true);
        rEquipmentLV.setLayoutManager(new LinearLayoutManager(getContext()));
        rInstructionsLV.setHasFixedSize(true);
        rInstructionsLV.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientRecyclerViewAdapter.notifyDataSetChanged();
        equipmentRecyclerViewAdapter.notifyDataSetChanged();
        instructionRecyclerViewAdapter.notifyDataSetChanged();
        cuFavRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).hasChild(recipeModel.getDocumentId()))
                    rLikeCB.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       rLikeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cuFavRef.child(userId).child(recipeModel.getDocumentId()).setValue(true);
                } else {
                    cuFavRef.child(userId).child(recipeModel.getDocumentId()).removeValue();
                }
            }
        });
    }
}
