package com.example.naidich.tom.tomsprojectapplication.core.models;

import java.util.Objects;

public class ApiResult {
    private boolean success;
    private String error;
    private Object result;

    public ApiResult(boolean success, String error, Object result) {
        this.success = success;
        this.error = error;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResult apiResult = (ApiResult) o;
        return success == apiResult.success &&
                Objects.equals(error, apiResult.error) &&
                Objects.equals(result, apiResult.result);
    }

    @Override
    public int hashCode() {

        return Objects.hash(success, error, result);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "success=" + success +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
