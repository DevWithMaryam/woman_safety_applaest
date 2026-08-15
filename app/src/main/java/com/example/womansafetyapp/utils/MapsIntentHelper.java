package com.maryam.womensafetyapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Opens Google Maps via plain intents — no embedded Maps SDK / API key required.
 * Falls back to a Toast if no Maps-capable app is installed.
 */
public final class MapsIntentHelper {

    private MapsIntentHelper() { }

    /** Shows a location pin for the given coordinates. */
    public static void viewLocation(Context context, double latitude, double longitude, String label) {
        Uri uri = Uri.parse("geo:" + latitude + "," + longitude
                + "?q=" + latitude + "," + longitude + "(" + Uri.encode(label) + ")");
        launch(context, uri);
    }

    /** Turn-by-turn navigation to the given coordinates. */
    public static void navigateTo(Context context, double latitude, double longitude) {
        Uri uri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        launch(context, uri);
    }

    /** Nearby search, e.g. "police station" or "hospital", biased around the given coordinates. */
    public static void searchNearby(Context context, String query, double latitude, double longitude) {
        Uri uri = Uri.parse("geo:" + latitude + "," + longitude + "?q="
                + Uri.encode(query) + "&z=15");
        launch(context, uri);
    }

    private static void launch(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Fall back to any app that can handle geo: intents.
            Intent fallback = new Intent(Intent.ACTION_VIEW, uri);
            if (fallback.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(fallback);
            } else {
                Toast.makeText(context, R.string.error_maps_unavailable, Toast.LENGTH_LONG).show();
            }
        }
    }
}