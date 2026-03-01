package com.raithumitra.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageVerifier {

    public interface VerificationCallback {
        void onSuccess();

        void onFailure(String reason);
    }

    public static void verifyImage(Context context, Uri imageUri, String expectedType, VerificationCallback callback) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

            labeler.process(image)
                    .addOnSuccessListener(labels -> {
                        boolean matchFound = false;
                        StringBuilder detectedLabels = new StringBuilder();

                        // Define keywords for each equipment type
                        List<String> keywords = getKeywordsForType(expectedType);

                        for (ImageLabel label : labels) {
                            String text = label.getText().toLowerCase();
                            float confidence = label.getConfidence();
                            detectedLabels.append(text).append(", ");

                            // Check if any label matches our keywords
                            for (String keyword : keywords) {
                                if (text.contains(keyword) && confidence > 0.5) {
                                    matchFound = true;
                                    break;
                                }
                            }
                            if (matchFound)
                                break;
                        }

                        if (matchFound) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Image does not look like a " + expectedType +
                                    ". Detected: " + detectedLabels.toString());
                        }
                    })
                    .addOnFailureListener(e -> {
                        callback.onFailure("AI Verification Failed: " + e.getMessage());
                    });

        } catch (IOException e) {
            callback.onFailure("Could not load image: " + e.getMessage());
        }
    }

    private static List<String> getKeywordsForType(String type) {
        type = type.toUpperCase();
        switch (type) {
            case "TRACTOR":
                return Arrays.asList("tractor", "vehicle", "farm", "agriculture", "machinery", "wheel");
            case "DRONE":
                return Arrays.asList("drone", "aircraft", "flying", "copter", "technology");
            case "HARVESTER":
                return Arrays.asList("harvester", "combine", "farm", "agriculture", "vehicle");
            case "PLOUGH":
                return Arrays.asList("plough", "plow", "tool", "farm", "metal");
            default:
                // Fallback for unknown types - allow generic farming terms
                return Arrays.asList("farm", "agriculture", "tool", "machine", "vehicle");
        }
    }
}
