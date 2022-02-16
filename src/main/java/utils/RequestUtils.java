package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;


public class RequestUtils {

    // returns parameter value or 0 if there is no such parameter or it's not a long number
    public static long getLongParameter(HttpServletRequest request, String parameterName) {
        try {
            return Long.parseLong(request.getParameter(parameterName));
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    // returns parameter value or null if there is no such parameter or it's not a long number
    public static BigDecimal getBigDecimalParameter(HttpServletRequest request, String parameterName) {
        try {
            return new BigDecimal(request.getParameter(parameterName));
        } catch(NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    // returns parameter value or 0 if there is no such parameter or it's not a long number
    public static int getIntParameter(HttpServletRequest request, String parameterName) {
        try {
            return Integer.parseInt(request.getParameter(parameterName));
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    // returns parameter value or null if there is no such parameter
    public static String getStringParameter(HttpServletRequest request, String parameterName) {
        return request.getParameter(parameterName);
    }

    public static <T> T getSessionAttribute(HttpServletRequest request, String attributeName, Class<T> clazz) {
        HttpSession session = request.getSession(false);
        if (session != null && attributeName != null) {
            Object attribute = session.getAttribute(attributeName);
            if (clazz.isInstance(attribute)) {
                return clazz.cast(attribute);
            }
        }
        return null;
    }

    // converts map of parameters to a query string
    public static String parameterMapToQuery(Map<String, String[]> map) {
        return map.entrySet().stream().map(entry -> {
            StringBuilder sb = new StringBuilder();
            String[] arr = entry.getValue();
            if(arr.length > 0)
                sb.append(entry.getKey()).append("=").append(arr[0]);
            for(int i = 1; i < arr.length; i++)
                sb.append("&").append(entry.getKey()).append("=").append(arr[i]);
            return sb.toString();
        }).reduce((s1, s2) -> s1 + "&" + s2).orElse("");
    }

}