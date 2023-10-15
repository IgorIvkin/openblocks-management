package ru.openblocks.management.abac;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Abac {

    /**
     * Класс с правилами контроля доступа, в котором описан метод по применению правил
     * к конкретной ситуации.
     * @return класс с правилами контроля доступа
     */
    Class<? extends AccessRule> type();

    /**
     * Названия аргументов метода, значения которых нужно извлечь и передать в класс правила.
     *
     * @return названия аргументов метода, значения которых нужно извлечь и передать в класс правила
     */
    String[] arguments() default {};
}
