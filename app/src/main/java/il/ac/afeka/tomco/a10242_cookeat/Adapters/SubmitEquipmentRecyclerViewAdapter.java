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

public class SubmitEquipmentRecyclerViewAdapter extends RecyclerView.Adapter<SubmitEquipmentRecyclerViewAdapter.SubmitEquipmentViewHolder> {
    private List<String> mEquipments;
    private Context mContext;

    public SubmitEquipmentRecyclerViewAdapter(Context context, List<String> equipments) {
        mEquipments = equipments;
        mContext = context;
    }

    @NonNull
    @Override
    public SubmitEquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_equipment_row_submit, parent, false);
        return new SubmitEquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitEquipmentViewHolder holder, final int position) {
        holder.lvEquipmentSubmitNameTV.setText(mEquipments.get(position));
        holder.lvEquipmentRemoveBTN.setOnClickListener(v -> {
            mEquipments.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mEquipments.size();
    }

    public class SubmitEquipmentViewHolder extends RecyclerView.ViewHolder {

        TextView lvEquipmentSubmitNameTV;
        ImageButton lvEquipmentRemoveBTN;

        public SubmitEquipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            lvEquipmentSubmitNameTV = itemView.findViewById(R.id.lvEquipSubmitNameTV);
            lvEquipmentRemoveBTN = itemView.findViewById(R.id.lvEquipmentRemoveBTN);
        }
    }
}
