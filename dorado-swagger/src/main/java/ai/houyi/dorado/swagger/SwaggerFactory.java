/*
 * Copyright 2017 The OpenDSP Project
 *
 * The OpenDSP Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package ai.houyi.dorado.swagger;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import ai.houyi.dorado.Dorado;
import ai.houyi.dorado.rest.util.PackageScanner;
import ai.houyi.dorado.swagger.ext.ApiInfoBuilder;
import io.swagger.models.Swagger;

/**
 * 
 * @author wangwp
 */
public class SwaggerFactory {
	private static Swagger swagger;
	private static ApiInfoBuilder apiInfoBuilder;

	static {
		ServiceLoader<ApiInfoBuilder> apiInfoBuilders = ServiceLoader.load(ApiInfoBuilder.class);
		apiInfoBuilder = apiInfoBuilders.iterator().hasNext() ? apiInfoBuilders.iterator().next() : null;

		try {

			if (apiInfoBuilder == null) {
				if (Dorado.isEnableSpring) {
					apiInfoBuilder = Dorado.beanContainer.getBean(ApiInfoBuilder.class);
				}
			}
		} catch (Throwable ex) {
			// ignore this exception
		}
	}

	public static Swagger getSwagger() {
		if (swagger != null)
			return swagger;

		Reader reader = new Reader(new Swagger());

		String[] packages = null;
		Class<?> mainClass = Dorado.mainClass;
		EnableSwagger enableSwagger = mainClass.getAnnotation(EnableSwagger.class);

		if (enableSwagger != null) {
			packages = enableSwagger.value();
		}

		if (packages == null || packages.length == 0) {
			packages = Dorado.serverConfig.scanPackages();
		}

		if (packages == null || packages.length == 0) {
			packages = new String[] { mainClass.getPackage().getName() };
		}

		if (packages == null || packages.length == 0) {
			throw new IllegalArgumentException("缺少scanPackages设置");
		}

		Set<Class<?>> classes = new HashSet<>();
		for (String pkg : packages) {
			try {
				classes.addAll(PackageScanner.scan(pkg));
			} catch (Exception ex) {
				// ignore this ex
			}
		}

		Swagger _swagger = reader.read(classes);
		if (apiInfoBuilder != null) {
			_swagger.setInfo(apiInfoBuilder.buildInfo());
			_swagger.setSchemes(apiInfoBuilder.schemes());
		}
		swagger = _swagger;
		return _swagger;
	}
}
