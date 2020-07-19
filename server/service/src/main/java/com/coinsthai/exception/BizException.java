package com.coinsthai.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1930539361139157688L;

    private ErrorCode errorCode;

    /**
     * 以逗号分隔的参数名，应与arguments一一对应
     */
    private String argumentNames;

    /**
     * 发生异常时的参数
     */
    private Object[] arguments;

    private BizException() {
        //hide the default constructor, require the error code to be used
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public BizException(ErrorCode errorCode, String argumentNames, Object... arguments) {
        this(errorCode);
        this.argumentNames = argumentNames;
        this.arguments = arguments;
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.toString(), cause);
        this.setErrorCode(errorCode);
    }

    public BizException(ErrorCode errorCode, Throwable cause, String argumentNames, Object... arguments) {
        this(errorCode, cause);
        this.argumentNames = argumentNames;
        this.arguments = arguments;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getArgumentNames() {
        return argumentNames;
    }

    public void setArgumentNames(String argumentNames) {
        this.argumentNames = argumentNames;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        String s = super.toString();
        String arguments = generateArguments();
        if (StringUtils.isNotBlank(arguments)) {
            s = s + arguments;
        }
        return s;
    }

    private String generateArguments() {
        StringBuilder sb = new StringBuilder();

        int nameLength = 0;
        String[] names = null;
        if (argumentNames != null) {
            names = StringUtils.split(argumentNames, ',');
            nameLength = names.length;
        }
        int valueLength = arguments == null ? 0 : arguments.length;
        int loops = NumberUtils.max(nameLength, valueLength);

        if (loops == 0) {
            return sb.toString();
        }

        sb.append(" [");
        for (int i = 0; i < loops; i++) {
            if (sb.length() > 2) {
                sb.append(",");
            }

            if (i >= nameLength) {
                sb.append("unknown");
            }
            else {
                sb.append(names[i]);
            }
            sb.append("=");

            if (i < valueLength) {
                sb.append(arguments[i]);
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
