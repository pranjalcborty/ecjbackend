package db.models;

public enum Problem {

    SYM_REGRESSION("Symbolic Regression", "gp.problems.SymbolicLinearRegressionProblem"),
    BINARY_CLASSIFICATION("Binary Classification", "gp.problems.BinaryClassificationProblem");

    private final String titleStr;
    private final String problemClass;

    Problem(String titleStr, String problemClass) {
        this.titleStr = titleStr;
        this.problemClass = problemClass;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public String getProblemClass() {
        return problemClass;
    }
}
