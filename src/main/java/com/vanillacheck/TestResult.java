package com.vanillacheck;

public class TestResult {
    private String testName;
    private String status;
    private String exceptionMessage;
    private long executionTime;

    public TestResult(String testName, String status, String exceptionMessage, long executionTime) {
        this.testName = testName;
        this.status = status;
        this.exceptionMessage = exceptionMessage;
        this.executionTime = executionTime;
    }

    public String getTestName() {
        return testName;
    }

    public String getStatus() {
        return status;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
