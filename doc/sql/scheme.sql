DROP TABLE IF EXISTS system_user;
CREATE TABLE system_user(
                            `object_id` VARCHAR(32) NOT NULL  COMMENT 'base-ID;主键' ,
                            `is_deleted` VARCHAR(255)   COMMENT 'base-是否删除;0未删除 1已删除' ,
                            `revision` INT   COMMENT 'base-乐观锁;乐观锁' ,
                            `created_by` VARCHAR(32)   COMMENT 'base-创建人;创建人' ,
                            `created_time` DATETIME   COMMENT 'base-创建时间;创建时间' ,
                            `updated_by` VARCHAR(32)   COMMENT 'base-更新人;更新人' ,
                            `updated_time` DATETIME   COMMENT 'base-更新时间;更新时间' ,
                            `username` VARCHAR(255)   COMMENT '用户名;用户名' unique ,
                            `password` VARCHAR(255)   COMMENT '密码;密码' ,
                            `telephone` VARCHAR(255)   COMMENT '电话;电话' unique ,
                            `email` VARCHAR(255)   COMMENT '邮箱;邮箱' unique ,
                            `location` VARCHAR(255)   COMMENT '地址;location' ,
                            PRIMARY KEY (object_id)
)  COMMENT = '用户表';

DROP TABLE IF EXISTS system_role;
CREATE TABLE system_role(
                            `object_id` VARCHAR(32) NOT NULL  COMMENT 'base-ID;主键' ,
                            `is_deleted` VARCHAR(255)   COMMENT 'base-是否删除;0未删除 1已删除' ,
                            `revision` INT   COMMENT 'base-乐观锁;乐观锁' ,
                            `created_by` VARCHAR(32)   COMMENT 'base-创建人;创建人' ,
                            `created_time` DATETIME   COMMENT 'base-创建时间;创建时间' ,
                            `updated_by` VARCHAR(32)   COMMENT 'base-更新人;更新人' ,
                            `updated_time` DATETIME   COMMENT 'base-更新时间;更新时间' ,
                            `name` VARCHAR(255)   COMMENT '角色名称;角色名称' ,
                            PRIMARY KEY (object_id)
)  COMMENT = '角色表';

DROP TABLE IF EXISTS system_permission;
CREATE TABLE system_permission(
                                  `object_id` VARCHAR(32) NOT NULL  COMMENT 'base-ID;主键' ,
                                  `is_deleted` VARCHAR(255)   COMMENT 'base-是否删除;0未删除 1已删除' ,
                                  `revision` INT   COMMENT 'base-乐观锁;乐观锁' ,
                                  `created_by` VARCHAR(32)   COMMENT 'base-创建人;创建人' ,
                                  `created_time` DATETIME   COMMENT 'base-创建时间;创建时间' ,
                                  `updated_by` VARCHAR(32)   COMMENT 'base-更新人;更新人' ,
                                  `updated_time` DATETIME   COMMENT 'base-更新时间;更新时间' ,
                                  `name` VARCHAR(255)   COMMENT '权限名称;权限名称' ,
                                  `description` VARCHAR(900)   COMMENT '权限描述;权限描述' ,
                                  PRIMARY KEY (object_id)
)  COMMENT = '权限表';

DROP TABLE IF EXISTS system_user_roles;
CREATE TABLE system_user_roles(
                                  `object_id` VARCHAR(32) NOT NULL  COMMENT 'base-ID;主键' ,
                                  `is_deleted` VARCHAR(255)   COMMENT 'base-是否删除;0未删除 1已删除' ,
                                  `revision` INT   COMMENT 'base-乐观锁;乐观锁' ,
                                  `created_by` VARCHAR(32)   COMMENT 'base-创建人;创建人' ,
                                  `created_time` DATETIME   COMMENT 'base-创建时间;创建时间' ,
                                  `updated_by` VARCHAR(32)   COMMENT 'base-更新人;更新人' ,
                                  `updated_time` DATETIME   COMMENT 'base-更新时间;更新时间' ,
                                  `user_id` VARCHAR(32)   COMMENT '关联的用户ID;关联的用户ID' ,
                                  `role_id` VARCHAR(32)   COMMENT '关联的角色ID;关联的角色ID' ,
                                  PRIMARY KEY (object_id)
)  COMMENT = '用户-角色表';

