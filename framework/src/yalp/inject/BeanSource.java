package yalp.inject;

public interface BeanSource {

    public <T> T getBeanOfType(Class<T> clazz);

}
