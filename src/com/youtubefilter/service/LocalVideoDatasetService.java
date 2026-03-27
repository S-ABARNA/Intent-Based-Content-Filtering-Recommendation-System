package com.youtubefilter.service;

import com.youtubefilter.model.Video;
import com.youtubefilter.model.Category;
import java.util.*;
import java.io.*;

public class LocalVideoDatasetService {
    private List<Video> videoDataset;
    
    public LocalVideoDatasetService() {
        this.videoDataset = new ArrayList<>();
        createSampleDataset();
    }
    
    private void createSampleDataset() {
        // Science Videos
        videoDataset.add(createVideo("1", "The Water Cycle - Science Lesson", 
            "Learn about evaporation, condensation, and precipitation in this educational science video", 
            "ScienceChannel", Category.SCIENCE));
        
        videoDataset.add(createVideo("2", "Fun Science Experiments at Home", 
            "Easy and safe science experiments you can do at home with common materials", 
            "ScienceForKids", Category.SCIENCE));
        
        videoDataset.add(createVideo("3", "Understanding Gravity", 
            "A simple explanation of how gravity works for beginners", 
            "PhysicsZone", Category.SCIENCE));
        
        videoDataset.add(createVideo("4", "Solar System Exploration", 
            "Journey through our solar system and learn about planets", 
            "SpaceEducation", Category.SCIENCE));
        
        // Maths Videos
        videoDataset.add(createVideo("5", "Basic Algebra Tutorial", 
            "Learn how to solve simple algebraic equations step by step", 
            "MathGenius", Category.MATHS));
        
        videoDataset.add(createVideo("6", "Multiplication Tables Song", 
            "Fun way to learn multiplication tables through music", 
            "KidsMath", Category.MATHS));
        
        videoDataset.add(createVideo("7", "Geometry for Beginners", 
            "Introduction to shapes, angles, and basic geometry concepts", 
            "MathZone", Category.MATHS));
        
        videoDataset.add(createVideo("8", "Fractions Made Easy", 
            "Understanding fractions with visual examples", 
            "NumberCrunchers", Category.MATHS));
        
        // Story Videos
        videoDataset.add(createVideo("9", "The Tortoise and the Hare", 
            "Classic fable about slow and steady winning the race with valuable moral lessons", 
            "StoryTime", Category.STORY));
        
        videoDataset.add(createVideo("10", "Jungle Book Story", 
            "Adventures of Mowgli in the jungle - a beloved tale", 
            "TalesForKids", Category.STORY));
        
        videoDataset.add(createVideo("11", "Cinderella - Fairy Tale", 
            "The magical story of Cinderella and her glass slipper", 
            "FairyTales", Category.STORY));
        
        videoDataset.add(createVideo("12", "The Boy Who Cried Wolf", 
            "Important lesson about telling the truth", 
            "MoralStories", Category.STORY));
        
        // Fun Videos
        videoDataset.add(createVideo("13", "Funny Animal Compilation", 
            "Cute and funny moments with animals that will make you smile", 
            "FunVideos", Category.FUN));
        
        videoDataset.add(createVideo("14", "Fun Science Magic Tricks", 
            "Amazing tricks that look like magic but are explained by science", 
            "ScienceFun", Category.FUN));
        
        videoDataset.add(createVideo("15", "Fun Math Puzzles", 
            "Entertaining math puzzles and brain teasers", 
            "PuzzleTime", Category.FUN));
        
        videoDataset.add(createVideo("16", "Storytelling with Funny Voices", 
            "Classic stories told with hilarious character voices", 
            "FunTales", Category.FUN));
    }
    
    private Video createVideo(String id, String title, String description, 
                             String channel, Category category) {
        Video video = new Video(id, title, description, channel);
        video.setAssignedCategory(category);
        video.setEducational(true);
        video.setThumbnailUrl("local-thumbnail-" + id);
        video.setVideoUrl("local-video-" + id);
        
        // Add some tags
        List<String> tags = new ArrayList<>();
        tags.add(category.getDisplayName().toLowerCase());
        tags.add("educational");
        tags.add("learning");
        video.setTags(tags);
        
        return video;
    }
    
    public List<Video> getVideosByCategory(Category category, int limit) {
        List<Video> result = new ArrayList<>();
        for (Video video : videoDataset) {
            if (video.getAssignedCategory() == category) {
                result.add(video);
                if (result.size() >= limit) break;
            }
        }
        return result;
    }
    
    public List<Video> getAllVideos() {
        return new ArrayList<>(videoDataset);
    }
    
    public List<Video> searchVideos(String query, int maxResults) {
        List<Video> results = new ArrayList<>();
        String queryLower = query.toLowerCase();
        
        for (Video video : videoDataset) {
            if (video.getTitle().toLowerCase().contains(queryLower) ||
                video.getDescription().toLowerCase().contains(queryLower)) {
                results.add(video);
                if (results.size() >= maxResults) break;
            }
        }
        
        return results;
    }
}