package com.youtubefilter.gui;

import com.youtubefilter.model.*;
import com.youtubefilter.service.LocalVideoDatasetService;
import com.youtubefilter.ml.MLModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    private LocalVideoDatasetService datasetService;
    private MLModel mlModel;
    private UserPreferences currentUser;
    
    // GUI Components
    private JPanel categoryPanel;
    private JPanel recommendationPanel;
    private JLabel statusLabel;
    private JCheckBox[] categoryCheckboxes;
    private JButton filterButton;
    private JButton refreshButton;
    private JButton allVideosButton;
    private JScrollPane scrollPane;
    
    public MainFrame() {
        // Initialize services
        this.datasetService = new LocalVideoDatasetService();
        this.mlModel = new MLModel();
        this.currentUser = new UserPreferences("user123");
        
        // Train the model
        mlModel.train(datasetService.getAllVideos());
        
        // Setup frame
        setupFrame();
        
        // Create UI components
        createUI();
        
        // Load initial recommendations
        loadRecommendations();
    }
    
    private void setupFrame() {
        setTitle("YouTube Content Filter - ML Based Recommendation System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }
    
    private void createUI() {
        // Create menu bar
        createMenuBar();
        
        // Create top panel with categories
        createCategoryPanel();
        
        // Create center panel for recommendations
        createRecommendationPanel();
        
        // Create bottom status panel
        createStatusPanel();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(e -> loadRecommendations());
        viewMenu.add(refreshItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void createCategoryPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Select Your Preferred Categories");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Categories panel
        categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        categoryPanel.setBackground(new Color(240, 248, 255));
        
        // Create checkboxes for each category
        Category[] categories = Category.values();
        categoryCheckboxes = new JCheckBox[categories.length];
        
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            JCheckBox checkBox = new JCheckBox(getCategoryIcon(category) + " " + category.getDisplayName());
            checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
            checkBox.addActionListener(e -> updateCategorySelection());
            categoryCheckboxes[i] = checkBox;
            categoryPanel.add(checkBox);
        }
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        filterButton = new JButton("Get Recommendations");
        filterButton.setFont(new Font("Arial", Font.BOLD, 14));
        filterButton.setBackground(new Color(0, 153, 76));
        filterButton.setForeground(Color.WHITE);
        filterButton.addActionListener(e -> loadRecommendations());
        
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.addActionListener(e -> loadRecommendations());
        
        allVideosButton = new JButton("Show All Videos");
        allVideosButton.setFont(new Font("Arial", Font.PLAIN, 12));
        allVideosButton.addActionListener(e -> showAllVideos());
        
        buttonPanel.add(filterButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(allVideosButton);
        
        topPanel.add(categoryPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
    }
    
    private String getCategoryIcon(Category category) {
        switch (category) {
            case STORY: return "📖";
            case FUN: return "😊";
            case MATHS: return "🔢";
            case SCIENCE: return "🔬";
            default: return "📺";
        }
    }
    
    private void createRecommendationPanel() {
        recommendationPanel = new JPanel();
        recommendationPanel.setLayout(new BoxLayout(recommendationPanel, BoxLayout.Y_AXIS));
        recommendationPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(recommendationPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Recommended Videos"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createStatusPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));
        
        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel infoLabel = new JLabel(" ML Model: Active | " + datasetService.getAllVideos().size() + " videos in dataset");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(infoLabel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateCategorySelection() {
        currentUser.getSelectedCategories().clear();
        
        Category[] categories = Category.values();
        for (int i = 0; i < categoryCheckboxes.length; i++) {
            if (categoryCheckboxes[i].isSelected()) {
                currentUser.addCategory(categories[i]);
            }
        }
        
        statusLabel.setText(" Selected " + currentUser.getSelectedCategories().size() + " categories");
    }
    
    private void loadRecommendations() {
        if (currentUser.getSelectedCategories().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one category first!",
                "No Categories Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        statusLabel.setText(" Analyzing content with ML model...");
        
        // Clear previous recommendations
        recommendationPanel.removeAll();
        
        // Get videos for each selected category
        for (Category category : currentUser.getSelectedCategories()) {
            List<Video> videos = datasetService.getVideosByCategory(category, 5);
            
            // Category header
            JPanel categoryHeader = createCategoryHeader(category);
            recommendationPanel.add(categoryHeader);
            recommendationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            
            // Videos for this category
            for (Video video : videos) {
                // Apply ML prediction
                Map<Category, Double> predictions = mlModel.predict(video);
                video.setCategoryProbabilities(predictions);
                video.setAssignedCategory(getTopCategory(predictions));
                
                JPanel videoCard = createVideoCard(video, category);
                recommendationPanel.add(videoCard);
                recommendationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        
        recommendationPanel.revalidate();
        recommendationPanel.repaint();
        
        statusLabel.setText(" Showing recommendations based on your preferences");
    }
    
    private JPanel createCategoryHeader(Category category) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 242, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        JLabel titleLabel = new JLabel(getCategoryIcon(category) + " " + category.getDisplayName() + " Recommendations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 51, 102));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createVideoCard(Video video, Category category) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        // Left panel - Video icon
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(120, 90));
        
        JLabel thumbnailLabel = new JLabel("🎬", SwingConstants.CENTER);
        thumbnailLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        leftPanel.add(thumbnailLabel, BorderLayout.CENTER);
        
        // Center panel - Video info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel titleLabel = new JLabel(video.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel channelLabel = new JLabel("Channel: " + video.getChannelTitle());
        channelLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        channelLabel.setForeground(new Color(102, 102, 102));
        
        // ML Confidence
        double confidence = video.getCategoryProbabilities().getOrDefault(category, 0.0) * 100;
        JLabel confidenceLabel = new JLabel(String.format("ML Confidence: %.1f%%", confidence));
        confidenceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set color based on confidence
        if (confidence >= 70) {
            confidenceLabel.setForeground(new Color(0, 153, 76)); // Green
        } else if (confidence >= 40) {
            confidenceLabel.setForeground(new Color(255, 153, 0)); // Orange
        } else {
            confidenceLabel.setForeground(new Color(204, 0, 0)); // Red
        }
        
        // Keywords
        String keywords = getMatchingKeywords(video, category);
        JLabel keywordsLabel = new JLabel("Keywords: " + keywords);
        keywordsLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        keywordsLabel.setForeground(new Color(0, 102, 204));
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(channelLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(confidenceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(keywordsLabel);
        
        // Right panel - Action buttons
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        rightPanel.setBackground(Color.WHITE);
        
        JButton watchButton = new JButton("Watch");
        watchButton.setBackground(new Color(255, 0, 0));
        watchButton.setForeground(Color.WHITE);
        watchButton.addActionListener(e -> showVideoDialog(video));
        
        JButton detailsButton = new JButton("Details");
        detailsButton.addActionListener(e -> showVideoDetails(video));
        
        rightPanel.add(watchButton);
        rightPanel.add(detailsButton);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private String getMatchingKeywords(Video video, Category category) {
        String text = (video.getTitle() + " " + video.getDescription()).toLowerCase();
        List<String> matches = new ArrayList<>();
        
        for (String keyword : category.getKeywords()) {
            if (text.contains(keyword) && matches.size() < 3) {
                matches.add(keyword);
            }
        }
        
        return matches.isEmpty() ? "No specific keywords" : String.join(", ", matches);
    }
    
    private void showVideoDialog(Video video) {
        JDialog dialog = new JDialog(this, "Watch Video", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🎬 " + video.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setText(
            "Channel: " + video.getChannelTitle() + "\n\n" +
            "Description: " + video.getDescription() + "\n\n" +
            "Category: " + video.getAssignedCategory().getDisplayName() + "\n" +
            "ML Confidence: " + 
            String.format("%.1f%%", video.getCategoryProbabilities()
                .getOrDefault(video.getAssignedCategory(), 0.0) * 100)
        );
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showVideoDetails(Video video) {
        String details = String.format(
            "Title: %s\n\nChannel: %s\n\nDescription: %s\n\nCategory: %s\n\nML Predictions:\n%s",
            video.getTitle(),
            video.getChannelTitle(),
            video.getDescription(),
            video.getAssignedCategory().getDisplayName(),
            formatPredictions(video.getCategoryProbabilities())
        );
        
        JOptionPane.showMessageDialog(this,
            details,
            "Video Details",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String formatPredictions(Map<Category, Double> predictions) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Category, Double> entry : predictions.entrySet()) {
            sb.append(String.format("   %s: %.1f%%\n", 
                entry.getKey().getDisplayName(), 
                entry.getValue() * 100));
        }
        return sb.toString();
    }
    
    private void showAllVideos() {
        JDialog dialog = new JDialog(this, "All Videos", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Video> allVideos = datasetService.getAllVideos();
        
        for (Video video : allVideos) {
            listModel.addElement(video.getTitle() + " [" + video.getAssignedCategory().getDisplayName() + "]");
        }
        
        JList<String> videoList = new JList<>(listModel);
        videoList.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(videoList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Video List"));
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "YouTube Content Filter\n" +
            "ML-Based Content Control System\n\n" +
            "Version: 1.0\n" +
            "Features:\n" +
            "• ML-based content classification\n" +
            "• Category-based filtering\n" +
            "• Confidence scoring\n" +
            "• 16 sample educational videos\n\n" +
            "Created with Java Swing",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Category getTopCategory(Map<Category, Double> predictions) {
        return predictions.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(Category.SCIENCE);
    }
}