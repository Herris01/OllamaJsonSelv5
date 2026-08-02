package ai.selfheal.tests;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExecutionTimeListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        printTime(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        printTime(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        printTime(result);
    }

    private void printTime(ITestResult result) {
        // Calculate total execution time in milliseconds
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        System.out.println(">>> Test: " + testName + " took " + duration + " ms");
    }
}
