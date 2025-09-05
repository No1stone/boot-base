package com.origemite.lib.legacy.transaction;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(transactionManager = "TransactionManager_LEGACY", rollbackFor = { RuntimeException.class, Exception.class})
public @interface LegacyTransactional {
}


