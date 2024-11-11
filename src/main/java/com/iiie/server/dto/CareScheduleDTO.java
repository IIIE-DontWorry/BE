package com.iiie.server.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class CareScheduleDTO {

    private Long id;
    private String description;
    private LocalDateTime activityAt;

    @Getter
    @Setter
    public static class CareScheduleRequest {
        private List<String> careSchedulesDescription;
    }
}
