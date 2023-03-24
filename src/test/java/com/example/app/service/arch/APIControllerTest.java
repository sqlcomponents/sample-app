package com.example.app.service.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import com.tngtech.archunit.lang.syntax.elements.GivenMethodsConjunction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * The type Api controller test.
 */
public class APIControllerTest {

    private static final GivenClassesConjunction CONTROLLER_CLASSES = classes()
            .that()
            .areAnnotatedWith(RestController.class);

    private static final GivenMethodsConjunction CONTROLLER_PUBLIC_METHODS
            = methods().that().areDeclaredInClassesThat()
            .areAnnotatedWith(RestController.class).and().arePublic();

    @Test
    public void controller_immutable_stateless() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .importPackages("com.example");

        ArchRule rule = CONTROLLER_CLASSES
                .should()
                .bePackagePrivate()
                .andShould().haveSimpleNameEndingWith("APIController")
                .andShould().beAnnotatedWith(RequestMapping.class)
                .andShould().beAnnotatedWith(Tag.class)
                .andShould().haveOnlyFinalFields()
                .andShould().accessClassesThat()
                .resideOutsideOfPackage("jakarta.validation")
                .andShould().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "com.example.core.controllers.util"
                        , "jakarta.servlet.http"
                        , "java.sql"
                        , "jakarta.servlet"
                        , "java.io"
                        , "java.net"
                        , "jakarta.servlet.http"
                        , "com.example.core.security.controllers.util"
                        , "org.springframework.security.core.userdetails"
                        , "org.springframework.security.access.prepost"
                        , "com.example.web.starter.security.security"
                        , "org.springframework.security.authentication"
                        , "org.springframework.beans.factory.annotation"
                        , "org.springframework.data.domain"
                        , "java.util"
                        , "..service.."
                        , "..model.."
                        , "..payload.."
                        , "java.lang"
                        , "org.springframework.http"
                        , "com.fasterxml.jackson.core"
                        , "org.springframework.web.bind.annotation"
                        , "io.swagger.v3.oas.annotations"
                        , "io.swagger.v3.oas.annotations.tags"
                        , "io.swagger.v3.oas.annotations.parameters"
                        , "org.springframework.web.bind"
                        , "io.swagger.v3.oas.annotations.responses"
                        , "io.swagger.v3.oas.annotations.security"
                        , "java.security")
                .because("Controllers should only be delegates");

        rule.check(importedClasses);

        rule = CONTROLLER_PUBLIC_METHODS
                .should()
                .haveRawReturnType(ResponseEntity.class)
                .andShould().beAnnotatedWith(Operation.class)
                .andShould().notBeAnnotatedWith(Valid.class)
                .because("we don't want to expose domain models directly");

        rule.check(importedClasses);


    }

    @Test
    public void testSourceText() throws IOException {
        List<Path> controllerFiles ;
        //TODO: https://github.com/TNG/ArchUnit/issues/113
        // We should not validate at Controller Layer
        try (Stream<Path> walk = Files.walk(Paths.get("src/main/java"))) {
            controllerFiles = walk
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith("APIController.java"))
                    .collect(Collectors.toList());

        }

        List<Path> problematicFiles = controllerFiles.stream()
                .filter(path -> {
                    try {
                        return Files.readString(path).contains("@Valid");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());

        Assertions.assertEquals(0, problematicFiles.size(),
                "No Validation at Controller " + problematicFiles.toString());

        problematicFiles = controllerFiles.stream()
                .filter(path -> {
                    try {
                        return Files.readString(path).contains("new ResponseEntity");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
        Assertions.assertEquals(0, problematicFiles.size(),
                "Response Entity should only be created by convinient methods "
                        + problematicFiles.toString());


    }


}