package org.tinkoff.project;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqCodegen extends IntegrationEnvironment {
    public static void main(String[] args) throws Exception {
        Database database = new Database()
                .withName("org.jooq.meta.postgres.PostgresDatabase")
                .withIncludes(".*")
                .withExcludes("")
                .withInputSchema("public");

        Generate options = new Generate()
                .withGeneratedAnnotation(true)
                .withGeneratedAnnotationDate(false)
                .withNullableAnnotation(true)
                .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
                .withNonnullAnnotation(true)
                .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
                .withJpaAnnotations(false)
                .withValidationAnnotations(true)
                .withSpringAnnotations(true)
                .withConstructorPropertiesAnnotation(true)
                .withConstructorPropertiesAnnotationOnPojos(true)
                .withConstructorPropertiesAnnotationOnRecords(true)
                .withFluentSetters(false)
                .withDaos(false)
                .withPojos(true);

        Jdbc jdbc = new Jdbc()
                .withDriver("org.postgresql.Driver")
                .withUrl(DB_CONTAINER.getJdbcUrl())
                .withUser(DB_CONTAINER.getUsername())
                .withPassword(DB_CONTAINER.getPassword());

        Target target = new Target()
                .withPackageName("ru.tinkoff.edu.java.scrapper.domain.jooq")
                .withDirectory("scrapper/src/main/java");

        Configuration configuration = new Configuration()
                .withJdbc(jdbc)
                .withGenerator(
                        new Generator()
                                .withDatabase(database)
                                .withGenerate(options)
                                .withTarget(target)
                );

        GenerationTool.generate(configuration);
    }
}