package org.tinkoff.project;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqCodegen {
    private static final String ENV_DB_URL = "JOOQ_DB_URL";
    private static final String ENV_DB_USERNAME = "JOOQ_DB_USERNAME";
    private static final String ENV_DB_PASSWORD = "JOOQ_DB_PASSWORD";
    public static void main(String[] args) throws Exception {
        Database database = new Database()
                .withName("org.jooq.meta.postgres.PostgresDatabase")
                .withProperties(
                        new Property().withKey("rootPath").withValue("migrations"),
                        new Property().withKey("scripts").withValue("master.xml")
                );

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
                .withUrl(System.getenv(ENV_DB_URL))
                .withUser(System.getenv(ENV_DB_USERNAME))
                .withPassword(System.getenv(ENV_DB_PASSWORD));

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