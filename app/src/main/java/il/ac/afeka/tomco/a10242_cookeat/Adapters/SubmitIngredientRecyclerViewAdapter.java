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

public class SubmitIngredientRecyclerViewAdapter extends RecyclerView.Adapter<SubmitIngredientRecyclerViewAdapter.SubmitIngredientViewHolder> {
    private List<String> mIngredients;
    private Context mContext;

    public SubmitIngredientRecyclerViewAdapter(Context context, List<String> ingredients) {
        mIngredients = ingredients;
        mContext = context;
    }

    @NonNull
    @Override
    public SubmitIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_ingredient_row_submit, parent, false);
        SubmitIngredientViewHolder holder = new SubmitIngredientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitIngredientViewHolder holder, final int position) {
        holder.lvIngNameTV.setText(mIngredients.get(position));
        holder.lvIngRemoveBTN.setOnClickListener(v -> {
            mIngredients.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class SubmitIngredientViewHolder extends RecyclerView.ViewHolder {

        TextView lvIngNameTV;
        ImageButton lvIngRemoveBTN;

        public SubmitIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            lvIngNameTV = itemView.findViewById(R.id.lvIngSubmitNameTV);
            lvIngRemoveBTN = itemView.findViewById(R.id.lvIngRemoveBTN);
        }
    }
}
