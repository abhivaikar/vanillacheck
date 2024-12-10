package com.vanillacheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanillacheck.annotations.AutoTest;
import com.vanillacheck.annotations.BeforeAutoTest;
import com.vanillacheck.annotations.AfterAutoTest;
import com.vanillacheck.tests.YourTestClass;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        List<TestResult> results = runner.runTests(YourTestClass.class);
        runner.displayResults(results);
        runner.writeJsonReport(results, "test-results.json");
        runner.writeHtmlReport(results, "test-report.html");
    }

    public void displayResults(List<TestResult> results) {
        int totalTests = results.size();
        long passedTests = results.stream().filter(result -> "PASS".equals(result.getStatus())).count();
        long failedTests = totalTests - passedTests;

        System.out.println("=== Test Summary ===");
        System.out.println("Total tests run: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("====================");

        System.out.println("\nDetailed Results:");
        results.forEach(result -> {
            System.out.println("Test: " + result.getTestName() + " | Status: " + result.getStatus());
            if ("FAIL".equals(result.getStatus())) {
                System.out.println("Reason: " + result.getExceptionMessage());
            }
        });
    }

    public List<TestResult> runTests(Class<?> testClass) {
        List<TestResult> results = new ArrayList<>();

        try {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            Method beforeMethod = null, afterMethod = null;

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(BeforeAutoTest.class)) {
                    beforeMethod = method;
                }
                if (method.isAnnotationPresent(AfterAutoTest.class)) {
                    afterMethod = method;
                }
            }

            for (Method testMethod : testClass.getDeclaredMethods()) {
                if (testMethod.isAnnotationPresent(AutoTest.class)) {
                    TestResult result = executeTest(testInstance, testMethod, beforeMethod, afterMethod);
                    results.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    private TestResult executeTest(Object testInstance, Method testMethod, Method beforeMethod, Method afterMethod) {
        String testName = testMethod.getName();
        long startTime = System.currentTimeMillis();
        String status = "PASS";
        String exceptionMessage = null;

        try {
            if (beforeMethod != null) beforeMethod.invoke(testInstance);
            testMethod.invoke(testInstance);
        } catch (Exception e) {
            status = "FAIL";
            exceptionMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        } finally {
            if (afterMethod != null) {
                try {
                    afterMethod.invoke(testInstance);
                } catch (Exception ignored) {}
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;
        return new TestResult(testName, status, exceptionMessage, executionTime);
    }

    public void writeJsonReport(List<TestResult> results, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new java.io.File(fileName), results);
            System.out.println("JSON report written to: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to write JSON report: " + e.getMessage());
        }
    }

    public void writeHtmlReport(List<TestResult> results, String fileName) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>Test Report</title>")
                .append("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\">")
                .append("</head>")
                .append("<body class=\"container mt-5\">")
                .append("<h1 class=\"mb-4\">Test Report</h1>")
                .append("<table class=\"table table-bordered\">")
                .append("<thead><tr><th>Test Name</th><th>Status</th><th>Exception</th><th>Execution Time (ms)</th></tr></thead>")
                .append("<tbody>");

        for (TestResult result : results) {
            htmlContent.append("<tr>")
                    .append("<td>").append(result.getTestName()).append("</td>")
                    .append("<td>").append(result.getStatus()).append("</td>")
                    .append("<td>").append(result.getExceptionMessage() != null ? result.getExceptionMessage() : "N/A").append("</td>")
                    .append("<td>").append(result.getExecutionTime()).append("</td>")
                    .append("</tr>");
        }

        htmlContent.append("</tbody>")
                .append("</table>")
                .append("</body>")
                .append("</html>");

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(htmlContent.toString());
            System.out.println("HTML report written to: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to write HTML report: " + e.getMessage());
        }
    }
}