DROP TABLE IF EXISTS system_role_permissions;
CREATE TABLE system_role_permissions(
                                        `object_id` VARCHAR(32) NOT NULL  COMMENT 'base-ID;主键' ,
                                        `is_deleted` VARCHAR(255)   COMMENT 'base-是否删除;0未删除 1已删除' ,
                                        `revision` INT   COMMENT 'base-乐观锁;乐观锁' ,
                                        `created_by` VARCHAR(32)   COMMENT 'base-创建人;创建人' ,
                                        `created_time` DATETIME   COMMENT 'base-创建时间;创建时间' ,
                                        `updated_by` VARCHAR(32)   COMMENT 'base-更新人;更新人' ,
                                        `updated_time` DATETIME   COMMENT 'base-更新时间;更新时间' ,
                                        `role_id` VARCHAR(32)   COMMENT '关联的角色ID;关联的角色ID' ,
                                        `permission_id` VARCHAR(32)   COMMENT '关联的权限ID;关联的权限ID' ,
                                        PRIMARY KEY (object_id)
)  COMMENT = '角色-权限表';

DROP TABLE IF EXISTS system_menu;
CREATE TABLE system_menu(
                            `object_id` VARCHAR(32) NOT NULL  COMMENT 'ID;主键' ,
                            `is_deleted` VARCHAR(1)   COMMENT '是否删除;0未删除 1已删除' ,
                            `revision` INT   COMMENT '乐观锁;乐观锁' ,
                            `created_by` VARCHAR(32)   COMMENT '创建人;创建人' ,
                            `created_time` DATETIME   COMMENT '创建时间;创建时间' ,
                            `updated_by` VARCHAR(32)   COMMENT '更新人;更新人' ,
                            `updated_time` DATETIME   COMMENT '更新时间;更新时间' ,
                            `name` VARCHAR(255)   COMMENT '菜单名称;菜单名称' ,
                            `path` VARCHAR(255)   COMMENT '路径;路径' ,
                            `parent_id` VARCHAR(32)   COMMENT '父级菜单ID;父级菜单ID' ,
                            PRIMARY KEY (object_id)
)  COMMENT = '菜单表';


DROP TABLE IF EXISTS system_role_menus;
CREATE TABLE system_role_menus(
                                  `object_id` VARCHAR(32) NOT NULL  COMMENT 'ID;主键' ,
                                  `is_deleted` VARCHAR(1)   COMMENT '是否删除;0未删除 1已删除' ,
                                  `revision` INT   COMMENT '乐观锁;乐观锁' ,
                                  `created_by` VARCHAR(32)   COMMENT '创建人;创建人' ,
                                  `created_time` DATETIME   COMMENT '创建时间;创建时间' ,
                                  `updated_by` VARCHAR(32)   COMMENT '更新人;更新人' ,
                                  `updated_time` DATETIME   COMMENT '更新时间;更新时间' ,
                                  `role_id` VARCHAR(32)   COMMENT '角色ID;角色ID' ,
                                  `menu_id` VARCHAR(32)   COMMENT '菜单ID;菜单ID' ,
                                  PRIMARY KEY (object_id)
)  COMMENT = '角色-菜单表';

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
                             `branch_id` bigint(0) NOT NULL COMMENT '分支事务ID',
                             `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全局事务ID',
                             `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上下文',
                             `rollback_info` longblob NOT NULL COMMENT '回滚信息',
                             `log_status` int(0) NOT NULL COMMENT '状态，0正常，1全局已完成',
                             `log_created` datetime(6) NOT NULL COMMENT '创建时间',
                             `log_modified` datetime(6) NOT NULL COMMENT '修改时间',
                             UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

-- 索引 用在RBAC权限控制的联合查询中
CREATE INDEX idx_system_user_roles_user_id ON system_user_roles ( user_id );
CREATE INDEX idx_system_user_roles_role_id ON system_user_roles ( role_id );

CREATE INDEX idx_system_role_permissions_role_id ON system_role_permissions ( role_id );
CREATE INDEX idx_system_role_permissions_permission_id ON system_role_permissions ( permission_id );

CREATE INDEX idx_system_role_menus_role_id ON system_role_menus ( role_id );
CREATE INDEX idx_system_role_menus_menu_id ON system_role_menus ( menu_id );

-- 索引 用于通过用户名、手机号、
CREATE INDEX idx_system_user_username ON system_user ( username );
CREATE INDEX idx_system_user_email ON system_user ( email );
CREATE INDEX idx_system_user_telephone ON system_user ( telephone );
