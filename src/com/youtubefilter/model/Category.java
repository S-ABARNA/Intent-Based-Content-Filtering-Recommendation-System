package com.youtubefilter.model;

public enum Category {
    STORY("Story", new String[]{"story", "tale", "fiction", "novel", "narrative", "book", "reading", "adventure", "fairy", "moral"}),
    FUN("Fun", new String[]{"fun", "entertainment", "comedy", "joke", "funny", "humor", "game", "play", "amusing", "laughter"}),
    MATHS("Maths", new String[]{"math", "mathematics", "algebra", "calculus", "geometry", "numbers", "equation", "formula", "count", "addition"}),
    SCIENCE("Science", new String[]{"science", "physics", "chemistry", "biology", "experiment", "lab", "scientific", "nature", "space", "discovery"});
    
    private String displayName;
    private String[] keywords;
    
    Category(String displayName, String[] keywords) {
        this.displayName = displayName;
        this.keywords = keywords;
    }
    
    public String getDisplayName() { return displayName; }
    public String[] getKeywords() { return keywords; }
}