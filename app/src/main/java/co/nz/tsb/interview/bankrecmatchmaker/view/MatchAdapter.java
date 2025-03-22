package co.nz.tsb.interview.bankrecmatchmaker.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.nz.tsb.interview.bankrecmatchmaker.databinding.ListItemMatchBinding;
import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem;
import co.nz.tsb.interview.bankrecmatchmaker.core.Utils;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemMatchBinding binding;
        public ViewHolder(ListItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MatchItem matchItem, boolean selected, boolean hinted) {
            binding.textMain.setText(matchItem.getPaidTo());
            binding.textTotal.setText(Float.toString(matchItem.getTotal()));
            binding.textSubLeft.setText(matchItem.getTransactionDate());
            binding.textSubRight.setText(matchItem.getDocType());
            binding.getRoot().setChecked(selected);
            if(hinted) {
                binding.getRoot().setBackgroundColor(Color.CYAN);
            } else {
                binding.getRoot().setBackgroundColor(Color.WHITE);
            }
        }
    }

    interface OnItemClickedListener {
        void onClicked(int position, boolean selected);
    }

    private List<MatchItem> records = new ArrayList<>();

    private Set<Integer> selectedItems = new HashSet<>();

    private Set<Integer> hints = new HashSet<>();

    private OnItemClickedListener itemClickedListener;

    public MatchAdapter(OnItemClickedListener listener) {
        itemClickedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemMatchBinding binding = ListItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        ViewHolder holder = new ViewHolder(binding);

        binding.getRoot().setOnClickListener(v -> {
            itemClickedListener.onClicked(
                    holder.getAdapterPosition(),
                    !((Checkable)binding.getRoot()).isChecked()
            );
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchItem matchItem = records.get(position);
        holder.bind(matchItem,
                selectedItems.contains(position),
                hints.contains(position)
        );
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateRecords(List<MatchItem> records) {
        this.records = records;
        this.selectedItems.clear();

        notifyDataSetChanged();
    }

    public void updateSelection(Set<Integer> items) {
        Set<Integer> changes = Utils.Companion.findDifference(this.selectedItems, items);

        selectedItems = items;

        for(int idx : changes) {
            notifyItemChanged(idx);
        }
    }

    public void updateHints(Set<Integer> hints) {
        Set<Integer> changes = Utils.Companion.findDifference(this.hints, hints);

        this.hints = hints;
        
        for(int idx : changes) {
            notifyItemChanged(idx);
        }
    }
}