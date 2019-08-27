package aQute.bnd.classfile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public abstract class AnnotationsAttribute implements Attribute {
	public final AnnotationInfo[] annotations;

	AnnotationsAttribute(AnnotationInfo[] annotations) {
		this.annotations = annotations;
	}

	@Override
	public String toString() {
		return name() + " " + Arrays.toString(annotations);
	}

	static <A extends AnnotationsAttribute> A read(DataInput in, ConstantPool constant_pool,
		Function<AnnotationInfo[], A> constructor) throws IOException {
		AnnotationInfo[] annotations = AnnotationInfo.readInfos(in, constant_pool);
		return constructor.apply(annotations);
	}

	@Override
	public void write(DataOutput out, ConstantPool constant_pool) throws IOException {
		int attribute_name_index = constant_pool.utf8Info(name());
		int attribute_length = attribute_length();
		out.writeShort(attribute_name_index);
		out.writeInt(attribute_length);
		AnnotationInfo.writeInfos(out, constant_pool, annotations);
	}

	@Override
	public int attribute_length() {
		int attribute_length = AnnotationInfo.infos_length(annotations);
		return attribute_length;
	}
}
