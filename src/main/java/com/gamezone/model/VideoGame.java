package com.gamezone.model;

    /**
     * Represents a video game sold by GameZone Unicesar.
     * In addition to the common product attributes, a video game is
     * characterized by the platform it runs on, its genre, and its
     * recommended age rating.
     */
public class VideoGame extends Product {

    private String platform;
    private String genre;
    private String ageRating;

        /**
         * Represents a video game sold by GameZone Unicesar.
         * In addition to the common product attributes, a video game is
         * characterized by the platform it runs on, its genre, and its
         * recommended age rating.
         */
    public VideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating) {
        super(id, title, price, stock);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }

        /**
         * Returns the platform this video game is developed for.
         *
         * @return the platform
         */
    public String getPlatform() {
        return platform;
    }

        /**
         * Updates the platform this video game is developed for.
         *
         * @param platform the new platform
         */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

        /**
         * Returns the genre of this video game.
         *
         * @return the genre
         */
    public String getGenre() {
        return genre;
    }

        /**
         * Updates the genre of this video game.
         *
         * @param genre the new genre
         */
    public void setGenre(String genre) {
        this.genre = genre;
    }

        /**
         * Returns the recommended age rating of this video game.
         *
         * @return the age rating
         */
    public String getAgeRating() {
        return ageRating;
    }

        /**
         * Updates the recommended age rating of this video game.
         *
         * @param ageRating the new age rating
         */
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

        /**
         * Builds a complete description of the video game, integrating
         * its platform, genre, and age rating with the common product
         * information.
         *
         * @return a human-readable description of the video game
         */
    @Override
    public String getDescription() {
        return "Video Game: " + getTitle()
                + " | Platform: " + platform
                + " | Genre: " + genre
                + " | Age Rating: " + ageRating
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }
}
