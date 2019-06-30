package model;

import java.util.HashMap;

public class TradeError
{
    private String error_text = "";
    private boolean[] errors;

    public TradeError()
    {
        errors = new boolean[6];
        for (int i = 0; i < errors.length; i++)
        {
            errors[i] = false;
        }
    }

    public String getError_text()
    {
        return error_text;
    }

    public boolean[] getErrors()
    {
        return errors;
    }

    public void append_to_error(String string)
    {
        error_text += string + "\n";
    }

    public boolean any_error()
    {
        for (boolean b : errors)
        {
            if (b) return true;
        }
        return false;
    }
}
