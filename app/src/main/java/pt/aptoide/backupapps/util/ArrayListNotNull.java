package pt.aptoide.backupapps.util;

import java.util.ArrayList;

/**
 * Created by neuro on 30-03-2016.
 */
public class ArrayListNotNull<E> extends ArrayList<E> {

  public ArrayListNotNull(ArrayList<E> arrayList) {
    for (E e : arrayList) {
      if (e != null) {
        add(e);
      }
    }
  }
}
