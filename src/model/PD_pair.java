package model;

public class PD_pair
{
    private Position position;
    private Long value;

    public PD_pair(Position position, Long value)
    {
        this.position = position;
        this.value = value;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Long getValue()
    {
        return value;
    }

    public void setValue(Long value)
    {
        this.value = value;
    }
}
