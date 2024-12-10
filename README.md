# VanillaCheck

Hi there! I'm working on this project, **VanillaCheck**, as an exercise to better understand how testing frameworks like JUnit or TestNG are created. This is not meant to compete with those frameworks; it's just a simple and minimalistic attempt to build something from scratch to learn the core concepts behind test discovery, execution, and reporting.

---

## Why I Created VanillaCheck

I’ve always been fascinated by how popular testing frameworks make writing and running tests so seamless. To demystify the inner workings, I decided to create my own basic test runner. The goal is to explore:
- **How tests are discovered automatically using annotations.**
- **How hooks like setup and teardown are implemented.**
- **How results are captured and presented.**

This project is my way of diving deeper into those concepts and experimenting with a lightweight test runner.

---

## What VanillaCheck Does

VanillaCheck is a minimal test runner that:
1. Automatically discovers test methods in a class using custom annotations.
2. Executes tests sequentially with before and after hooks.
3. Generates test result reports in three formats:
    - Console output
    - JSON file
    - HTML file (styled with Bootstrap)

---

## Features

- **Custom Annotations**:
    - `@AutoTest`: Marks a method as a test.
    - `@BeforeAutoTest`: Marks a method to run before each test.
    - `@AfterAutoTest`: Marks a method to run after each test.

- **Simple Test Execution**:
    - Tests are discovered and executed in sequence.
    - Failures in one test don’t stop others from running.

- **Report Generation**:
    - A clean JSON file summarizing the results.
    - A Bootstrap-styled HTML report for easy viewing.

---

## How It Works

Here’s a simple example of how to use VanillaCheck:

### Write a Test Class
```java
package com.vanillacheck.tests;

import com.vanillacheck.annotations.AutoTest;
import com.vanillacheck.annotations.BeforeAutoTest;
import com.vanillacheck.annotations.AfterAutoTest;
import org.assertj.core.api.Assertions;

public class YourTestClass {

    @BeforeAutoTest
    public void setup() {
        System.out.println("Before each test");
    }

    @AutoTest
    public void testExamplePass() {
        Assertions.assertThat(1 + 1).isEqualTo(2);
    }

    @AutoTest
    public void testExampleFail() {
        Assertions.assertThat(1 + 1).isEqualTo(3);
    }

    @AfterAutoTest
    public void teardown() {
        System.out.println("After each test");
    }
}
```

### Run the Tests
The `TestRunner` class is responsible for running your tests:
```java
public class TestRunner {

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        List<TestResult> results = runner.runTests(YourTestClass.class);
        runner.displayResults(results);
        runner.writeJsonReport(results, "test-results.json");
        runner.writeHtmlReport(results, "test-report.html");
    }
}
```

---

## What You’ll See

1. **Console Output**:
   ```plaintext
   Test Results:
   Test: testExamplePass | Status: PASS
   Test: testExampleFail | Status: FAIL
   Reason: expected:<3> but was:<2>
   ```

2. **JSON Report (`test-results.json`)**:
   ```json
   [
       {
           "testName": "testExamplePass",
           "status": "PASS",
           "exceptionMessage": null,
           "executionTime": 5
       },
       {
           "testName": "testExampleFail",
           "status": "FAIL",
           "exceptionMessage": "expected:<3> but was:<2>",
           "executionTime": 7
       }
   ]
   ```

3. **HTML Report (`test-report.html`)**:
    - A Bootstrap-styled report that summarizes test results in a table.


