package ttyy.com.datasdao.constants;

/**
 * author: admin
 * date: 2017/12/22
 * version: 0
 * mail: secret
 * desc: ConflictAction
 */

public enum ConflictAction {
    IGNORE("IGNORE"),
    REPLACE("REPLACE"),
    ABORT("ABORT"),
    FAIL("FAIL");

    String value;

    ConflictAction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
