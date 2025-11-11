package com.k8smaster.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k8smaster.domain.entity.CustomConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomConfigMapper extends BaseMapper<CustomConfig> {
}
