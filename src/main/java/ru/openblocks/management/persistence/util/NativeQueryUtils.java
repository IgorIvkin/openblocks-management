package ru.openblocks.management.persistence.util;

import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.model.task.TaskType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Supplier;

@UtilityClass
public class NativeQueryUtils {

    /**
     * Sets parameter to query by its given param name and supplied value.
     * Value is inside of supplier function here because sometimes it is calculated by some algorithm,
     * and it is convenient to calculate it lazily, in case if it's presented only.
     *
     * @param query     SQL native query
     * @param paramName name of parameter
     * @param value     value supplier function
     */
    public static void setParameterIfPresent(Query query, String paramName, Supplier<Object> value) {
        if (query.getParameters().stream().anyMatch(param -> param.getName().equals(paramName))) {
            query.setParameter(paramName, value.get());
        }
    }

    public static String mapString(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof String strValue) {
            return strValue;
        }
        return null;
    }

    public static Long mapLong(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof Long longValue) {
            return longValue;
        }
        return null;
    }

    public static Integer mapInteger(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof Integer intValue) {
            return intValue;
        }
        return null;
    }

    public static LocalDate mapLocalDate(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        return null;
    }

    public static TaskStatus mapStatus(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof Long statusValue) {
            return TaskStatus.of(statusValue);
        }
        return null;
    }

    public static TaskType mapType(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof Long typeValue) {
            return TaskType.of(typeValue);
        }
        return null;
    }

    public static TaskPriority mapPriority(String name, Tuple tuple) {
        final Object tupleValue = tuple.get(name);
        if (tupleValue instanceof Long priorityValue) {
            return TaskPriority.of(priorityValue);
        }
        return null;
    }
}
