module sample {

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;
    requires javafx.web;
    requires javafx.media;

    requires kotlin.stdlib;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires okhttp3;

    requires com.jfoenix;
    requires static lombok;

    opens com.wang.music to com.fasterxml.jackson.databind;

    exports com.wang.music;
}