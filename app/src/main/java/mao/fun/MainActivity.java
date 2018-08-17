package mao.fun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "maoTest";
    private MyViewGroup mMyViewGroup;
    private List<String> mLabel;
    private int margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyViewGroup = (MyViewGroup) findViewById(R.id.flowLayout);

        initData();
        showLabels();
    }

    private void initData() {
        mLabel = new ArrayList<>();
        String[] labels = getResources().getStringArray(R.array.labels);
        Collections.addAll(mLabel, labels);
        margin = (int) (getResources().getDisplayMetrics().density * 5);
    }

    private void showLabels() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);

        for (String label : mLabel) {
            final TextView textView = new TextView(this);
            textView.setText(label);
            textView.setLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_shape));
            textView.setLayoutParams(params);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            //textView.setMaxWidth(dm.widthPixels-mMyViewGroup.getPaddingLeft()-mMyViewGroup.getPaddingRight()-params.leftMargin-params.rightMargin);
            mMyViewGroup.addView(textView);
        }
    }
}
