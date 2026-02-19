package sparadrap.util.regex;

public final class Regex {

    public static final String NOM = "^[\\p{L}]+(?:[ \\p{L}'’-][\\p{L}]+)*$";
    public static final String EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String DATE = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String CODE_POSTAL = "^\\d{5}$";
    public static final String TELEPHONE = "^(0\\d)([\\s.-]?\\d{2}){4}$";
    public static final String NUMERO_SECURITE_SOCIALE = "^[12]\\d{2}\\d{2}\\d{2}\\d{3}\\d{3}\\d{2}$";

    private Regex() {
    }

    public static boolean isValid(String value, String pattern) {
        return value != null && value.matches(pattern);
    }
}
