package pl.edu.pollub.virtualcasino.clientservices.samples

import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.NoSuchMessageException
import org.springframework.core.ResolvableType
import org.springframework.core.env.Environment
import org.springframework.core.io.Resource
import org.springframework.lang.Nullable

import java.lang.annotation.Annotation

class DummyApplicationContext {

    static ApplicationContext dummyApplicationContext() {
        return new ApplicationContext() {
            @Override
            String getId() {
                return null
            }

            @Override
            String getApplicationName() {
                return null
            }

            @Override
            String getDisplayName() {
                return null
            }

            @Override
            long getStartupDate() {
                return 0
            }

            @Override
            ApplicationContext getParent() {
                return null
            }

            @Override
            AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
                return null
            }

            @Override
            BeanFactory getParentBeanFactory() {
                return null
            }

            @Override
            boolean containsLocalBean(String name) {
                return false
            }

            @Override
            boolean containsBeanDefinition(String beanName) {
                return false
            }

            @Override
            int getBeanDefinitionCount() {
                return 0
            }

            @Override
            String[] getBeanDefinitionNames() {
                return new String[0]
            }

            @Override
            String[] getBeanNamesForType(ResolvableType type) {
                return new String[0]
            }

            @Override
            String[] getBeanNamesForType(@Nullable Class<?> type) {
                return new String[0]
            }

            @Override
            String[] getBeanNamesForType(
                    @Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
                return new String[0]
            }

            @Override
            def <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
                return null
            }

            @Override
            def <T> Map<String, T> getBeansOfType(
                    @Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
                return null
            }

            @Override
            String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
                return new String[0]
            }

            @Override
            Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
                return null
            }

            @Override
            def <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
                return null
            }

            @Override
            Object getBean(String name) throws BeansException {
                return null
            }

            @Override
            def <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                return null
            }

            @Override
            Object getBean(String name, Object... args) throws BeansException {
                return null
            }

            @Override
            def <T> T getBean(Class<T> requiredType) throws BeansException {
                return null
            }

            @Override
            def <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
                return null
            }

            @Override
            def <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
                return null
            }

            @Override
            def <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
                return null
            }

            @Override
            boolean containsBean(String name) {
                return false
            }

            @Override
            boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
                return false
            }

            @Override
            boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
                return false
            }

            @Override
            boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
                return false
            }

            @Override
            boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
                return false
            }

            @Override
            Class<?> getType(String name) throws NoSuchBeanDefinitionException {
                return null
            }

            @Override
            String[] getAliases(String name) {
                return new String[0]
            }

            @Override
            void publishEvent(Object event) {

            }

            @Override
            String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
                return null
            }

            @Override
            String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
                return null
            }

            @Override
            String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
                return null
            }

            @Override
            Environment getEnvironment() {
                return null
            }

            @Override
            Resource[] getResources(String locationPattern) throws IOException {
                return new Resource[0]
            }

            @Override
            Resource getResource(String location) {
                return null
            }

            @Override
            ClassLoader getClassLoader() {
                return null
            }
        }
    }
}
