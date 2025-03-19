package co.nz.tsb.interview.bankrecmatchmaker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "co.nz.tsb.interview.target_match_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView matchText = findViewById(R.id.match_text);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_find_match);


        float target = getIntent().getFloatExtra(TARGET_MATCH_VALUE, 10000f);
        matchText.setText(getString(R.string.select_matches, (int) target));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<MatchItem> items = buildMockData();
        final MatchAdapter adapter = new MatchAdapter(items);

        recyclerView.setAdapter(adapter);
    }

    private List<MatchItem> buildMockData() {
        List<MatchItem> items = new ArrayList<>();
        items.add(new MatchItem("City Limousines", "30 Aug", 249.00f, "Sales Invoice"));
        items.add(new MatchItem("Ridgeway University", "12 Sep", 618.50f, "Sales Invoice"));
        items.add(new MatchItem("Cube Land", "22 Sep", 495.00f, "Sales Invoice"));
        items.add(new MatchItem("Bayside Club", "23 Sep", 234.00f, "Sales Invoice"));
        items.add(new MatchItem("SMART Agency", "12 Sep", 250f, "Sales Invoice"));
        items.add(new MatchItem("PowerDirect", "11 Sep", 108.60f, "Sales Invoice"));
        items.add(new MatchItem("PC Complete", "17 Sep", 216.99f, "Sales Invoice"));
        items.add(new MatchItem("Truxton Properties", "17 Sep", 181.25f, "Sales Invoice"));
        items.add(new MatchItem("MCO Cleaning Services", "17 Sep", 170.50f, "Sales Invoice"));
        items.add(new MatchItem("Gateway Motors", "18 Sep", 411.35f, "Sales Invoice"));
        return items;
    }

}