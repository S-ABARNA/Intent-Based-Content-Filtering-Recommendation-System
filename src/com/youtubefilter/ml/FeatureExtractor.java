package com.youtubefilter.ml;

import com.youtubefilter.model.Video;
import com.youtubefilter.model.Category;
import java.util.*;

public class FeatureExtractor {
    
    public double[] extractFeatures(Video video) {
        List<Double> features = new ArrayList<>();
        
        String title = video.getTitle().toLowerCase();
        String description = video.getDescription().toLowerCase();
        String combined = title + " " + description;
        
        // Feature 1-4: Keyword matches for each category
        for (Category category : Category.values()) {
            double matchCount = 0;
            for (String keyword : category.getKeywords()) {
                if (combined.contains(keyword)) {
                    matchCount++;
                }
            }
            features.add(matchCount / category.getKeywords().length);
        }
        
        // Feature 5: Title length factor
        features.add(Math.min(title.length() / 100.0, 1.0));
        
        // Feature 6: Educational indicators
        String[] eduIndicators = {"learn", "tutorial", "lesson", "education", "course"};
        double eduScore = 0;
        for (String indicator : eduIndicators) {
            if (combined.contains(indicator)) {
                eduScore += 0.2;
            }
        }
        features.add(Math.min(eduScore, 1.0));
        
        // Feature 7: Has numbers (math indicator)
        features.add(title.matches(".*\\d+.*") ? 1.0 : 0.0);
        
        return features.stream().mapToDouble(Double::doubleValue).toArray();
    }
}