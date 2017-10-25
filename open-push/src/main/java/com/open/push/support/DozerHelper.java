package com.open.push.support;

import java.util.ArrayList;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * Use dozer map a list.
 */
public class DozerHelper {

  private static DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

  public static <T, U> ArrayList<U> map(final Mapper mapper, final List<T> source,
      final Class<U> destType) {

    final ArrayList<U> dest = new ArrayList<>();

    for (T element : source) {
      if (element == null) {
        continue;
      }
      dest.add(mapper.map(element, destType));
    }

    // finally remove all null values if any
    List<U> s1 = new ArrayList<>();
    s1.add(null);
    dest.removeAll(s1);

    return dest;
  }

  public static <T, U> ArrayList<U> map(final List<T> source, final Class<U> destType) {
    return map(dozerBeanMapper, source, destType);
  }

  public static <T, U> U map(final T source, final Class<U> destType) {
    return dozerBeanMapper.map(source, destType);
  }

  public static <T, U> void map(final T source, final U dest) {
    dozerBeanMapper.map(source, dest);

  }
}
