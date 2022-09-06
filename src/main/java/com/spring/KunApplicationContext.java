package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * @author guokun
 * @date 2022/9/5 17:03
 */
public class KunApplicationContext {
    private Class<?> configClass;
    private HashMap<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private HashMap<String, Object> singletonObjects = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList = new LinkedList<>();

    public KunApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        componentScan();

        createSingletonBeans();
    }

    private void createSingletonBeans() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            // 检查是否是singleton
            if ("singleton".equals(beanDefinition.getScope())) {
                Class<?> cla = beanDefinition.getType();
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private void componentScan() {
        // 扫描包
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
            // 获取包路径 xx.xx.xx
            String packagePath = componentScanAnnotation.value();
            // 格式转换为 xx/xx/xx (因为我们要去加载的是class文件，通过文件系统去读取)
            String filePath = packagePath.replace(".", "/");
            // 获取类加载器(AppClassLoader)
            ClassLoader classLoader = KunApplicationContext.class.getClassLoader();
            // 使用AppClassLoader去获取指定包下的class文件夹路径
            URL targetClassUrl = classLoader.getResource(filePath);
            // 获取对应的文件目录
            assert targetClassUrl != null;
            File targetClassDir = new File(targetClassUrl.getFile());
            // 检查是否是一个文件目录
            if (targetClassDir.isDirectory()) {
                // 获取所有子文件 （先只考虑当前包，不考虑子包，假设子文件都是class文件）
                File[] classFiles = targetClassDir.listFiles();
                assert classFiles != null;
                List<Class<?>> classList = new ArrayList<>(classFiles.length);
                for (File classFile : classFiles) {
                    // 获取class文件名称 xx.class
                    String classFileName = classFile.getName();
                    // 获取类名（去除.class)
                    String className = classFileName.substring(0, classFileName.length() - 6);
                    String classFullName = packagePath + "." + className;
                    try {
                        // 使用类加载器(AppClassLoader)加载类
                        Class<?> loadClass = classLoader.loadClass(classFullName);
                        // 检查是否含有Component注解
                        if (loadClass.isAnnotationPresent(Component.class)) {
                            // 创建BeanDefinition
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(loadClass);

                            // 获取beanName
                            Component componentAnnotation = loadClass.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
                            // 如果beanName没有设置则使用类名的小驼峰命名作为beanName
                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(loadClass.getSimpleName());
                            }

                            // 查看是否是单例bean
                            if (loadClass.isAnnotationPresent(Scope.class)
                                    && "prototype".equals(loadClass.getAnnotation(Scope.class).value())) {
                                // 多例bean
                                beanDefinition.setScope("prototype");
                            } else {
                                // 单例bean
                                beanDefinition.setScope("singleton");
                            }

                            // 存储BeanDefinition到map中
                            beanDefinitionMap.put(beanName, beanDefinition);

                            // 判断是否实现了BeanPostProcessor，如果实现了则放入beanPostProcessorList中
                            if (BeanPostProcessor.class.isAssignableFrom(loadClass)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) loadClass.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessor);
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        // 如果BeanDefinitionMap中没有对应的则说明该bean不在扫描的范围内
        if (!beanDefinitionMap.containsKey(beanName)) {
            return null;
        }

        // 有对应的BeanDefinition
        Object bean = null;
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("prototype".equals(beanDefinition.getScope())) {
            // 多例
            bean = createBean(beanName, beanDefinition);
        } else {
            // 单例
            bean = singletonObjects.get(beanName);

            // 考虑还未创建的单例bean被获取时，提前创建（可能出现循环依赖）
            if (bean == null) {
                bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }

        return bean;
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> cla = beanDefinition.getType();
        Object bean = null;
        try {
            bean = cla.getDeclaredConstructor().newInstance();

            // 依赖注入
            Field[] declaredFields = cla.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    String typeName = Introspector.decapitalize(type.getSimpleName());
                    Object iocBean = getBean(typeName);
                    if (iocBean == null) {
                        iocBean = getBean(field.getName());
                    }
                    field.set(bean, iocBean);
                }
            }

            // Aware注入Spring对象属性
            if (bean instanceof Aware) {
                if (bean instanceof BeanNameAware) {
                    ((BeanNameAware) bean).setBeanName(beanName);
                }
            }

            // 执行InitializingBean的afterPropertiesSet方法
            if (bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }

            // 执行所有BeanPostProcessor的before
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessorBeforeInitialization(bean, beanName);
            }

            // 初始化 To Do

            // 执行所有BeanPostProcessor的after
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessorAfterInitialization(bean, beanName);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
