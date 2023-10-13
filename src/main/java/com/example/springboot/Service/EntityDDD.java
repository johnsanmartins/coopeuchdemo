package com.example.springboot.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.emma.store.application.entity.anotation.Colum;
import com.emma.store.application.entity.anotation.ID;
import com.emma.store.application.entity.anotation.TableName;
import com.emma.store.application.exception.AnnotationException;

public class EntityDDD<T> {

	private T val;
	private final Class<T> clase;

	public EntityDDD(T val, Class<T> clase) {
		this.val = val;
		this.clase = clase;
	}

	public String tableName() {
		String name = "";
		Annotation[] annotations = clase.getAnnotations();
		for (Annotation annotatedType : annotations) {
			if (annotatedType instanceof TableName) {
				TableName table = (TableName) annotatedType;
				name = table.name();
			}
		}
		if (name.isEmpty()) {
			throw new AnnotationException(clase.getSimpleName(), TableName.class.getSimpleName());
		}
		return name;
	}

	public String schemaName() {
		String name = "";
		Annotation[] annotations = clase.getAnnotations();
		for (Annotation annotatedType : annotations) {
			if (annotatedType instanceof TableName) {
				TableName table = (TableName) annotatedType;
				name = table.schema();
			}
		}
		if (name.isEmpty()) {
			throw new AnnotationException(clase.getSimpleName(), TableName.class.getSimpleName());
		}
		return name;
	}

	public String camposName() {
		String name = "";
		StringJoiner sj = new StringJoiner(", ");
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			Colum colum = field.getAnnotation(Colum.class);
			if (!Objects.isNull(colum)) {
				sj.add(colum.name());
			}
		}
		name = sj.toString();
		return name;
	}

	public Object[] camposSetUpdate() {
		List<Object> arg = new ArrayList<>();
		for (Field field : clase.getDeclaredFields()) {
			Colum colum = field.getAnnotation(Colum.class);
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(colum) && Objects.isNull(id) && !colum.foreignKey()) {
				if (colum.updatedAt()) {
					arg.add( new Date());
				} else if(colum.defaul()) {
					// not argument
				} else {
					Object object = runGetter(field, val);
					if(object instanceof String) {
						String fields = (String) object;
						if(!fields.isBlank()) {
							arg.add(object);
						}
					} else if(Objects.nonNull(object)) {
						arg.add(object);
					}
				}
			}
		}
		return arg.toArray();
	}

	public String camposSetUpdateName() {
		String name = "";
		StringJoiner sj = new StringJoiner(", ");
		for (Field field : clase.getDeclaredFields()) {
			Colum colum = field.getAnnotation(Colum.class);
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(colum) && Objects.isNull(id) && !colum.foreignKey()) {
				StringBuilder sb = new StringBuilder();
				if (colum.updatedAt()) {
					sb.append(colum.name()).append(" = ").append(" ? ");
					sj.add(sb.toString());
				} else if(colum.defaul()) {
					// not argument
				} else {
					Object object = runGetter(field, val);
					
					if(object instanceof String) {
						String fields = (String) object;
						if(!fields.isBlank()) {
							sb.append(colum.name()).append(" = ").append(" ? ");
							sj.add(sb.toString());
						}
					} else if(Objects.nonNull(object)) {
						sb.append(colum.name()).append(" = ").append(" ? ");
						sj.add(sb.toString());
					}
				}
			}
		}
		name = sj.toString();
		return name;
	}

	public String camposInsertName() {
		String name = "";
		StringJoiner sj = new StringJoiner(", ");
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			Colum colum = field.getAnnotation(Colum.class);
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(colum) && Objects.isNull(id)) {
				if (!colum.defaul()) {
					Object object = runGetter(field, val);
					if(Objects.nonNull(object)) {
						sj.add(colum.name());
					}
				}
			}
		}
		name = sj.toString();
		return name;
	}
	
	public Object[] insertValues() {
		return this.camposInsert();
	}
	
	public String idName() {
		String name = "";
		StringJoiner sj = new StringJoiner(", ");
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			Colum colum = field.getAnnotation(Colum.class);
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(id)) {
				if (!colum.defaul()) {
					sj.add(colum.name());
				}
			}
		}
		name = sj.toString();
		return name;
	}
	
	public String valuesInsertName() {
		String name = "";
		StringJoiner sj = new StringJoiner(", ");
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			Colum colum = field.getAnnotation(Colum.class);
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(colum) && Objects.isNull(id)) {
				if (!colum.defaul()) {
					Object object = runGetter(field, val);
					if(Objects.nonNull(object)) {
						sj.add("?");
					}
				}
			}
		}
		name = sj.toString();
		return name;
	}
	
	public Object[] camposInsert() {
		List<Object> arg = new ArrayList<>();
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			Colum colum = field.getAnnotation(Colum.class);
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(colum) && Objects.isNull(id)) {
				if (!colum.defaul()) {
					Object object = runGetter(field, val);
					if(Objects.nonNull(object)) {
						 arg.add(runGetter(field, val));
					}
				}
			}
		}
		return arg.toArray();
	}

	public T setID(Object object, T data) {
		Field[] file = clase.getDeclaredFields();
		for (Field field : file) {
			ID id = field.getAnnotation(ID.class);
			if (!Objects.isNull(id)) {
				runSetter(field, object, data);
			}
		}
		return data;
	}

	private Object runGetter(Field field, T instance) {
		Object value = null;
		for (Method method : clase.getMethods()) {
			if ((method.getName().startsWith("get")) && (method.getName().length() == field.getName().length() + 3)) {
				if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					try {
						value = method.invoke(instance);
					} catch (IllegalAccessException e) {
					} catch (IllegalArgumentException e) {
					} catch (InvocationTargetException e) {
					}
				}
			}
		}
		return value;
	}


	private void runSetter(Field field, Object object, T instance) {
		for (Method method : clase.getMethods()) {
			if ((method.getName().startsWith("set")) && (method.getName().length() == field.getName().length() + 3)) {
				if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					try {
						method.invoke(instance, object);
					} catch (IllegalAccessException e) {
					} catch (IllegalArgumentException e) {
					} catch (InvocationTargetException e) {
					}
				}
			}
		}
	}
	
	public void setVal(T val) {
		this.val = val;
	}
	
}

