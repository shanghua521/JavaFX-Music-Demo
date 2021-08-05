package com.wang.music;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class MusicApplication extends Application {

    private final ObservableList<MusicItem> data = FXCollections.observableArrayList();
    private final TableView<MusicItem> musicTableView = new TableView<>();
    private volatile MediaPlayer mediaPlayer;
    private int musicIndex = -1;

    @Override
    public void start(Stage primaryStage) {
        AnchorPane anchorPane = new AnchorPane();
        Button playButton = new Button("播放");
        Button stopButton = new Button("暂停");
        Button searchButton = new Button("搜索");
        Button nextPageButton = new Button("下一页");
        TextField textField = new TextField();

        musicTableView.setPrefHeight(700);

        anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> musicTableView.setPrefWidth((Double) newValue));

        TableColumn<MusicItem, String> song = new TableColumn<>("歌曲") {
            {
                prefWidthProperty().bind(musicTableView.widthProperty().multiply(.3));
            }
        };
        song.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MusicItem, String> artist = new TableColumn<>("歌手") {
            {
                prefWidthProperty().bind(musicTableView.widthProperty().multiply(.3));
            }
        };
        artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        TableColumn<MusicItem, String> album = new TableColumn<>("专辑") {
            {
                prefWidthProperty().bind(musicTableView.widthProperty().multiply(.3));
            }
        };
        album.setCellValueFactory(new PropertyValueFactory<>("album"));
        TableColumn<MusicItem, String> songTimeMinutes = new TableColumn<>("时长") {
            {
                prefWidthProperty().bind(musicTableView.widthProperty().multiply(.09));
            }
        };
        songTimeMinutes.setCellValueFactory(new PropertyValueFactory<>("songTimeMinutes"));

        //noinspection unchecked
        musicTableView.getColumns().addAll(song, artist, album, songTimeMinutes);

        HBox hBox = new HBox(5);

        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setMax(100);

        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> Optional.ofNullable(mediaPlayer).ifPresent(_mediaPlayer -> _mediaPlayer.setVolume(newValue.floatValue())));

        hBox.setAlignment(Pos.CENTER);
        Group volume = new Group();
        volume.getChildren().addAll(new Label("音量"), scrollBar);
        volume.setVisible(false);
        hBox.getChildren().addAll(searchButton, textField, nextPageButton, playButton, stopButton, volume);
        HBox.setMargin(volume, new Insets(0, 0, 0, 20));

        KuWoMusic kuWoMusic = new KuWoMusic();
        searchButton.setOnAction(event -> {
            Optional<MusicResponse> musicResponse = kuWoMusic.search(textField.getText());
            musicResponse.ifPresent(this::printMusicItems);
        });

        nextPageButton.setOnAction(event -> {
            if (data.size() > 0) {
                Optional<MusicResponse> musicResponse = kuWoMusic.nextPage();
                musicResponse.ifPresent(this::printMusicItems);
            }
        });

        playButton.setOnAction(event -> {
            MusicItem selectedItem = musicTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) kuWoMusic.getMusicPlayUrl(selectedItem.getRid()).ifPresent(this::playMusic);
        });

        stopButton.setOnAction(event -> Optional.ofNullable(mediaPlayer).ifPresent(ig -> {
            MediaPlayer.Status currentStatus = mediaPlayer.getStatus();
            if (currentStatus == MediaPlayer.Status.PLAYING) {
                stopButton.setText("继续");
                mediaPlayer.pause();
            } else if (currentStatus == MediaPlayer.Status.PAUSED || currentStatus == MediaPlayer.Status.STOPPED) {
                stopButton.setText("暂停");
                mediaPlayer.play();
            }
        }));

        anchorPane.getChildren().addAll(hBox, musicTableView);

        AnchorPane.setTopAnchor(hBox, 20.0);
        AnchorPane.setLeftAnchor(hBox, 20.0);

        AnchorPane.setTopAnchor(musicTableView, 60.0);

        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    public void printMusicItems(MusicResponse response) {
        Optional.ofNullable(response.getData()).ifPresent(musicData -> {
            List<MusicItem> musicItems = musicData.getList();
            data.setAll(musicItems);
            musicTableView.setItems(data);
        });
    }

    public void playMusic(MusicUrl musicUrl) {
        int selectedIndex = musicTableView.getSelectionModel().getSelectedIndex();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (musicIndex != selectedIndex) {
            musicIndex = selectedIndex;
            Media media = new Media(musicUrl.getUrl());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}