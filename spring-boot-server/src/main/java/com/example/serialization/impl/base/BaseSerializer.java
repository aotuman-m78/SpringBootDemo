package com.example.serialization.impl.base;

import com.example.model.wrapper.SerializeWrapper;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by fangrui on 2018/6/13.
 */
public abstract class BaseSerializer {
    protected final static Class<SerializeWrapper> WRAPPER_CLASS = SerializeWrapper.class;

    protected final static Set<Class<?>> set = Sets.newHashSet();

    static {
        set.add(List.class);
        set.add(Map.class);
        set.add(Set.class);
        set.add(ArrayList.class);
        set.add(HashMap.class);
        set.add(HashSet.class);
        set.add(LinkedList.class);
        set.add(ConcurrentMap.class);
        set.add(Hashtable.class);
        set.add(TreeMap.class);
        set.add(SortedMap.class);
        set.add(Vector.class);
        set.add(Stack.class);
    }
}
