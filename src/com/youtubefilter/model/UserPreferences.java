package com.youtubefilter.model;

import java.util.HashSet;
import java.util.Set;

public class UserPreferences {
    private String userId;
    private Set<Category> selectedCategories;
    private boolean strictFiltering;
    private int maxResultsPerCategory;
    
    public UserPreferences(String userId) {
        this.userId = userId;
        this.selectedCategories = new HashSet<>();
        this.strictFiltering = true;
        this.maxResultsPerCategory = 5;
    }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Set<Category> getSelectedCategories() { return selectedCategories; }
    public void setSelectedCategories(Set<Category> selectedCategories) { 
        this.selectedCategories = selectedCategories; 
    }
    
    public void addCategory(Category category) {
        this.selectedCategories.add(category);
    }
    
    public void removeCategory(Category category) {
        this.selectedCategories.remove(category);
    }
    
    public boolean isStrictFiltering() { return strictFiltering; }
    public void setStrictFiltering(boolean strictFiltering) { 
        this.strictFiltering = strictFiltering; 
    }
    
    public int getMaxResultsPerCategory() { return maxResultsPerCategory; }
    public void setMaxResultsPerCategory(int maxResultsPerCategory) { 
        this.maxResultsPerCategory = maxResultsPerCategory; 
    }
    
    @Override
    public String toString() {
        return selectedCategories.toString();
    }
}