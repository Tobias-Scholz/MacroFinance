package model;

import java.util.HashMap;

public class TradeError
{
    private String error_text;
    private HashMap<Integer, Boolean> errors;

    public TradeError()
    {
        errors = new HashMap<>();
    }

    public String getError_text()
    {
        return error_text;
    }

    public HashMap<Integer, Boolean> getErrors()
    {
        return errors;
    }

    public void append_to_error(String string)
    {
        error_text += string + "\n";
    }
}
