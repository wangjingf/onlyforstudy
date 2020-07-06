package io.study.lang;

public interface IVisitor<T extends IVisitable> {
  boolean preVisit(T t);
  void postVisit(T t);
}
