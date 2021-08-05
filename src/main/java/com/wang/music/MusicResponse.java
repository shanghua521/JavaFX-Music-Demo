package com.wang.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicResponse extends BaseResult {

    private Long curTime;
    private String profileId;
    private String reqId;
    private MusicData data;
    private String tId;

}
