package il.ac.afeka.tomco.a10242_cookeat.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import il.ac.afeka.tomco.a10242_cookeat.Models.CategoryModel;
import il.ac.afeka.tomco.a10242_cookeat.R;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.CategoriesRepository;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoryViewHolder> {
    private List<CategoryModel> mCategoryModels;
    private OnCategoryClickListener listener;
    private Context mContext;

    public CategoriesRecyclerViewAdapter(Context context, List<CategoryModel> categoryModels) {
        mCategoryModels = categoryModels;
        mContext = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryTitle.setText(mCategoryModels.get(position).getTitle());
        Picasso.get()
                .load(mCategoryModels.get(position).getImagePath())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.food)
                .into(holder.categoryImage);
    }

    @Override
    public int getItemCount() {
        if (mCategoryModels != null)
            return mCategoryModels.size();
        return 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView categoryImage;
        public TextView categoryTitle;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.category_image);
            categoryTitle = itemView.findViewById(R.id.category_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                  listener.onCategoryClicked(CategoriesRepository.getCategories().get(position));
            }
        }
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }
}
