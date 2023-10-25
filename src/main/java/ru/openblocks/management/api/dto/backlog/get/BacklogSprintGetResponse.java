package ru.openblocks.management.api.dto.backlog.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacklogSprintGetResponse {

    private Long id;

    private String title;

}
