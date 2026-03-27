package com.youtubefilter.ml;

import com.youtubefilter.model.Video;
import com.youtubefilter.model.Category;
import java.util.*;

public class MLModel {
    private FeatureExtractor featureExtractor;
    private Map<String, double[]> trainingFeatures;
    private Map<String, Category> trainingLabels;
    
    public MLModel() {
        this.featureExtractor = new FeatureExtractor();
        this.trainingFeatures = new HashMap<>();
        this.trainingLabels = new HashMap<>();
    }
    
    public void train(List<Video> trainingVideos) {
        for (Video video : trainingVideos) {
            double[] features = featureExtractor.extractFeatures(video);
            trainingFeatures.put(video.getId(), features);
            trainingLabels.put(video.getId(), video.getAssignedCategory());
        }
        System.out.println("Model trained with " + trainingVideos.size() + " videos");
    }
    
    public Map<Category, Double> predict(Video video) {
        double[] features = featureExtractor.extractFeatures(video);
        Map<Category, Double> scores = new HashMap<>();
        
        // Initialize scores
        for (Category category : Category.values()) {
            scores.put(category, 0.0);
        }
        
        if (trainingFeatures.isEmpty()) {
            // Simple keyword-based fallback
            String combined = (video.getTitle() + " " + video.getDescription()).toLowerCase();
            for (Category category : Category.values()) {
                double score = 0;
                for (String keyword : category.getKeywords()) {
                    if (combined.contains(keyword)) {
                        score += 0.2;
                    }
                }
                scores.put(category, Math.min(score, 1.0));
            }
        } else {
            // KNN-like approach
            for (Map.Entry<String, double[]> entry : trainingFeatures.entrySet()) {
                double similarity = cosineSimilarity(features, entry.getValue());
                Category category = trainingLabels.get(entry.getKey());
                scores.put(category, scores.get(category) + similarity);
            }
            
            // Normalize
            double total = scores.values().stream().mapToDouble(Double::doubleValue).sum();
            if (total > 0) {
                for (Category category : scores.keySet()) {
                    scores.put(category, scores.get(category) / total);
                }
            }
        }
        
        return scores;
    }
    
    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        
        if (normA == 0 || normB == 0) return 0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}