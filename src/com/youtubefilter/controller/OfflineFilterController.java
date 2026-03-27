package com.youtubefilter.controller;

import com.youtubefilter.model.*;
import com.youtubefilter.service.LocalVideoDatasetService;
import com.youtubefilter.ml.MLModel;
import java.util.*;

public class OfflineFilterController {
    private LocalVideoDatasetService datasetService;
    private MLModel mlModel;
    private UserPreferences currentUser;
    private Scanner scanner;
    
    public OfflineFilterController() {
        this.datasetService = new LocalVideoDatasetService();
        this.mlModel = new MLModel();
        this.scanner = new Scanner(System.in);
        this.currentUser = new UserPreferences("user123");
        
        // Train the model with sample data
        mlModel.train(datasetService.getAllVideos());
    }
    
    public void start() {
        while (true) {
            clearScreen();
            printHeader();
            printMainMenu();
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    showCategorySelection();
                    break;
                case "2":
                    if (currentUser.getSelectedCategories().isEmpty()) {
                        System.out.println("\n⚠ Please select categories first!");
                        waitForEnter();
                    } else {
                        showFilteredContent();
                    }
                    break;
                case "3":
                    showAllVideos();
                    break;
                case "4":
                    System.out.println("\nThank you for using YouTube Content Filter!");
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
                    waitForEnter();
            }
        }
    }
    
    private void printHeader() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║        YouTube Content Filter System          ║");
        System.out.println("║              ML-Based Content Control         ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
    
    private void printMainMenu() {
        System.out.println("\n📋 MAIN MENU");
        System.out.println("═══════════════");
        System.out.println("Current selections: " + currentUser.getSelectedCategories());
        System.out.println("\n1. 🎯 Select Categories");
        System.out.println("2. 🔍 View Filtered Content");
        System.out.println("3. 📚 View All Videos");
        System.out.println("4. 🚪 Exit");
        System.out.print("\nYour choice: ");
    }
    
    private void showCategorySelection() {
        while (true) {
            clearScreen();
            System.out.println("\n🎯 SELECT CATEGORIES");
            System.out.println("════════════════════");
            System.out.println("Current selections: " + currentUser.getSelectedCategories());
            System.out.println("\nAvailable categories:");
            System.out.println("1. 📖 Story");
            System.out.println("2. 😊 Fun");
            System.out.println("3. 🔢 Maths");
            System.out.println("4. 🔬 Science");
            System.out.println("5. ❌ Clear all");
            System.out.println("0. ↩ Back to main menu");
            
            System.out.print("\nEnter category number: ");
            String input = scanner.nextLine();
            
            switch (input) {
                case "1": toggleCategory(Category.STORY); break;
                case "2": toggleCategory(Category.FUN); break;
                case "3": toggleCategory(Category.MATHS); break;
                case "4": toggleCategory(Category.SCIENCE); break;
                case "5": 
                    currentUser.getSelectedCategories().clear();
                    System.out.println("✅ All categories cleared!");
                    waitForEnter();
                    break;
                case "0": return;
                default:
                    System.out.println("❌ Invalid choice!");
                    waitForEnter();
            }
        }
    }
    
    private void toggleCategory(Category category) {
        if (currentUser.getSelectedCategories().contains(category)) {
            currentUser.removeCategory(category);
            System.out.println("❌ Removed: " + category.getDisplayName());
        } else {
            currentUser.addCategory(category);
            System.out.println("✅ Added: " + category.getDisplayName());
        }
        waitForEnter();
    }
    
    private void showFilteredContent() {
        clearScreen();
        System.out.println("\n🔍 FILTERED CONTENT");
        System.out.println("═══════════════════");
        System.out.println("Selected categories: " + currentUser.getSelectedCategories());
        System.out.println("\nFetching and analyzing videos...\n");
        
        // Get videos for selected categories
        Map<Category, List<Video>> videosByCategory = new HashMap<>();
        
        for (Category category : currentUser.getSelectedCategories()) {
            List<Video> videos = datasetService.getVideosByCategory(category, 5);
            videosByCategory.put(category, videos);
        }
        
        // Apply ML predictions
        for (List<Video> videos : videosByCategory.values()) {
            for (Video video : videos) {
                Map<Category, Double> predictions = mlModel.predict(video);
                video.setCategoryProbabilities(predictions);
                
                // Set the predicted category
                Category predicted = getTopCategory(predictions);
                video.setAssignedCategory(predicted);
            }
        }
        
        // Display results
        boolean hasContent = false;
        for (Category category : currentUser.getSelectedCategories()) {
            List<Video> videos = videosByCategory.get(category);
            
            if (videos != null && !videos.isEmpty()) {
                hasContent = true;
                System.out.println("\n─── " + category.getDisplayName() + 
                    " (" + videos.size() + " videos) ───");
                
                for (int i = 0; i < videos.size(); i++) {
                    Video video = videos.get(i);
                    System.out.println("\n" + (i + 1) + ". 📹 " + video.getTitle());
                    System.out.println("   📺 Channel: " + video.getChannelTitle());
                    
                    // Show confidence
                    double confidence = video.getCategoryProbabilities()
                        .getOrDefault(category, 0.0) * 100;
                    System.out.printf("   🎯 ML Confidence: %.1f%%\n", confidence);
                    
                    // Show matching keywords
                    showMatchingKeywords(video, category);
                }
            }
        }
        
        if (!hasContent) {
            System.out.println("No videos found for selected categories.");
        }
        
        System.out.println("\n" + "═".repeat(50));
        waitForEnter();
    }
    
    private void showMatchingKeywords(Video video, Category category) {
        String text = (video.getTitle() + " " + video.getDescription()).toLowerCase();
        List<String> matches = new ArrayList<>();
        
        for (String keyword : category.getKeywords()) {
            if (text.contains(keyword)) {
                matches.add(keyword);
            }
        }
        
        if (!matches.isEmpty()) {
            System.out.println("   🔑 Keywords: " + String.join(", ", matches));
        }
    }
    
    private void showAllVideos() {
        clearScreen();
        System.out.println("\n📚 ALL VIDEOS IN DATASET");
        System.out.println("════════════════════════");
        
        List<Video> allVideos = datasetService.getAllVideos();
        Map<Category, List<Video>> byCategory = new HashMap<>();
        
        // Group by category
        for (Video video : allVideos) {
            byCategory.computeIfAbsent(video.getAssignedCategory(), 
                k -> new ArrayList<>()).add(video);
        }
        
        // Display all categories
        for (Category category : Category.values()) {
            List<Video> videos = byCategory.get(category);
            if (videos != null && !videos.isEmpty()) {
                System.out.println("\n─── " + category.getDisplayName() + 
                    " (" + videos.size() + " videos) ───");
                
                for (int i = 0; i < videos.size(); i++) {
                    Video video = videos.get(i);
                    System.out.println("   " + (i + 1) + ". " + video.getTitle());
                }
            }
        }
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("Total videos: " + allVideos.size());
        waitForEnter();
    }
    
    private Category getTopCategory(Map<Category, Double> predictions) {
        return predictions.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(Category.SCIENCE);
    }
    
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clear screen fails, just print new lines
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
    
    private void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}