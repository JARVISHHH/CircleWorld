package engine.uikit;

import engine.support.FXFrontEnd;
import engine.support.Vec2d;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

public class Video extends UIElement{

    protected MediaView mediaView;
    protected MediaPlayer mediaPlayer;
    protected FXFrontEnd app;

    protected boolean autoPlay = false;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    public void setApp(FXFrontEnd app) {
        this.app = app;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void dispose() {
        if(mediaPlayer != null) mediaPlayer.dispose();
    }

    public void play() {
        if(mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        }
    }

    public boolean isFinished() {
        if(mediaPlayer != null && mediaPlayer.getStopTime().equals(mediaPlayer.getCurrentTime()))
            return true;
        return false;
    }

    @Override
    public void onStartup() {
        if(mediaPlayer != null) mediaPlayer.play();
        if(mediaView != null) app.addMediaView(mediaView);
    }

    @Override
    public void onShutdown() {
        if(mediaPlayer != null) mediaPlayer.stop();
        if(mediaView != null) app.removeMediaView(mediaView);
    }

    public Video(String path, Vec2d position, Vec2d size) {
        super();
        this.position = position;
        this.size = size;

        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(size.y);
        mediaView.setFitWidth(size.x);

        mediaView.setX(position.x);
        mediaView.setY(position.y);
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if(!active && mediaPlayer != null && mediaView != null) {
            mediaPlayer.stop();
            app.removeMediaView(mediaView);
        } else if (mediaPlayer != null && mediaView != null){
            mediaPlayer.play();
            app.addMediaView(mediaView);
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        if(mediaView != null) {
            mediaView.setFitHeight(size.y);
            mediaView.setFitWidth(size.x);
            mediaView.setX(position.x);
            mediaView.setY(position.y);
        }
        if(autoPlay && isFinished())
            play();
    }

}
