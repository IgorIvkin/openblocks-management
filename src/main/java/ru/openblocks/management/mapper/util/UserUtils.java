package ru.openblocks.management.mapper.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class UserUtils {

    public static String mapShortName(String name) {
        if (name != null) {
            return Arrays.stream(name.split(" "))
                    .filter(StringUtils::hasText)
                    .limit(2)
                    .map(item -> item.substring(0, 1).toUpperCase())
                    .collect(Collectors.joining(""));
        }
        return null;
    }
}
