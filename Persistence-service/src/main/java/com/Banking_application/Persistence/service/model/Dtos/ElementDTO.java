package com.Banking_application.Persistence.service.model.Dtos;

import java.util.Map;

public record ElementDTO(String type,
                         Map<String, Object> data,
                         Double x,
                         Double y,
                         Double width,
                         Double height) {
}
