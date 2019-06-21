package model;

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
}
