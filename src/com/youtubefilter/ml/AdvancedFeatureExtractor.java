package com.youtubefilter.ml;

import com.youtubefilter.model.Video;
import com.youtubefilter.model.Category;
import java.util.*;
import java.util.regex.Pattern;

public class AdvancedFeatureExtractor {
    
    public double[] extractComprehensiveFeatures(Video video) {
        List<Double> features = new ArrayList<>();
        
        String title = video.getTitle().toLowerCase();
        String description = video.getDescription().toLowerCase();
        String combined = title + " " + description;
        
        // Feature 1-4: Category keyword densities
        for (Category category : Category.values()) {
            double density = calculateKeywordDensity(combined, 
                new HashSet<>(Arrays.asList(category.getKeywords())));
            features.add(density);
        }
        
        // Feature 5: Educational phrase density
        Set<String> eduPhrases = new HashSet<>(Arrays.asList(
            "learn", "tutorial", "lesson", "education", "study", "understand"
        ));
        double eduDensity = calculateKeywordDensity(combined, eduPhrases);
        features.add(eduDensity);
        
        // Feature 6: Title length
        features.add(Math.min(title.length() / 100.0, 1.0));
        
        // Feature 7: Description length
        features.add(Math.min(description.length() / 500.0, 1.0));
        
        // Feature 8: Has question mark?
        features.add(title.contains("?") ? 1.0 : 0.0);
        
        // Feature 9: Has numbers?
        features.add(Pattern.compile("\\d+").matcher(title).find() ? 1.0 : 0.0);
        
        return features.stream().mapToDouble(Double::doubleValue).toArray();
    }
    
    private double calculateKeywordDensity(String text, Set<String> keywords) {
        String[] words = text.split("\\s+");
        int keywordCount = 0;
        
        for (String word : words) {
            if (keywords.contains(word)) {
                keywordCount++;
            }
        }
        
        return words.length > 0 ? (double) keywordCount / words.length : 0.0;
    }
}