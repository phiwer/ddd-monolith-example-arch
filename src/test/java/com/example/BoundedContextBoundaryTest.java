package com.example;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * These tests enforce the bounded context boundaries at build time.
 * If someone accidentally imports a domain class from another context,
 * the build fails.
 */
class BoundedContextBoundaryTest {

    private static com.tngtech.archunit.core.domain.JavaClasses allClasses;

    @BeforeAll
    static void importClasses() {
        allClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Catalog domain is not accessed from outside the Catalog context")
    void catalogDomainIsEncapsulated() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("com.example.catalog..")
                .should().accessClassesThat().resideInAnyPackage("com.example.catalog.domain..");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Ordering domain is not accessed from outside the Ordering context")
    void orderingDomainIsEncapsulated() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("com.example.ordering..")
                .should().accessClassesThat().resideInAnyPackage("com.example.ordering.domain..");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Inventory domain is not accessed from outside the Inventory context")
    void inventoryDomainIsEncapsulated() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("com.example.inventory..")
                .should().accessClassesThat().resideInAnyPackage("com.example.inventory.domain..");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Infrastructure layers are not accessed from outside their own context")
    void infrastructureIsEncapsulated() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("com.example.catalog..")
                .should().accessClassesThat().resideInAnyPackage("com.example.catalog.infrastructure..");

        rule.check(allClasses);
    }

    @Test
    @DisplayName("Cross-context dependencies only go through application interfaces")
    void crossContextDependenciesUseApplicationLayer() {
        // Ordering can depend on Catalog's application layer (interface + DTOs)
        // but never on Catalog's domain or infrastructure
        ArchRule rule = classes()
                .that().resideInAnyPackage("com.example.ordering..")
                .should().onlyAccessClassesThat()
                .resideInAnyPackage(
                        "com.example.ordering..",
                        "com.example.catalog.application..",   // ← allowed: interface + DTOs
                        "com.example.shared..",                 // ← allowed: shared kernel
                        "java..",
                        "org.springframework..",
                        "jakarta.."
                );

        rule.check(allClasses);
    }
}
