package com.k8smaster.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("custom_config")
public class CustomConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String namespace;
    @TableField("resource_type")
    private String resourceType;
    @TableField("yaml_content")
    private String yamlContent;
    private String description;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
