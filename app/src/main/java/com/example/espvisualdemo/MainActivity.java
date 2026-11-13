package com.example.espvisualdemo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private DemoView demoView;
    private TextView stateText;
    private Button toggleButton;
    private boolean enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(20));
        root.setBackgroundColor(Color.rgb(10, 15, 24));

        TextView title = new TextView(this);
        title.setText("ESP VISUAL DEMO");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("OFFLINE SANDBOX  •  SIMULATED DATA ONLY");
        subtitle.setTextColor(Color.rgb(90, 210, 170));
        subtitle.setTextSize(11);
        subtitle.setPadding(0, dp(5), 0, dp(16));
        root.addView(subtitle);

        stateText = new TextView(this);
        stateText.setText("VISUALS: OFF");
        stateText.setTextColor(Color.LTGRAY);
        stateText.setTextSize(14);
        stateText.setPadding(dp(12), dp(12), dp(12), dp(12));
        stateText.setBackgroundColor(Color.rgb(25, 34, 48));
        root.addView(stateText);

        demoView = new DemoView(this);
        LinearLayout.LayoutParams previewParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        previewParams.setMargins(0, dp(16), 0, dp(16));
        root.addView(demoView, previewParams);

        toggleButton = new Button(this);
        toggleButton.setText("ENABLE DEMO VISUALS");
        toggleButton.setAllCaps(false);
        toggleButton.setOnClickListener(v -> {
            enabled = !enabled;
            demoView.setEnabledDemo(enabled);
            stateText.setText(enabled
                    ? "VISUALS: ON  •  SAMPLE DATA"
                    : "VISUALS: OFF");
            toggleButton.setText(enabled
                    ? "DISABLE DEMO VISUALS"
                    : "ENABLE DEMO VISUALS");
        });
        root.addView(toggleButton, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(54)));

        TextView footer = new TextView(this);
        footer.setText("This app does not connect to, read, or modify any game.");
        footer.setTextColor(Color.rgb(145, 155, 170));
        footer.setTextSize(11);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(12), 0, 0);
        root.addView(footer);

        setContentView(root);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class DemoView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private boolean active = false;

        DemoView(Activity activity) {
            super(activity);
            setBackgroundColor(Color.rgb(14, 22, 34));
        }

        void setEnabledDemo(boolean value) {
            active = value;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth();
            float h = getHeight();

            paint.setColor(Color.rgb(34, 48, 66));
            paint.setStrokeWidth(1);
            for (int x = 0; x < w; x += 48) {
                canvas.drawLine(x, 0, x, h, paint);
            }
            for (int y = 0; y < h; y += 48) {
                canvas.drawLine(0, y, w, y, paint);
            }

            paint.setColor(Color.rgb(125, 145, 165));
            paint.setTextSize(12);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(active ? "SIMULATED TARGETS" : "PREVIEW PAUSED",
                    dp(14), dp(24), paint);

            if (!active) {
                paint.setColor(Color.rgb(145, 155, 170));
                paint.setTextSize(14);
                paint.setTypeface(Typeface.DEFAULT);
                canvas.drawText("Tap the button below to show sample data",
                        dp(14), h / 2f, paint);
                return;
            }

            drawTarget(canvas, w * 0.24f, h * 0.34f, "BOT 01", "12 m");
            drawTarget(canvas, w * 0.67f, h * 0.56f, "BOT 02", "24 m");
            drawTarget(canvas, w * 0.48f, h * 0.78f, "BOT 03", "08 m");
        }

        private void drawTarget(Canvas canvas, float cx, float cy,
                                String name, String distance) {
            float boxW = dp(76);
            float boxH = dp(112);

            paint.setColor(Color.rgb(70, 235, 170));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(2));
            canvas.drawRect(cx - boxW / 2, cy - boxH / 2,
                    cx + boxW / 2, cy + boxH / 2, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(dp(11));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(name, cx - boxW / 2, cy - boxH / 2 - dp(8), paint);
            paint.setTextSize(dp(10));
            paint.setTypeface(Typeface.DEFAULT);
            canvas.drawText(distance, cx - boxW / 2, cy + boxH / 2 + dp(14), paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(1));
            canvas.drawCircle(cx, cy - boxH * 0.27f, dp(10), paint);
            paint.setStyle(Paint.Style.FILL);
        }

        private int dp(float value) {
            return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
        }
    }
}
