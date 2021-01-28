package units.progadv.process.ExpressionResponse;

import java.text.NumberFormat;
import java.util.Locale;

public class ResultResponseFormatter {

    private static final int RESULT_DECIMALS = 6;
    private static final int TIME_DECIMALS = 3;

    static NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);

    public static String formatResult(double value) {
        if (Double.isFinite(value)) {
            formatter.setMaximumFractionDigits(RESULT_DECIMALS);
            formatter.setMinimumFractionDigits(RESULT_DECIMALS);
            formatter.setGroupingUsed(false);
            return formatter.format(value);
        } else
            return Double.toString(value);
    }

    public static String formatTime(double value) {
        formatter.setMaximumFractionDigits(TIME_DECIMALS);
        formatter.setMinimumFractionDigits(TIME_DECIMALS);
        return formatter.format(value);
    }
}
