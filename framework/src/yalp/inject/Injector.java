package yalp.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import javax.inject.Inject;
import yalp.Yalp;
import yalp.classloading.enhancers.ControllersEnhancer.ControllerSupport;
import yalp.jobs.Job;
import yalp.mvc.Mailer;

public class Injector {
    
    /**
     * For now, inject beans in controllers
     */
    public static void inject(BeanSource source) {
        List<Class> classes = Yalp.classloader.getAssignableClasses(ControllerSupport.class);
        classes.addAll(Yalp.classloader.getAssignableClasses(Mailer.class));
        classes.addAll(Yalp.classloader.getAssignableClasses(Job.class));
        for(Class<?> clazz : classes) {
            for(Field field : clazz.getDeclaredFields()) {
                if(Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Inject.class)) {
                    Class<?> type = field.getType();
                    field.setAccessible(true);
                    try {
                        field.set(null, source.getBeanOfType(type));
                    } catch(RuntimeException e) {
                        throw e;
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
