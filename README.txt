============================================
YouTube Content Filter System
ML-Based Content Control
============================================

DESCRIPTION:
A Java-based content filtering system that uses Machine Learning 
to categorize and filter YouTube-style educational content based 
on user preferences.

FEATURES:
- ML-based content classification
- Category selection (Story, Fun, Maths, Science)
- Smart filtering based on user preferences
- Confidence scoring for recommendations
- 16 pre-loaded educational videos
- No API key required (works offline)

REQUIREMENTS:
- Java 8 or higher installed
- Command line/terminal access

HOW TO RUN:

WINDOWS:
1. Open Command Prompt in this folder
2. Type: run.bat
3. Press Enter

MAC/LINUX:
1. Open Terminal in this folder
2. Type: chmod +x run.sh
3. Type: ./run.sh
4. Press Enter

USAGE:
1. Select categories you're interested in
2. View filtered content based on your choices
3. See ML confidence scores for each video
4. View all available videos in the dataset

CATEGORIES:
- Story: Fables, tales, narratives, moral stories
- Fun: Entertainment, comedy, fun activities
- Maths: Algebra, geometry, numbers, tutorials
- Science: Experiments, physics, chemistry, biology

HOW IT WORKS:
1. Extracts features from video titles and descriptions
2. Uses ML algorithm to classify content
3. Matches with user category preferences
4. Ranks and displays relevant content
5. Shows confidence scores for transparency

TROUBLESHOOTING:
- If "javac" not found: Install Java JDK
- If compilation fails: Check Java version
- If nothing happens: Ensure you're in correct directory

FUTURE ENHANCEMENTS:
- Add real YouTube API integration
- More training data for better ML
- GUI interface
- User feedback loop
- More categories

Enjoy your filtered educational content!