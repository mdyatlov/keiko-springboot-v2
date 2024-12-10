package com.theodo.albeniz.mappers;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.model.TuneEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TuneMapper {
  Tune from(TuneEntity proofFileRequestDto);

  TuneEntity from(Tune tune);
}