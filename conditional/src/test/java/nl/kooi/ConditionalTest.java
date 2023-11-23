package nl.kooi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static nl.kooi.Conditional.applyIf;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConditionalTest {

    @Nested
    @DisplayName("Tests for conditionals with orElse")
    class orElse {

        @Test
        @DisplayName("orElse: when a condition matches, then the matching function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .orElse(0);

            assertThat(outcome).isEqualTo(4);
        }

        @Test
        @DisplayName("orElse: when no condition matches, then the default value is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .orElse(0);

            assertThat(outcome).isEqualTo(0);
        }

        @Test
        @DisplayName("orElse: when a condition matches and the Conditional pipeline contains a map, then the matching function is first applied and then the map function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .map(i -> String.format("And the number is: %d", i))
                    .orElse("No outcome");

            assertThat(outcome).isEqualTo("And the number is: 4");
        }

        @Test
        @DisplayName("orElse: when no condition matches and the Conditional pipeline contains a map, then the default value is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse_ignoringMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .map(i -> String.format("And the number is: %d", i))
                    .orElse("No outcome");

            assertThat(outcome).isEqualTo("No outcome");
        }

        @Test
        @DisplayName("orElse: when a condition matches and the Conditional pipeline contains a flatMap, then the matching function is first applied and then the flatMap function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingFlatMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .flatMap(ConditionalTest::conditionalThatMultipliesBy2WhenNumberIsEven)
                    .orElse(0);

            assertThat(outcome).isEqualTo(8);
        }

        @Test
        @DisplayName("orElse: when no condition matches and the Conditional pipeline contains a flatMap, then the default value is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse_ignoringFlatMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .flatMap(ConditionalTest::conditionalThatMultipliesBy2WhenNumberIsEven)
                    .orElse(0);

            assertThat(outcome).isEqualTo(0);
        }

        @Test
        @DisplayName("orElse: when a condition matches and the matching function evaluates to null, then a null is being returned and the default value is ignored.")
        void conditionalWithOneConditionThatEvaluatesToTrueAndFunctionThatReturnsNull() {
            var outcome = Conditional.of(2)
                    .firstMatching(applyIf(isEven(), i -> null))
                    .orElse(0);

            assertThat(outcome).isNull();
        }

        @Test
        @DisplayName("orElse: when a null is passed as the object to be evaluated, then the default value is returned.")
        void conditionalPassingInANull_orElse() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(null)
                    .orElse(0);

            assertThat(outcome).isEqualTo(0);
        }

        @Test
        @DisplayName("orElse: when no condition matches and the default value is null, then this default value is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse_orElseWithNull() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .orElse(null);

            assertThat(outcome).isNull();
        }

        @Test
        @DisplayName("orElse: when multiple conditions would return true, then only the function belonging to the first condition that evaluated to true is applied.")
        void conditionalWithMultipleConditionsThatEvaluateToTrue_functionOfFirstTrueIsEvaluated() {
            var outcome = conditionalWithMultipleConditionsThatEvaluateToTrue(0)
                    .orElse(9);

            assertThat(outcome).isEqualTo(3);
        }

        @Test
        @DisplayName("orElse: when the Conditional pipeline contain multiple conditions that all evaluate to false, then the default value is returned.")
        void conditionalWithMultipleConditionsThatAllEvaluateToFalse() {
            var outcome = conditionalWithAllConditionsEvaluatingToFalse(0)
                    .orElse(9);

            assertThat(outcome).isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("Tests for conditionals with orElseGet")
    class orElseGet {

        @Test
        @DisplayName("orElseGet: when a condition matches, then the matching function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue() {
            var outcome = Conditional.of(2)
                    .firstMatching(applyIf(isEven(), Objects::toString))
                    .orElseGet(() -> "hello world");

            assertThat(outcome).isEqualTo("2");
        }

        @Test
        @DisplayName("orElseGet: when no condition matches, then the value evaluated from the default Supplier is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse() {
            var outcome = Conditional.of(2)
                    .firstMatching(applyIf(isEven().negate(), Object::toString))
                    .orElseGet(() -> "hello world");

            assertThat(outcome).isEqualTo("hello world");
        }

        @Test
        @DisplayName("orElseGet: when a condition matches and the Conditional pipeline contains a map, then the matching function is first applied and then the map function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .map(i -> String.format("And the number is: %d", i))
                    .orElseGet(() -> "No outcome");

            assertThat(outcome).isEqualTo("And the number is: 4");
        }

        @Test
        @DisplayName("orElseGet: when no condition matches and the Conditional pipeline contains a map, then the value evaluated from the default Supplier is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse_ignoringMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .map(i -> String.format("And the number is: %d", i))
                    .orElseGet(() -> "No outcome");

            assertThat(outcome).isEqualTo("No outcome");
        }

        @Test
        @DisplayName("orElseGet: when a condition matches and the Conditional pipeline contains a flatMap, then the matching function is first applied and then the flatMap function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingFlatMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .flatMap(ConditionalTest::conditionalThatMultipliesBy2WhenNumberIsEven)
                    .orElseGet(() -> 0);

            assertThat(outcome).isEqualTo(8);
        }

        @Test
        @DisplayName("orElseGet: when no condition matches and the Conditional pipeline contains a flatMap, then the value evaluated from the default Supplier is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse_ignoringFlatMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .flatMap(ConditionalTest::conditionalThatMultipliesBy2WhenNumberIsEven)
                    .orElseGet(() -> 0);

            assertThat(outcome).isEqualTo(0);
        }

        @Test
        @DisplayName("orElseGet: when a null is passed as the object to be evaluated, then the value evaluated from the default Supplier is returned.")
        void conditionalPassingInANull_orElseGet() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(null)
                    .orElseGet(() -> 0);

            assertThat(outcome).isEqualTo(0);
        }

        @Test
        @DisplayName("orElseGet: when no condition matches and the default Supplier evaluates to null, then this null is returned.")
        void conditionalWithOneConditionThatEvaluatesToFalse_orElseGetReturningNull() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .orElseGet(() -> null);

            assertThat(outcome).isNull();
        }

        @Test
        @DisplayName("orElseGet: when a condition matches and the matching function evaluates to null, then a null is being returned and the default Supplier is ignored.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingFunctionThatReturnsNull() {
            var outcome = Conditional.of(2)
                    .firstMatching(applyIf(isEven(), i -> null))
                    .orElseGet(() -> "hello world");

            assertThat(outcome).isNull();
        }

        @Test
        @DisplayName("orElseGet: when multiple conditions would return true, then only the function belonging to the first condition that evaluated to true is applied.")
        void conditionalWithMultipleConditionsThatEvaluateToTrue_functionOfFirstTrueIsEvaluated() {
            var outcome = conditionalWithMultipleConditionsThatEvaluateToTrue(0)
                    .orElseGet(() -> 9);

            assertThat(outcome).isEqualTo(3);
        }

        @Test
        @DisplayName("orElseGet: when the Conditional pipeline contain multiple conditions that all evaluate to false, then the value evaluated from the default Supplier is returned.")
        void conditionalWithMultipleConditionsThatAllEvaluateToFalse() {
            var outcome = conditionalWithAllConditionsEvaluatingToFalse(0)
                    .orElseGet(() -> 9);

            assertThat(outcome).isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("Tests for conditionals with orElseThrow")
    class orElseThrow {

        @Test
        @DisplayName("orElseThrow: when a condition matches, then the matching function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .orElseThrow(IllegalArgumentException::new);

            assertThat(outcome).isEqualTo(4);
        }

        @Test
        @DisplayName("orElseThrow: when no condition matches, then the exception supplier is evaluated (throwing an exception).")
        void conditionalWithOneConditionThatEvaluatesToFalse() {
            assertThrows(IllegalArgumentException.class, () ->
                    conditionalThatMultipliesBy2WhenNumberIsEven(3)
                            .orElseThrow(IllegalArgumentException::new));
        }

        @Test
        @DisplayName("orElseThrow: when a condition matches and the Conditional pipeline contains a map, then the matching function is first applied and then the map function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .map(i -> String.format("And the number is: %d", i))
                    .orElseThrow(IllegalArgumentException::new);

            assertThat(outcome).isEqualTo("And the number is: 4");
        }

        @Test
        @DisplayName("orElseThrow: when no condition matches and the Conditional pipeline contains a map, then the exception supplier is evaluated (throwing an exception).")
        void conditionalWithOneConditionThatEvaluatesToFalse_ignoringMap() {
            assertThrows(IllegalArgumentException.class, () ->
                    conditionalThatMultipliesBy2WhenNumberIsEven(3)
                            .map(i -> String.format("And the number is: %d", i))
                            .orElseThrow(IllegalArgumentException::new));
        }

        @Test
        @DisplayName("orElseThrow: when a condition matches and the Conditional pipeline contains a map, then the matching function is first applied and then the map function is applied to the object.")
        void conditionalWithOneConditionThatEvaluatesToTrue_applyingFlatMap() {
            var outcome = conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .flatMap(ConditionalTest::conditionalThatMultipliesBy2WhenNumberIsEven)
                    .orElseThrow(IllegalArgumentException::new);

            assertThat(outcome).isEqualTo(8);
        }

        @Test
        @DisplayName("orElseThrow: when no condition matches and the Conditional pipeline contains a map, then the exception supplier is evaluated (throwing an exception).")
        void conditionalWithOneConditionThatEvaluatesToFalse_ignoringFlatMap() {
            assertThrows(IllegalArgumentException.class, () ->
                    conditionalThatMultipliesBy2WhenNumberIsEven(3)
                            .flatMap(ConditionalTest::conditionalThatMultipliesBy2WhenNumberIsEven)
                            .orElseThrow(IllegalArgumentException::new));
        }

        @Test
        @DisplayName("orElseThrow: when a null is passed as the object to be evaluated, then the exception supplier is evaluated (throwing an exception).")
        void conditionalPassingInANull() {
            assertThrows(IllegalArgumentException.class, () ->
                    conditionalThatMultipliesBy2WhenNumberIsEven(null)
                            .orElseThrow(IllegalArgumentException::new));
        }

        @Test
        @DisplayName("orElseThrow: when multiple conditions would return true, then only the function belonging to the first condition that evaluated to true is applied.")
        void conditionalWithMultipleConditionsThatEvaluateToTrue_functionOfFirstTrueIsEvaluated() {
            var outcome = conditionalWithMultipleConditionsThatEvaluateToTrue(0)
                    .orElseThrow(IllegalArgumentException::new);

            assertThat(outcome).isEqualTo(3);
        }

        @Test
        @DisplayName("orElseThrow: when the Conditional pipeline contain multiple conditions that all evaluate to false, then the exception supplier is evaluated (throwing an exception).")
        void conditionalWithMultipleConditionsThatAllEvaluateToFalse() {
            assertThrows(IllegalArgumentException.class, () -> conditionalWithAllConditionsEvaluatingToFalse(0)
                    .orElseThrow(IllegalArgumentException::new));
        }
    }

    @Nested
    @DisplayName("Tests for wrong use of the Conditional")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Exception Handling: when a null is passed as the second parameter of a mapWhen, an NPE is thrown.")
        void predicateShouldNotBeNull() {
            assertThrows(NullPointerException.class, () -> Conditional.of(1)
                    .firstMatching(applyIf(null, square()))
                    .orElse(3456));
        }

        @Test
        @DisplayName("Exception Handling: when an orMapWhen containing null is added to the Conditional pipeline, an NPE is thrown.")
        void functionShouldNotBeNull_orMapWhen() {
            assertThrows(NullPointerException.class, () -> Conditional.of(1)
                    .firstMatching(
                            applyIf(returnTrue(), timesTwo()),
                            applyIf(returnFalse(), null))
                    .orElse(3456));
        }

        @Test
        @DisplayName("Exception Handling: when the Conditional pipeline starts with an.mapWhen containing null, an NPE is thrown.")
        void functionShouldNotBeNullmapWhen() {
            assertThrows(NullPointerException.class, () -> Conditional
                    .of(1)
                    .firstMatching(
                            applyIf(null, null),
                            applyIf(null, null))
                    .orElse(3456));
        }

        @Test
        @DisplayName("Exception Handling: when the Conditional pipeline contains an orElseThrow containing a null throwableSupplier, an NPE is thrown.")
        void conditionalWithOneConditionThatEvaluatesToFalse_passingNullThrowableSupplierToOrElseThrow() {
            assertThrows(NullPointerException.class, () -> conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .orElseThrow(null));
        }

        @Test
        @DisplayName("Exception Handling: when the Conditional pipeline contains an orElseGet containing a null Supplier, an NPE is thrown.")
        void conditionalWithOneConditionThatEvaluatesToFalse_passingNullSupplierToOrElseGet() {
            assertThrows(NullPointerException.class, () -> conditionalThatMultipliesBy2WhenNumberIsEven(3)
                    .orElseGet(null));
        }

        @Test
        @DisplayName("Exception Handling: when a null is passed to map, an NPE is thrown.")
        void conditionalWithOneConditionThatEvaluatesToTrue_passingNullToMap() {
            assertThrows(NullPointerException.class, () -> conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .map(null)
                    .orElse("No outcome"));
        }

        @Test
        @DisplayName("Exception Handling: when a null is passed to flatMap, an NPE is thrown.")
        void conditionalWithOneConditionThatEvaluatesToTrue_passingNullToFlatMap() {
            assertThrows(NullPointerException.class, () -> conditionalThatMultipliesBy2WhenNumberIsEven(2)
                    .flatMap(null)
                    .orElse("No outcome"));
        }
    }

    private static Conditional<Integer, Integer> conditionalWithMultipleConditionsThatEvaluateToTrue(Integer number) {
        return Conditional.of(number)
                .firstMatching(
                        applyIf(returnFalse(), plus(1)),
                        applyIf(returnFalse(), plus(2)),
                        applyIf(returnTrue(), plus(3)),
                        applyIf(returnTrue(), plus(4)),
                        applyIf(returnTrue(), plus(5)),
                        applyIf(returnFalse(), plus(6))
                );
    }

    private static Conditional<Integer, Integer> conditionalWithAllConditionsEvaluatingToFalse(Integer number) {
        return Conditional.of(number)
                .firstMatching(
                        applyIf(returnFalse(), plus(1)),
                        applyIf(returnFalse(), plus(2)),
                        applyIf(returnFalse(), plus(3)),
                        applyIf(returnFalse(), plus(4)),
                        applyIf(returnFalse(), plus(5)),
                        applyIf(returnFalse(), plus(6))
                );
    }

    private static Conditional<Integer, Integer> conditionalThatMultipliesBy2WhenNumberIsEven(Integer number) {
        return Conditional.of(number)
                .firstMatching(
                        applyIf(isEven(), timesTwo())
                );
    }

    private static UnaryOperator<Integer> timesTwo() {
        return i -> i * 2;
    }

    private static UnaryOperator<Integer> square() {
        return i -> i * i;
    }

    private static Predicate<Integer> isEven() {
        return i -> i % 2 == 0;
    }

    private static UnaryOperator<Integer> plus(int plus) {
        return i -> i + plus;
    }

    private static Predicate<Integer> returnFalse() {
        return i -> false;
    }

    private static Predicate<Integer> returnTrue() {
        return i -> true;
    }
}