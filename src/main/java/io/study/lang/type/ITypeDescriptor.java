package io.study.lang.type;

import io.entropy.lang.SourceLocation;
import ITypeVariable;
import impl.JavaTypeDescriptorBuilder;
import impl.TypeDescriptorBuilder;
import impl.TypeDescriptorParser;
import io.entropy.xlang.IRenamer;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ITypeDescriptor extends IType {
    ITypeDescriptor[] EMPTY_TYPES = new ITypeDescriptor[0];

    boolean isObjectType();

    ITypeDescriptor getComponentType();

    List<ITypeVariable> getGenericTypeArguments();

    default ITypeVariable getGenericTypeArgument(int var1) {
        return (ITypeVariable)this.getGenericTypeArguments().get(var1);
    }

    default boolean isRealType() {
        if (this.isGenericType()) {
            return false;
        } else {
            Iterator var1 = this.getGenericTypeArguments().iterator();

            ITypeVariable var2;
            do {
                if (!var1.hasNext()) {
                    return true;
                }

                var2 = (ITypeVariable)var1.next();
            } while(var2.isRealType());

            return false;
        }
    }

    String getRawTypeName();

    String getGenericTypeString();

    boolean isPrimitive();

    boolean isNumberType();

    boolean isArray();

    default boolean isGenericType() {
        return !this.getGenericTypeArguments().isEmpty();
    }

    Object getDefaultValue();

    ITypeDescriptor applyRename(IRenamer var1);

    static TypeDescriptorBuilder builder() {
        return new TypeDescriptorBuilder();
    }

    static ITypeDescriptor fromJavaType(Type var0) {
        return (new JavaTypeDescriptorBuilder()).buildFromJavaType(var0);
    }

    static ITypeDescriptor fromJavaType(Type var0, Map<? extends Type, ITypeDescriptor> var1) {
        return (new JavaTypeDescriptorBuilder(var1)).buildFromJavaType(var0);
    }

    static ITypeDescriptor fromRawTypeName(String var0) {
        return builder().rawTypeName(var0).build();
    }

    static ITypeDescriptor fromComponentType(ITypeDescriptor var0) {
        return builder().componentType(var0).build();
    }

    static ITypeDescriptor parseFromText(SourceLocation var0, String var1, boolean var2) {
        return (new TypeDescriptorParser()).intern(var2).parseFromText(var0, var1);
    }

    static List<ITypeDescriptor> parseTypesFromText(SourceLocation var0, String var1, boolean var2) {
        return (new TypeDescriptorParser()).intern(var2).parseTypesFromText(var0, var1);
    }
}
