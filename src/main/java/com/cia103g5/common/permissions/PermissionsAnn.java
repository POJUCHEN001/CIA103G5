package com.cia103g5.common.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**##########################################################
 #                                                          #
 #   權限 Ann                                                #
 #   在方法上 @PermissionsAnn() 即可限制管理員可訪問哪一些資源     #
 #   基於 AOP 切面攔截 ( #PermissionsAspect )                 #
 #                                                          #
 ##########################################################*/



@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionsAnn {
    String[] value();
}
