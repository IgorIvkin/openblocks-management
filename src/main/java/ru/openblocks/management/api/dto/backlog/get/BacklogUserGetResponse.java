package ru.openblocks.management.api.dto.backlog.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacklogUserGetResponse {

    private Long id;

    private String shortName;

    private String name;
}
