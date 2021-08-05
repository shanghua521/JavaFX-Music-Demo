package com.wang.music;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class KuWoMusic {

    private final Headers _headers = new Headers.Builder()
            .add("Host", "www.kuwo.cn")
            .add("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:90.0) Gecko/20100101 Firefox/90.0")
            .add("Accept", "application/json, text/plain, */*")
            .add("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
            .add("csrf", "AEAVIVQ46T9")
            .add("Cookie", "gid=b839c08b-91ff-4f76-ac69-c8159ea03761; kw_token=AEAVIVQ46T9")
            .add("Pragma", "no-cache")
            .add("Cache-Control", "no-cache").build();

    private final OkHttpClient client = new OkHttpClient();

    private int pageNumber = 1;
    private String musicName;

    public static void main(String[] args) {
        KuWoMusic kuWoMusic = new KuWoMusic();
        Optional<MusicResponse> musicResponse = kuWoMusic.search("九儿");
        musicResponse.flatMap(musicResponse1 -> Optional.ofNullable(musicResponse1.getData())).ifPresent(musicData -> {
            Integer rid = musicData.getList().get(5).getRid();
            Optional<MusicUrl> musicPlayUrl = kuWoMusic.getMusicPlayUrl(rid);
            musicPlayUrl.ifPresent(System.out::println);
        });
    }

    public Optional<MusicResponse> search(String musicName) {
        return search(musicName, pageNumber);
    }

    public Optional<MusicResponse> search(String musicName, Integer pageNumber) {
        this.musicName = musicName;
        this.pageNumber = pageNumber;

        var url = String.format("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=%s&pn=%s&rn=30&httpsStatus=1&reqId=80895b40-f4dc-11eb-832e-9da963afd885", musicName, pageNumber);
        var headers = new Headers.Builder()
                .addAll(_headers)
                .add("Referer", String.format("http://www.kuwo.cn/search/list?key=%s", URLEncoder.encode(musicName, StandardCharsets.UTF_8)))
                .build();

        var request = new Request.Builder()
                .headers(headers)
                .url(url)
                .build();

        try (var response = client.newCall(request).execute()) {
            String result = Objects.requireNonNull(response.body()).string();
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(result, MusicResponse.class));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<MusicUrl> getMusicPlayUrl(Integer rid) {
        String url = String.format("http://www.kuwo.cn/url?format=mp3&rid=%d&response=url&type=convert_url3&br=128kmp3&from=web", rid);

        var headers = new Headers.Builder()
                .addAll(_headers)
                .add("Referer", String.format("http://www.kuwo.cn/play_detail/%d", rid))
                .build();

        var request = new Request.Builder()
                .headers(headers)
                .url(url)
                .build();

        try (var response = client.newCall(request).execute()) {
            String result = Objects.requireNonNull(response.body()).string();
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(result, MusicUrl.class));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<MusicResponse> nextPage() {
        return search(musicName, ++pageNumber);
    }
}
