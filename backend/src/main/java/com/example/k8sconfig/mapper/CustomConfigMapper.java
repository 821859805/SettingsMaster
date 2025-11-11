package com.example.k8sconfig.mapper;

import com.example.k8sconfig.entity.CustomConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CustomConfigMapper {
    void insert(CustomConfig config);

    void update(CustomConfig config);

    void delete(@Param("id") Long id);

    Optional<CustomConfig> findById(@Param("id") Long id);

    List<CustomConfig> search(@Param("keyword") String keyword);
}
