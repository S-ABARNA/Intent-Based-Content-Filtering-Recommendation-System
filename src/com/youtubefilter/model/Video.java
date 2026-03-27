package com.youtubefilter.model;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Video {
    private String id;
    private String title;
    private String description;
    private String channelTitle;
    private List<String> tags;
    private Map<Category, Double> categoryProbabilities;
    private Category assignedCategory;
    private boolean isEducational;
    private String thumbnailUrl;
    private String videoUrl;
    
    public Video(String id, String title, String description, String channelTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.channelTitle = channelTitle;
        this.tags = new ArrayList<>();
        this.categoryProbabilities = new HashMap<>();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getChannelTitle() { return channelTitle; }
    public void setChannelTitle(String channelTitle) { this.channelTitle = channelTitle; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public Map<Category, Double> getCategoryProbabilities() { return categoryProbabilities; }
    public void setCategoryProbabilities(Map<Category, Double> categoryProbabilities) { 
        this.categoryProbabilities = categoryProbabilities; 
    }
    
    public Category getAssignedCategory() { return assignedCategory; }
    public void setAssignedCategory(Category assignedCategory) { 
        this.assignedCategory = assignedCategory; 
    }
    
    public boolean isEducational() { return isEducational; }
    public void setEducational(boolean educational) { isEducational = educational; }
    
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    @Override
    public String toString() {
        return "Video{id='" + id + "', title='" + title + "', category=" + assignedCategory + "}";
    }
}