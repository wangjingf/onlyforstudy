package io.study.data.graphql.model.type;

import java.util.LinkedList;
import java.util.List;

public class EnumType implements Type {
    List<ScaleType> fields = new LinkedList<>();
}
