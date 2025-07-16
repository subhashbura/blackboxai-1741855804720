package com.provizit.qrscanner;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class LoaderUtil {
    private static ProgressBar progressBar;

    public static void showLoader(Activity activity) {
        // Create a new ProgressBar every time
        progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;

        progressBar.setLayoutParams(params);

        FrameLayout rootLayout = activity.findViewById(android.R.id.content);
        rootLayout.addView(progressBar);

        progressBar.setVisibility(View.VISIBLE);  // Show the loader
    }

    public static void hideLoader(Activity activity) {
        if (progressBar != null) {
            // Remove progress bar from the layout
            FrameLayout rootLayout = activity.findViewById(android.R.id.content);
            rootLayout.removeView(progressBar);
            progressBar = null;  // Nullify the reference
        }
    }
}
