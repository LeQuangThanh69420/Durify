package huce.duriu.durifyandroid.Model;

public class Music {
    private int musicId;
    private String musicName;
    private String musicURL;
    private String musicImageURL;
    private String musicArtist;
    private String musicNation;

    public int getMusicId() {
        return musicId;
    }

    public String getMusicName() {
        return musicName;
    }

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

    public void setMusicNation(String musicNation) {
        this.musicNation = musicNation;
    }
}
