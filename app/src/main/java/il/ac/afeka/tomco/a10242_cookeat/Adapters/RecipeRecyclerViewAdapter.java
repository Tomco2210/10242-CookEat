package il.ac.afeka.tomco.a10242_cookeat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Models.RecipeModel;
import il.ac.afeka.tomco.a10242_cookeat.R;

import static il.ac.afeka.tomco.a10242_cookeat.Network.Constants.*;


public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecipeModel> mRecipes;
    private Context mContext;
    public OnRecipeClickListener onRecipeClickListener;
    FirebaseDatabase favDB = FirebaseDatabase.getInstance();
    DatabaseReference cuFavRef = favDB.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favorites");
    private String userId;


    public RecipeRecyclerViewAdapter(Context context, List<RecipeModel> recipeModels) {
        mRecipes = recipeModels;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mContext = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        Picasso.get()
                .load(mRecipes.get(i).getImageUrl())
                .fit()
                .placeholder(R.drawable.food)
                .into(((RecipeViewHolder) viewHolder).imageView);

        ((RecipeViewHolder) viewHolder).title.setText(mRecipes.get(i).getTitle());
        ((RecipeViewHolder) viewHolder).submitter.setText(mRecipes.get(i).getUploadingUserID());
        int hours = mRecipes.get(i).getReadyInMinutes() / 60;
        int minutes = mRecipes.get(i).getReadyInMinutes() % 60;
        ((RecipeViewHolder) viewHolder).time.setText(READY_IN + hours + HOURS + AND + minutes + MINUTES);
        cuFavRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userId).hasChild(mRecipes.get(i).getDocumentId()))
                    ((RecipeViewHolder) viewHolder).likeCB.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((RecipeViewHolder) viewHolder).likeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cuFavRef.child(userId).child(mRecipes.get(i).getDocumentId()).setValue(true);
                } else {
                    cuFavRef.child(userId).child(mRecipes.get(i).getDocumentId()).removeValue();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title, time, submitter;
        public ImageView imageView;
        public CheckBox likeCB;


        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.rcTitleTV);
            time = itemView.findViewById(R.id.rcTimeTV);
            submitter = itemView.findViewById(R.id.rcSubmitterTV);
            likeCB = itemView.findViewById(R.id.rcLikeCB);
            imageView = itemView.findViewById(R.id.rcImageIV);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRecipeClickListener.onRecipeClick(getAdapterPosition());
        }
    }

    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.onRecipeClickListener = listener;
    }
}
