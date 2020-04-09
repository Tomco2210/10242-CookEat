package il.ac.afeka.tomco.a10242_cookeat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.R;

public class SubmitInstructionRecyclerViewAdapter extends RecyclerView.Adapter<SubmitInstructionRecyclerViewAdapter.SubmitInstructionsViewHolder> {
    private List<String> mIinstructions;
    private Context mContext;

    public SubmitInstructionRecyclerViewAdapter(Context context, List<String> instructions) {
        mIinstructions = instructions;
        mContext = context;
    }

    @NonNull
    @Override
    public SubmitInstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_instruction_row_submit, parent, false);
        SubmitInstructionsViewHolder holder = new SubmitInstructionsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitInstructionsViewHolder holder, final int position) {
        holder.lvInstInstTV.setText(mIinstructions.get(position));
        holder.lvInstRemoveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIinstructions.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIinstructions.size();
    }

    public class SubmitInstructionsViewHolder extends ViewHolder {

        TextView lvInstInstTV;
        ImageButton lvInstRemoveBTN;

        public SubmitInstructionsViewHolder(@NonNull View itemView) {
            super(itemView);
            lvInstInstTV = itemView.findViewById(R.id.lvInstSubmitInstTV);
            lvInstRemoveBTN = itemView.findViewById(R.id.lvInstRemoveBTN);
        }
    }
}
