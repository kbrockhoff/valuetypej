/*
 * Copyright (c) 2008 Kevin Brockhoff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codekaizen.vtj.intervals.math;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.codekaizen.vtj.intervals.IntervalValueType;


/**
 * <p>Holds an interval tree with the intervals mapped to values and more than one value allowed to be mapped to the
 * same interval.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
@SuppressWarnings("unchecked")
public interface IntervalTreeMultimap<K extends IntervalValueType, V> {

    /**
     * Returns the number of key-value pairs in the multimap.
     *
     * @return  DOCUMENT ME!
     */
    int size();

    /**
     * The maximum depth of the tree.
     *
     * @return  the number of node layers
     */
    int depth();

    /**
     * Returns the total number of nodes in the tree.
     *
     * @return  the number of nodes
     */
    int nodeSize();

    /**
     * Returns {@code true} if the multimap contains no key-value pairs.
     *
     * @return  DOCUMENT ME!
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if the multimap contains any values for the specified key.
     *
     * @param  key  key to search for in multimap
     *
     * @return  DOCUMENT ME!
     */
    boolean containsKey(Object key);

    /**
     * Returns {@code true} if the multimap contains the specified value for any key.
     *
     * @param  value  value to search for in multimap
     *
     * @return  DOCUMENT ME!
     */
    boolean containsValue(Object value);

    /**
     * Returns {@code true} if the multimap contains the specified key-value pair.
     *
     * @param  key  key to search for in multimap
     * @param  value  value to search for in multimap
     *
     * @return  DOCUMENT ME!
     */
    boolean containsEntry(Object key, Object value);

    // Modification Operations

    /**
     * Stores a key-value pair in the multimap.
     *
     * <p>Some multimap implementations allow duplicate key-value pairs, in which case {@code put} always adds a new
     * key-value pair and increases the multimap size by 1. Other implementations prohibit duplicates, and storing a
     * key-value pair that's already in the multimap has no effect.</p>
     *
     * @param  key  key to store in the multimap
     * @param  value  value to store in the multimap
     *
     * @return  {@code true} if the method increased the size of the multimap, or {@code false} if the multimap already
     *          contained the key-value pair and doesn't allow duplicates
     */
    boolean put(K key, V value);

    /**
     * Removes a key-value pair from the multimap.
     *
     * @param  key  key of entry to remove from the multimap
     * @param  value  value of entry to remove the multimap
     *
     * @return  {@code true} if the multimap changed
     */
    boolean remove(Object key, Object value);

    // Bulk Operations

    /**
     * Stores a collection of values with the same key.
     *
     * @param  key  key to store in the multimap
     * @param  values  values to store in the multimap
     */
    void putAll(K key, Iterable<? extends V> values);

    /**
     * Copies all of another multimap's key-value pairs into this multimap. The order in which the mappings are added is
     * determined by {@code multimap.entries()}.
     *
     * @param  multimap  mappings to store in this multimap
     */
    void putAll(IntervalTreeMultimap<? extends K, ? extends V> multimap);

    /**
     * Stores a collection of values with the same key, replacing any existing values for that key.
     *
     * @param  key  key to store in the multimap
     * @param  values  values to store in the multimap
     *
     * @return  the collection of replaced values, or an empty collection if no values were previously associated with
     *          the key. The collection is modifiable, but updating it will have no effect on the multimap.
     */
    Collection<V> replaceValues(K key, Iterable<? extends V> values);

    /**
     * Removes all values associated with a given key.
     *
     * @param  key  key of entries to remove from the multimap
     *
     * @return  the collection of removed values, or an empty collection if no values were associated with the provided
     *          key. The collection is modifiable, but updating it will have no effect on the multimap.
     */
    Collection<V> removeAll(Object key);

    /**
     * Removes all key-value pairs from the multimap.
     */
    void clear();

    // Views

    /**
     * Returns a collection view of all values associated with a key. If no mappings in the multimap have the provided
     * key, an empty collection is returned.
     *
     * <p>Changes to the returned collection will update the underlying multimap, and vice versa.</p>
     *
     * @param  key  key to search for in multimap
     *
     * @return  the collection of values that the key maps to
     */
    Collection<V> get(K key);

    /**
     * Returns the set of all keys, each appearing once in the returned set. Changes to the returned set will update the
     * underlying multimap, and vice versa.
     *
     * @return  the collection of distinct keys
     */
    Set<K> keySet();

    /**
     * Returns a collection, which may contain duplicates, of all keys. The number of times of key appears in the
     * returned multiset equals the number of mappings the key has in the multimap. Changes to the returned multiset
     * will update the underlying multimap, and vice versa.
     *
     * @return  a multiset with keys corresponding to the distinct keys of the multimap and frequencies corresponding to
     *          the number of values that each key maps to
     */
    Collection<K> keys();

    /**
     * Returns a collection of all values in the multimap. Changes to the returned collection will update the underlying
     * multimap, and vice versa.
     *
     * @return  collection of values, which may include the same value multiple times if it occurs in multiple mappings
     */
    Collection<V> values();

    /**
     * Returns a collection of all key-value pairs. Changes to the returned collection will update the underlying
     * multimap, and vice versa. The entries collection does not support the {@code add} or {@code addAll} operations.
     *
     * @return  collection of map entries consisting of key-value pairs
     */
    Collection<Map.Entry<K, V>> entries();

    /**
     * Returns a map view that associates each key with the corresponding values in the multimap. Changes to the
     * returned map, such as element removal, will update the underlying multimap. The map does not support {@code
     * setValue()} on its entries, {@code put}, or {@code putAll}.
     *
     * <p>The collections returned by {@code asMap().get(Object)} have the same behavior as those returned by {@link
     * #get}.</p>
     *
     * @return  a map view from a key to its collection of values
     */
    Map<K, Collection<V>> asMap();

    /**
     * Returns all values whose key contains the input parameter.
     *
     * @param  value  the interval value to match
     *
     * @return  the values of all containing keys
     */
    Collection<V> query(Object value);

    /**
     * Returns all values whose key overlaps the input parameter.
     *
     * @param  interval  the interval to match
     *
     * @return  the values of all overlapping keys
     */
    Collection<V> query(IntervalValueType interval);

}
