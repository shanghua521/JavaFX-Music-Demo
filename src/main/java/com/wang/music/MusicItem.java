package com.wang.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicItem {

    // 专辑
    private String album;
    // 专辑 Id
    private Integer albumid;
    // 专辑图片
    private String albumpic;
    // 演唱人员/艺术家们
    private String artist;
    // 演出人员 Id
    private Integer artistid;
    // 弹幕
    private String barrage;
    private String content_type;
    // 期间，不晓得什么意思
    private Integer duration;
    // 是否有无损
    private Boolean hasLossless;
    // 是否有 MV
    private Integer hasmv;
    // 是否收费
    private Boolean isListenFee;
    // 是明星吗
    private Integer isstar;
    // 音乐 ID
    private String musicrid;

    // 支付信息
    private MvPayInfo mvpayinfo;
    // 名称
    private String name;
    // 是否在线
    private Integer online;
    // 原曲类型
    private Integer originalsongtype;
    // 翻译过来是支付，不晓得干啥用的
    private String pay;
    // 支付信息
    private PayInfo payInfo;

    // 图片
    private String pic;
    // 小图
    private String pic120;
    // 发布日期
    private String releaseDate;

    private Integer rid;
    private String score100;
    // 歌曲时间
    private String songTimeMinutes;
    private Integer track;
}

@ToString
@Getter
@Setter
class MvPayInfo {
    private Integer down;
    private Integer play;
    private Integer vid;
}

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class PayInfo {
    // 是否无法下载
    private Integer cannotDownload;
    // 是否在线支付
    private Integer cannotOnlinePlay;

    private String down;
    private String download;
//    private FeeType feeType;
    // 限免
    private Integer limitfree;
    // 本地加密
    private String local_encrypt;
    private String play;
    private Integer refrain_end;
    private Integer refrain_start;

    @ToString
    @Getter
    @Setter
    public static class FeeType {
        private String feeType;
        private String vip;
    }
}