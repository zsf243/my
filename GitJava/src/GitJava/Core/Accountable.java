package GitJava.Core;


import java.util.Collection;

/**
 * An object whose RAM usage can be computed.
 *
 * @lucene.internal
 */
public interface Accountable
{
    /**
     * Return the memory usage of this object in bytes. Negative values are illegal.
     */
    long ramBytesUsed();

    /**
     * Returns nested resources of this class. 
     * The result should be a point-in-time snapshot (to avoid race conditions).
     * @see Accountables
     */
    // TODO: on java8 make this a default method returning emptyList
    Collection<Accountable> getChildResources();
}
