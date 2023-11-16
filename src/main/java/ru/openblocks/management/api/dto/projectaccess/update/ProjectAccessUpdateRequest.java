package ru.openblocks.management.api.dto.projectaccess.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAccessUpdateRequest {

    @NotNull
    private Long userId;

    @NotNull
    @JsonProperty("projectAdmin")
    private Boolean projectAdmin;
}
