package com.pigeon.utils;

import javax.validation.GroupSequence;

public class ValidGroup {

    // 新增使用(配合spring的@Validated功能分组使用)
    public interface Insert{}

    // 更新使用(配合spring的@Validated功能分组使用)
    public interface Update{}

    // 删除使用(配合spring的@Validated功能分组使用)
    public interface Delete{}

    // 查询使用(配合spring的@Validated功能分组使用)
    public interface Select{}

    // 属性必须有这两个分组的才验证(配合spring的@Validated功能分组使用)
    @GroupSequence({Insert.class, Update.class, Delete.class, Select.class})
    public interface All{}

//    @Null                           被注释的元素必须为 null
//    @NotNull                        被注释的元素必须不为 null
//    @AssertTrue                     被注释的元素必须为 true
//    @AssertFalse                    被注释的元素必须为 false
//    @Min(value)                     被注释的元素必须是一个数字，其值必须大于等于指定的最小值
//    @Max(value)                     被注释的元素必须是一个数字，其值必须小于等于指定的最大值
//    @DecimalMin(value)              被注释的元素必须是一个数字，其值必须大于等于指定的最小值
//    @DecimalMax(value)              被注释的元素必须是一个数字，其值必须小于等于指定的最大值
//    @Size(max=, min=)               被注释的元素的大小必须在指定的范围内
//    @Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内
//    @Past                           被注释的元素必须是一个过去的日期
//    @Future                         被注释的元素必须是一个将来的日期
//    @Pattern(regex=,flag=)           被注释的元素必须符合指定的正则表达式
//
//
//    Hibernate Validator提供的校验注解：
//    @NotBlank(message =)            验证字符串非null，且trim后长度必须大于0
//    @Email                          被注释的元素必须是电子邮箱地址
//    @Length(min=,max=)              被注释的字符串的大小必须在指定的范围内
//    @NotEmpty                       被注释的字符串的必须非空
//    @Range(min=,max=,message=)      被注释的元素必须在合适的范围内
//
//    @AssertFalse                    校验false
//    @AssertTrue                     校验true
//    @DecimalMax(value=,inclusive=)  小于等于value，
//    inclusive=true,是小于等于
//    @DecimalMin(value=,inclusive=)  与上类似
//    @Max(value=)                    小于等于value
//    @Min(value=)                    大于等于value
//    @NotNull                        检查Null
//    @Past                           检查日期
//    @Pattern(regex=,flag=)           正则
//    @Size(min=, max=)               字符串，集合，map限制大小
//    @Valid                          对po实体类进行校验

}
