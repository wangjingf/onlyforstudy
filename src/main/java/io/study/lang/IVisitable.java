package io.study.lang;

public interface IVisitable<T extends IVisitor> {
    public void accept(T t);
}
