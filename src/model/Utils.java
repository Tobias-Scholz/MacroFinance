package model;

import java.math.BigDecimal;

public class Utils
{
    public static String format_money_to_string(long value)
    {
        long abs_val = Math.abs(value);
        String ret;

        if (abs_val > 100)
        {
            String s = Long.toString(abs_val);
            ret = new StringBuilder(s).insert(s.length() - 2, ".").toString();
        }
        else if (abs_val > 9)
        {
            ret = "0." + abs_val;
        }
        else if (value > 0)
        {
            ret = "0.0" + abs_val;
        }
        else
        {
            ret = "0.00";
        }

        if (value < 0)
        {
            ret = "-" + ret;
        }

        return ret;
    }

    public static Long convert_string_to_money(String string)
    {
        BigDecimal decimal = new BigDecimal(string);
        decimal = decimal.multiply(new BigDecimal(100));
        return decimal.longValue();
    }

    public static boolean is_valid_moneystring(String string)
    {
        return string.matches("^[0-9]*\\.?[0-9]*$");
    }
}
