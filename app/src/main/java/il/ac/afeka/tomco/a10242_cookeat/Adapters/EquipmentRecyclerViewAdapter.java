package il.ac.afeka.tomco.a10242_cookeat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.R;

public class EquipmentRecyclerViewAdapter extends RecyclerView.Adapter<EquipmentRecyclerViewAdapter.EquipmentViewHolder> {
    private List<String> mEquipments;
    private Context mContext;

    public EquipmentRecyclerViewAdapter(Context context, List<String> equipments) {
        mEquipments = equipments;
        mContext = context;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_equipment_row, parent, false);
        EquipmentViewHolder holder = new EquipmentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, final int position) {
        holder.lvEquipmentNameTV.setText(mEquipments.get(position));
    }

    @Override
    public int getItemCount() {
        return mEquipments.size();
    }

    public class EquipmentViewHolder extends RecyclerView.ViewHolder {

        TextView lvEquipmentNameTV;

        public EquipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            lvEquipmentNameTV = itemView.findViewById(R.id.lvEquipmentNameTV);
        }
    }
}
