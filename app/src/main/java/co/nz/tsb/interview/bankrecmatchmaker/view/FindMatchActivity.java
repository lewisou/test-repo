package co.nz.tsb.interview.bankrecmatchmaker.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import co.nz.tsb.interview.bankrecmatchmaker.viewmodel.MatchViewModel;
import co.nz.tsb.interview.bankrecmatchmaker.R;
import co.nz.tsb.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding;
import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "co.nz.tsb.interview.target_match_value";

    private MatchViewModel viewModel;
    private ActivityFindMatchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFindMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_find_match);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(MatchViewModel.class);

        binding.recyclerView.setAdapter(new MatchAdapter((position, selected) ->
                viewModel.selectItem(position, selected)));

        viewModel.getLiveRecords().observe(this, this::updateRecords);

        viewModel.getLiveRemain().observe(this, this::updateRemain);

        viewModel.getLiveSelection().observe(this, this::updateSelection);

        viewModel.getLiveHints().observe(this, this::updateHints);

        viewModel.getLiveAutoSelection().observe(this, this::scrollTo);

        viewModel.init(
//            getIntent().getFloatExtra(TARGET_MATCH_VALUE, 970.25f)
//            getIntent().getFloatExtra(TARGET_MATCH_VALUE, 2140.35f)
            getIntent().getFloatExtra(TARGET_MATCH_VALUE, 618.5f)
        );
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    private void updateRecords(List<MatchItem> records) {
        ((MatchAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter()))
                .updateRecords(records);
    }

    private void updateRemain(double remain) {
        binding.matchText.setText(getString(R.string.select_matches, remain));
    }

    private void updateSelection(Set<Integer> indexes) {
        ((MatchAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter()))
                .updateSelection(indexes);
    }

    private void updateHints(Set<Integer> indexes) {
        ((MatchAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter()))
                .updateHints(indexes);
    }

    private void scrollTo(int index) {
        Objects.requireNonNull(binding.recyclerView).scrollToPosition(index);
    }
}