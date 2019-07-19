/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.testbed.routing.here;

import org.n52.iceland.config.spring.ProviderAwareListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * Class to customize the {@link org.springframework.beans.factory.ListableBeanFactory} of the application context.
 * Mostly copied from {@link org.springframework.test.context.support.GenericXmlContextLoader}
 * and {@link org.springframework.test.context.support.AbstractGenericContextLoader}.
 */
public class ProviderAwareGenericXmlContextLoader extends AbstractContextLoader {

    @Override
    public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
        validateMergedContextConfiguration(mergedConfig);

        GenericApplicationContext context = new GenericApplicationContext(createDefaultListableBeanFactory());
        Optional.ofNullable(mergedConfig.getParentApplicationContext()).ifPresent(context::setParent);
        prepareContext(context, mergedConfig);
        loadBeanDefinitions(context, mergedConfig.getLocations());
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        customizeContext(context, mergedConfig);
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    private ProviderAwareListableBeanFactory createDefaultListableBeanFactory() {
        return new ProviderAwareListableBeanFactory();
    }

    @Override
    public ApplicationContext loadContext(String... locations) {
        GenericApplicationContext context = new GenericApplicationContext(createDefaultListableBeanFactory());
        loadBeanDefinitions(context, locations);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    @Override
    protected String getResourceSuffix() {
        return "-context.xml";
    }

    private void loadBeanDefinitions(GenericApplicationContext context, String[] locations) {
        new XmlBeanDefinitionReader(context).loadBeanDefinitions(locations);
    }

    private void validateMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
        if (mergedConfig.hasClasses()) {
            String msg = String.format(
                    "Test class [%s] has been configured with @ContextConfiguration's 'classes' attribute %s, "
                            + "but %s does not support annotated classes.", mergedConfig.getTestClass().getName(),
                    ObjectUtils.nullSafeToString(mergedConfig.getClasses()), getClass().getSimpleName());
            throw new IllegalStateException(msg);
        }
    }
}
