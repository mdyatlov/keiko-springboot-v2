package com.theodo.albeniz.mappers;

import org.mapstruct.Mapper;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.model.TuneEntity;

@Mapper(componentModel = "spring")
public interface TuneMapper {
  Tune from(TuneEntity proofFileRequestDto);
  TuneEntity from(Tune tune);
}