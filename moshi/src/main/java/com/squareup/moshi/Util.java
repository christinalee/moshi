/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.moshi;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

final class Util {
  public static final Set<Annotation> NO_ANNOTATIONS = Collections.emptySet();

  private Util() {
  }

  public static boolean typesMatch(Type pattern, Type candidate) {
    // TODO: permit raw types (like Set.class) to match non-raw candidates (like Set<Long>).
    return pattern.equals(candidate);
  }

  public static Set<? extends Annotation> jsonAnnotations(AnnotatedElement annotatedElement) {
    return jsonAnnotations(annotatedElement.getAnnotations());
  }

  public static Set<? extends Annotation> jsonAnnotations(Annotation[] annotations) {
    Set<Annotation> result = null;
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().isAnnotationPresent(JsonQualifier.class)) {
        if (result == null) result = new LinkedHashSet<>();
        result.add(annotation);
      }
    }
    return result != null ? Collections.unmodifiableSet(result) : Util.NO_ANNOTATIONS;
  }

  public static boolean isAnnotationPresent(
      Set<? extends Annotation> annotations, Class<? extends Annotation> annotationClass) {
    if (annotations.isEmpty()) return false; // Save an iterator in the common case.
    for (Annotation annotation : annotations) {
      if (annotation.annotationType() == annotationClass) return true;
    }
    return false;
  }

  /** Returns true if {@code annotations} has any annotation whose simple name is Nullable. */
  public static boolean hasNullable(Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().getSimpleName().equals("Nullable")) {
        return true;
      }
    }
    return false;
  }

  public static int[] doubleArray(int[] array) {
    int[] result = new int[array.length * 2];
    System.arraycopy(array, 0, result, 0, array.length);
    return result;
  }

  public static <T> T[] doubleArray(T[] array) {
    @SuppressWarnings("unchecked") // We use the same type as 'array'.
    T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length * 2);
    System.arraycopy(array, 0, result, 0, array.length);
    return result;
  }
}
