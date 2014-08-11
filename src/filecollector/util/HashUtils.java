package filecollector.util;

/**
 * Utility-Klasse zur Vereinfachung der Generierung von Hash-Codes 
 * f�r eigene Objekte. Die hashCode()-Methode bedient sich der hier
 * definierten �berladenen calcHashCode()-Methoden
 * <br>
 * USAGE:
 * <code>
 * <br> @Override
 * <br> public int hashCode() 
 * <br> {
 * <br>    int hash = 17;
 * <br>    hash = HashUtils.calcHashCode(hash, attribute1);
 * <br>    hash = HashUtils.calcHashCode(hash, ...);
 * <br>    hash = HashUtils.calcHashCode(hash, attributeN);
 * <br>    return hash;
 * <br> }
 * </code>
 * 
 * @author Michael Inden
 * 
 * Copyright 2011 by Michael Inden 
 */ 
public final class HashUtils
{
    public static final int PRIME = 113;

    private HashUtils()
    {
    }

    public static final int calcHashCode(final int hash, final boolean input)
    {
        return PRIME * hash + (input ? 1 : 0);
    }

    public static final int calcHashCode(final int hash, final int input)
    {
        return PRIME * hash + input;
    }

    public static final int calcHashCode(final int hash, final long input)
    {
        return PRIME * hash + (int) (input ^ (input >>> 32));
    }

    public static final int calcHashCode(final int hash, final float input)
    {
        return calcHashCode(hash, Float.floatToIntBits(input));
    }

    public static final int calcHashCode(final int hash, final double input)
    {
        return calcHashCode(hash, Double.doubleToLongBits(input));
    }

    public static final int calcHashCode(final int hash, final Object input)
    {
        return (input == null) ? 0 : PRIME * hash + input.hashCode();
    }
}