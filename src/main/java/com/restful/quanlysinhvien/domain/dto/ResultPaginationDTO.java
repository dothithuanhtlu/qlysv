package com.restful.quanlysinhvien.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;
}
