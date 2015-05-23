package GitJava.Core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 Helper methods for constructing nested resource descriptions and debugging RAM usage.
 {@code toString(AccountableC1}} can be used to quickly debug the nested structure of any AccountableC1.
 The {@code namedAccountable} and {@code namedAccountables} methods return
 type-safe, point-in-time snapshots of the provided resources.
 */
public class Accountables
{
    private Accountables()
    {
    }

    /** Returns a String description of an AccountableC1 and any nested resources. This is intended for development and debugging. */
    public static String toString(AccountableC1 a)
    {
        StringBuilder sb = new StringBuilder();
        toString(sb, a, 0);
        return sb.toString();
    }

    private static StringBuilder toString(StringBuilder dest, AccountableC1 a, int depth)
    {
        for (int i = 1; i < depth; i++)
        {
            dest.append("    ");
        }

        if (depth > 0)
        {
            dest.append("|-- ");
        }

        dest.append(a.toString());
        dest.append(": ");
        dest.append(System.lineSeparator());

        for (AccountableC1 child : a.getChildResources())
        {
            toString(dest, child, depth + 1);
        }

        return dest;
    }

    /**
     Augments an existing accountable with the provided description.
     The resource description is constructed in this format: {@code description [toString()]}
     This is a point-in-time type safe view: consumers will not be able to cast or manipulate the resource in any way.
     */
    public static AccountableC1 namedAccountable(String description, AccountableC1 in)
    {
        return namedAccountable(description + " [" + in + "]", in.getChildResources(), in.ramBytesUsedC1());
    }

    /** Returns an accountable with the provided description and bytes. */
    public static AccountableC1 namedAccountable(String description, long bytes)
    {
        return namedAccountable(description, Collections.<AccountableC1>emptyList(), bytes);
    }

    /**
     Converts a map of resources to a collection. The resource descriptions are constructed in this format:
     {@code prefix 'key' [toString()]} This is a point-in-time type safe view: consumers
     will not be able to cast or manipulate the resources in any way.
     */
    public static Collection<AccountableC1> namedAccountables(String prefix, Map<?, ? extends AccountableC1> in)
    {
        List<AccountableC1> resources = new ArrayList<>();
        for (Map.Entry<?, ? extends AccountableC1> kv : in.entrySet())
        {
            resources.add(namedAccountable(prefix + " '" + kv.getKey() + "'", kv.getValue()));
        }
        Collections.sort(resources, new Comparator<AccountableC1>()
        {
            @Override
            public int compare(AccountableC1 o1, AccountableC1 o2)
            {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return Collections.unmodifiableList(resources);
    }

    /**
     Returns an accountable with the provided description, children and bytes.
     The resource descriptions are constructed in this format:
     {@code description [toString()]} This is a point-in-time type safe view: consumers
     will not be able to cast or manipulate the resources in any way, provided
     that the passed in children Accountables (and all their descendants) were created
     with one of the namedAccountable functions.
     */
    public static AccountableC1 namedAccountable(final String description, final Collection<AccountableC1> children, final long bytes)
    {
        return new AccountableC1()
        {
            @Override
            public long ramBytesUsedC1()
            {
                return bytes;
            }

            @Override
            public Collection<AccountableC1> getChildResources()
            {
                return children;
            }

            @Override
            public String toString()
            {
                return description;
            }
        };
    }
}
