package com.origemite.lib.common.extend;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.query.sqm.function.FunctionKind;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.query.sqm.produce.function.PatternFunctionDescriptorBuilder;
import org.hibernate.type.spi.TypeConfiguration;

public class CustomMariaDBDialect extends MariaDBDialect {

    public CustomMariaDBDialect() {
        super();
    }

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        SqmFunctionRegistry registry = functionContributions.getFunctionRegistry();
        TypeConfiguration types = functionContributions.getTypeConfiguration();

        new PatternFunctionDescriptorBuilder(registry, "regexp", FunctionKind.NORMAL, "?1 REGEXP ?2")
                .setExactArgumentCount(2)
                .setInvariantType(types.getBasicTypeForJavaType(Integer.class))
                .register();
    }
}
