package il.ac.afeka.tomco.a10242_cookeat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.R;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.IngredientViewHolder> {
    private List<String> mIngredients;
    private Context mContext;

    public IngredientRecyclerViewAdapter(Context context, List<String> ingredients) {
        mIngredients = ingredients;
        mContext = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_ingredient_row, parent, false);
        IngredientViewHolder holder = new IngredientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, final int position) {
        holder.lvIngNameTV.setText(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView lvIngNameTV;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            lvIngNameTV = itemView.findViewById(R.id.lvIngNameTV);
        }
    }
}
