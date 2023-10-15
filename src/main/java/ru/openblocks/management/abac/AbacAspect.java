package ru.openblocks.management.abac;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import ru.openblocks.management.exception.NoUserRightsException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Аспектный класс, который обрабатывает права доступа к методам, помеченным
 * аннотацией по контролю доступа, основанному на аккаунтах (он же ABAC).
 */
@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AbacAspect {

    private final Map<Class<? extends AccessRule>, AccessRule> accessRules = new HashMap<>();

    @Autowired
    public AbacAspect(Set<AccessRule> rules) {
        rules.forEach(rule -> {
            final Class<? extends AccessRule> userClass = ClassUtils.getUserClass(rule).asSubclass(AccessRule.class);
            accessRules.put(userClass, rule);
        });
    }

    @Around("@annotation(abacAnnotation)")
    public Object aroundAbacMarkedMethod(ProceedingJoinPoint proceedingJoinPoint,
                                         Abac abacAnnotation) throws Throwable {

        final Class<? extends AccessRule> accessRuleType = abacAnnotation.type();
        final Set<String> argumentNames = Set.of(abacAnnotation.arguments());
        final AccessRule accessRule = accessRules.get(accessRuleType);

        if (accessRule != null) {
            final Map<String, Object> arguments = getArguments(argumentNames, proceedingJoinPoint);
            log.info("ABAC checking for {}", arguments);
            if (!accessRule.check(arguments)) {
                throw NoUserRightsException.notEnoughRights();
            }
        } else {
            throw new IllegalStateException("Cannot find access rule for class " + accessRuleType);
        }

        return proceedingJoinPoint.proceed();
    }

    private Map<String, Object> getArguments(Set<String> arguments, ProceedingJoinPoint proceedingJoinPoint) {

        final Map<String, Object> argumentsMap = new HashMap<>();
        final String[] argumentNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        final Object[] values = proceedingJoinPoint.getArgs();

        for (int i = 0; i < argumentNames.length; i++) {
            if (arguments.contains(argumentNames[i])) {
                argumentsMap.put(argumentNames[i], values[i]);
            }
        }

        if (argumentsMap.size() < arguments.size()) {
            throw new IllegalStateException("Cannot extract all the required params for ABAC, not all arguments " +
                    "presented - " + arguments);
        }

        return argumentsMap;
    }
}
