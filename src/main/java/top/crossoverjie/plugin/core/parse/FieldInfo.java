package top.crossoverjie.plugin.core.parse;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-03-16 00:48
 * @since JDK 1.8
 */
public class FieldInfo {
    private String fieldName;
    private String filedType;
    private String fieldLen;
    private String comment;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFiledType() {
        return filedType;
    }

    public void setFiledType(String filedType) {
        this.filedType = filedType;
    }

    public String getFieldLen() {
        return fieldLen;
    }

    public void setFieldLen(String fieldLen) {
        this.fieldLen = fieldLen;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", filedType='" + filedType + '\'' +
                ", fieldLen='" + fieldLen + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
