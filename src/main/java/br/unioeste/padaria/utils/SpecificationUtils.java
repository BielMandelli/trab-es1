package br.unioeste.padaria.utils;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class SpecificationUtils {

    public static <T> Specification<T> containsIgnoreCase(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            Path<?> path = root;

            for (String fieldPart : field.split("\\.")) {
                path = path.get(fieldPart);
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(path.as(String.class)),
                    "%" + value.toLowerCase(Locale.ROOT) + "%"
            );
        };
    }

}
