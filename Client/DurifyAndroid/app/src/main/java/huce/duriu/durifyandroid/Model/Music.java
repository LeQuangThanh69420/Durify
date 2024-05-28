package huce.duriu.durifyandroid.Model;

import java.util.Objects;

public class Music {
    private int musicId;
    private String musicName;
    private String musicURL;
    private String musicImageURL;
    private String musicArtist;
    private String musicNation;

    public Music(String musicName, String musicURL) {
        this.musicName = musicName;
        this.musicURL = musicURL;
        this.musicImageURL = "";
        this.musicArtist = "";
        this.musicNation = "";
    }
    public Music(int musicId, String musicName, String musicURL, String musicImageURL, String musicArtist, String musicNation) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicURL = musicURL;
        this.musicImageURL = musicImageURL;
        this.musicArtist = musicArtist;
        this.musicNation = musicNation;
    }

    public int getMusicId() { return musicId; }

    public String getMusicName() { return musicName; }

    public String getMusicURL() {
        return musicURL;
    }

    public String getMusicImageURL() {
        return musicImageURL;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public String getMusicNation() {
        return musicNation;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public void setMusicImageURL(String musicImageURL) {
        this.musicImageURL = musicImageURL;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public void setMusicNation(String musicNation) { this.musicNation = musicNation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return Objects.equals(musicName, music.musicName) &&
                Objects.equals(musicURL, music.musicURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicName, musicURL);
    }
}
