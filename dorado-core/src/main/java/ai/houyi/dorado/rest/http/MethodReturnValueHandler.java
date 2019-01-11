/*
 * Copyright 2017 The OpenAds Project
 *
 * The OpenAds Project licenses this file to you under the Apache License,
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
package ai.houyi.dorado.rest.http;

import ai.houyi.dorado.rest.util.MethodDescriptor;

/**
 * @author weiping wang
 *
 */
public interface MethodReturnValueHandler {
	/**
	 * 方法调用正常返回结果处理器
	 * 
	 * @param value            方法执行结果
	 * @param methodDescriptor 方法描述
	 * @return 处理后的返回结果
	 */
	Object handleMethodReturnValue(Object value, MethodDescriptor methodDescriptor);

	/**
	 * 判断指定返回类型是否需要被此handler支持
	 * 
	 * @param returnType the MethodDescriptor for this method
	 * @return {@code true} if this handler supports the supplied return type;
	 *         {@code false} otherwise
	 */
	public boolean supportsReturnType(MethodDescriptor returnType);
}
