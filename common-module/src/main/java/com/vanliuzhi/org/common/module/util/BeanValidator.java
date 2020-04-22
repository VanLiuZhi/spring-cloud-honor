package com.vanliuzhi.org.common.module.util;

import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @Description hibernate-validator校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * // TODO 依赖问题，猜测原项目被其它依赖注入了org.apache.collections:commons-collections4:4.1，而此时我的项目是没有的
 * // TODO 所以先用 commons-collections:commons-collections:3.2.2
 * // 依赖下面这个，源码找到的是collections，但是原项目是collections4
 * // 它们的包结构除了4以外都一样，artifactId 不一样
 * //
 * // commons-collections:commons-collections:3.2.2
 * // org.apache.collections:commons-collections4:4.1
 *
 * // <dependency>
 * //   <groupId>commons-collections</groupId>
 * //   <artifactId>commons-collections</artifactId>
 * // </dependency>
 *
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@SuppressWarnings("all")
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap errors = new LinkedHashMap<>();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String, String> validateList(Collection<?> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        Iterator iterator = collection.iterator();
        Map errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());

        return errors;
    }

    public static Map<String, String> validateObject(Object first, Object... objects) {

        if (objects != null && objects.length > 0) {

            List<Object> list = Arrays.asList(objects);
            list.add(0, first);

            return validateList(list);
        } else {
            return validate(first, new Class[0]);
        }
    }

    public static void check(Object param) throws IllegalArgumentException {
        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new IllegalArgumentException(map.toString());
        }
    }

    // public static void main(String[] args) {
    //
    // 	SysUser user = new SysUser ();
    // 	user.setId(1L);
    // 	BeanValidator.check(user);
    //
    //
    // }
}
