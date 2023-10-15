package ru.openblocks.management.api.dto.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdResponse<T> {

    private T id;

    /**
     * Returns an instance of IdResponse based on type and value passed to method.
     *
     * @param value value of ID
     * @param <T>   type of ID
     * @return identifier response
     */
    public static <T> IdResponse<T> of(T value) {
        return IdResponse.<T>builder()
                .id(value)
                .build();
    }
}

