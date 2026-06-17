package com.my.worldbuilder.common.mapping;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface EntityMapper<E, D> {

    E toEntity(D dto);

    List<E> toEntity(List<D> d);

    void updateEntity(@MappingTarget E e, D d);

}