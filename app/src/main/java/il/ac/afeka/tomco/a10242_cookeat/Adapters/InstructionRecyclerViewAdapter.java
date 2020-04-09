package il.ac.afeka.tomco.a10242_cookeat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.R;

public class InstructionRecyclerViewAdapter extends RecyclerView.Adapter<InstructionRecyclerViewAdapter.InstructionsViewHolder> {
    private List<String> mIinstructions;
    private Context mContext;

    public InstructionRecyclerViewAdapter(Context context, List<String> instructions) {
        mIinstructions = instructions;
        mContext = context;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_instruction_row, parent, false);
        InstructionsViewHolder holder = new InstructionsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, final int position) {
        holder.lvInstInstTV.setText(mIinstructions.get(position));
    }

    @Override
    public int getItemCount() {
        return mIinstructions.size();
    }

    public class InstructionsViewHolder extends ViewHolder {

        TextView lvInstInstTV;

        public InstructionsViewHolder(@NonNull View itemView) {
            super(itemView);
            lvInstInstTV = itemView.findViewById(R.id.lvInstInstTV);
        }
    }
}
